
package local.hal.st31.android.shift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.adapters.GroupMemberAdapter;
import local.hal.st31.android.shift.beans.BlackListBean;
import local.hal.st31.android.shift.db.DataAccess;
import local.hal.st31.android.shift.db.DatabaseHelper;
import local.hal.st31.android.shift.utils.GlobalUtils;

public class BlackListActivity extends BaseActivity {

    private static final String GROUP_URL = "http://flexibleshift.sakura.ne.jp/shift_app_backend/controllers/group_controller.php";
    private static final String BLACK_URL = "http://flexibleshift.sakura.ne.jp/shift_app_backend/controllers/test.php";

    private int userMemberId;
    private List<String> groupNameList;
    private String selectedGroup;
    private List<Integer> groupIdList;
    private int selectedGroupId;
    private List<String> groupMemberList;
    private List<Integer> groupMemberIdList;
    private RecyclerView recyclerView;
    private List<BlackListBean> blackMemberList;
    GroupMemberAdapter groupMemberAdapter;
    private ColorPicker picker;
    private DatabaseHelper _helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        groupNameList = new ArrayList<>();
        groupNameList.add("--------");
        groupIdList = new ArrayList<>();
        groupIdList.add(0);
        picker = findViewById(R.id.picker);
        picker.setOldCenterColor(picker.getColor());
        _helper = new DatabaseHelper(getApplicationContext());
        db = _helper.getWritableDatabase();


        SharedPreferences sp = getSharedPreferences("login", getApplicationContext().MODE_PRIVATE);
        userMemberId = sp.getInt("userId",0);
        Map<String,Integer> map = new HashMap<>();
        map.put("userId",userMemberId);
        map.put("postNo",1);
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
        String jsonGroupData = gson.toJson(map);
        GroupReceiver groupReceiver = new GroupReceiver();
        groupReceiver.execute(GROUP_URL,jsonGroupData);
    }

    private void spinnerHandler(){
        NiceSpinner niceSpinner = findViewById(R.id.group_spinner);
        Log.e("soso",groupNameList.toString());
        niceSpinner.attachDataSource(groupNameList);
        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                selectedGroup = parent.getItemAtPosition(position).toString();
                selectedGroupId = groupIdList.get(position);
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",selectedGroupId);
                map.put("userId",userMemberId);
                map.put("postNo",2);
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .create();
                String jsonMemberData = gson.toJson(map);
                GroupMemberReceiver groupMemberReceiver = new GroupMemberReceiver();
                groupMemberReceiver.execute(GROUP_URL,jsonMemberData);
            }
        });
    }

    private void swipeRecyclerViewHandler(){
        recyclerView = findViewById(R.id.memberRecyclerView);
        groupMemberAdapter = new GroupMemberAdapter(getApplicationContext());
        groupMemberAdapter.setListener(new GroupMemberAdapter.onMemberClickListener() {
            @Override
            public void onItemClick(int position, final BlackListBean blackListBean) {
                if (blackListBean.getBlackRank() == 0) {
                    blackListBean.setBlackRank(1);
                    picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                        @Override
                        public void onColorChanged(int color) {
                            blackListBean.setColorCode(color);
                            swipeRecyclerViewHandler();
                        }
                    });
                } else {
                    blackListBean.setBlackRank(0);

                }

            }
        });
        groupMemberAdapter.setData(blackMemberList);
        groupMemberAdapter.notifyDataSetChanged();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//縦並び
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupMemberAdapter);


    }

    public void sendButtonClick(View view){
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
        String submitJson = gson.toJson(blackMemberList);
        GroupMemberPoster groupMemberPoster = new GroupMemberPoster();
        groupMemberPoster.execute(BLACK_URL,submitJson);
        db = _helper.getWritableDatabase();
        for(int i = 0;i < blackMemberList.size(); i++){
            DataAccess.blackListReplace(db,blackMemberList.get(i));
        }
    }

    private class GroupReceiver extends AsyncTask<String,Void,String> {

        private static final String DEBUG_TAG = "GroupReceiver";
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String jsonData = params[1];
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(uri);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; utf-8"); // 追記
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                // サーバーへ送るJSONをセットする
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(jsonData);
                wr.flush();
                wr.close();

                con.connect();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException("ステータスコード："+status);
                }
                is = con.getInputStream();
                result = GlobalUtils.getInstance().is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for(int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                    String groupName = data.getString("group_name");
                    int groupId = data.getInt("group_id");
                    groupNameList.add(groupName);
                    groupIdList.add(groupId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spinnerHandler();

        }
    }

    private class GroupMemberReceiver extends AsyncTask<String,Void,String> {

        private static final String DEBUG_TAG = "GroupMemberReceiver";
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String jsonData = params[1];
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(uri);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; utf-8"); // 追記
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                // サーバーへ送るJSONをセットする
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(jsonData);
                wr.flush();
                wr.close();

                con.connect();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException("ステータスコード："+status);
                }
                is = con.getInputStream();
                result = GlobalUtils.getInstance().is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonObject = null;

            groupMemberList = new ArrayList<>();
            groupMemberIdList = new ArrayList<>();
            blackMemberList = new ArrayList<>();
            try {
                jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for(int i = 0 ; i < jsonArray.length() ; i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                    String memberName = data.getString("nickname");
                    int userId = data.getInt("group_member_id");
                    int blackRank = data.getInt("black_rank");
                    int colorCode = data.getInt("color_code");
                        BlackListBean blackListBean = new BlackListBean();
                        blackListBean.setNickName(memberName);
                        blackListBean.setUserId(userId);
                        blackListBean.setId(i);
                        blackListBean.setBlackRank(blackRank);
                        blackListBean.setMyId(userMemberId);
                        blackListBean.setGroupId(selectedGroupId);
                        blackListBean.setColorCode(colorCode);
                        blackMemberList.add(blackListBean);
                        groupMemberList.add(memberName);
                        groupIdList.add(userId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            swipeRecyclerViewHandler();
        }
    }


    private class GroupMemberPoster extends AsyncTask<String,Void,String> {

        private static final String DEBUG_TAG = "GroupMemberPoster";
        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            String jsonData = params[1];
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";

            try {
                URL url = new URL(uri);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; utf-8"); // 追記
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                // サーバーへ送るJSONをセットする
//                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                wr.writeBytes(jsonData);
//                wr.flush();
//                wr.close();
//
//                con.connect();

                OutputStream os = con.getOutputStream();
                os.write(jsonData.getBytes());
                os.flush();
                os.close();
                int status = con.getResponseCode();
                if(status != 200){
                    throw new IOException("ステータスコード："+status);
                }
                is = con.getInputStream();
                result = GlobalUtils.getInstance().is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStream解放失敗", ex);
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            String status = "";

            try {
                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                if(status.equals("ok")){
                    Toast.makeText(getApplicationContext(),"登録しました。",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"登録失敗しました。",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}