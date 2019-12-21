package local.hal.st31.android.shift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import local.hal.st31.android.shift.utils.GlobalUtils;

public class BlackListActivity extends AppCompatActivity {

    private static final String GROUP_URL = "http://10.0.2.2/shift_app_backend/controllers/group_controller.php";

    private int userMemberId;
    private List<String> groupNameList;
    private String selectedGroup;
    private List<Integer> groupIdList;
    private int selectedGroupId;
    private List<String> groupMemberList;
    private List<Integer> groupMemberIdList;
    private SwipeRecyclerView swipeRecyclerView;
    private List<BlackListBean> blackMemberList;
    GroupMemberAdapter groupMemberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        groupNameList = new ArrayList<>();
        groupIdList = new ArrayList<>();


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
        niceSpinner.attachDataSource(groupNameList);
        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                selectedGroup = parent.getItemAtPosition(position).toString();
                selectedGroupId = groupIdList.get(position);
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",selectedGroupId);
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
        swipeRecyclerView = findViewById(R.id.memberRecyclerView);
        groupMemberAdapter = new GroupMemberAdapter(getApplicationContext());
        groupMemberAdapter.setData(blackMemberList);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        swipeRecyclerView.setLayoutManager(layoutManager);
        swipeRecyclerView.setAdapter(groupMemberAdapter);
        groupMemberAdapter.setListener(new GroupMemberAdapter.onMemberClickListener() {
            @Override
            public void onItemClick(int position, BlackListBean blackListBean) {
                if (blackListBean.getBlackRank() == 0) {
                    blackListBean.setBlackRank(1);
                } else {
                    blackListBean.setBlackRank(0);

                }
            }
        });
    }

    public void sendButtonClick(View view){
        Map<String,Object> map = new HashMap<>();
        map.put("list",blackMemberList);
        map.put("postNo",3);
        map.put("userId",userMemberId);
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
        String submitJson = gson.toJson(map);
//        GroupMemberPoster groupMemberPoster = new GroupMemberPoster();
//        groupMemberPoster.execute(GROUP_URL,submitJson);
        Log.e("paku",submitJson);

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

        private static final String DEBUG_TAG = "GroupMenberReceiver";
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
                    int userId = data.getInt("user_id");
                    if(userId != userMemberId){
                        BlackListBean blackListBean = new BlackListBean();
                        blackListBean.setNickName(memberName);
                        blackListBean.setUserId(userId);
                        blackMemberList.add(blackListBean);
                        groupMemberList.add(memberName);
                        groupIdList.add(userId);
                    }
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

        }
    }
}
