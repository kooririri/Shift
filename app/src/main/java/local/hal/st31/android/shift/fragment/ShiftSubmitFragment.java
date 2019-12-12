package local.hal.st31.android.shift.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.MainActivity;
import local.hal.st31.android.shift.R;
import local.hal.st31.android.shift.adapters.ShiftMonthListAdapter;
import local.hal.st31.android.shift.beans.ShiftRequestBean;
import local.hal.st31.android.shift.beans.ShiftTypeBean;
import local.hal.st31.android.shift.db.DataAccess;
import local.hal.st31.android.shift.db.DatabaseHelper;
import local.hal.st31.android.shift.utils.DateUtils;
import local.hal.st31.android.shift.utils.GlobalUtils;

public class ShiftSubmitFragment extends Fragment {
    private View fragmentView;

    private RecyclerView shiftListView;
    private ShiftMonthListAdapter shiftMonthListAdapter;
    private TextView dateLabel;
    private List<List<ShiftTypeBean>> dataList;
    private DatabaseHelper _helper;
    private SQLiteDatabase db;
    int shiftId = 0;
    int modifyVersion = 3;
//    List<List<ShiftTypeBean>> data;
    List<ShiftRequestBean> data;

//    private static final String URL = "http://shift_backend.test/shift";
    private static final String URL = "http://10.0.2.2/shift_backend/controllers/shift_controller.php";
//    private static final String URL = "http://10.0.2.2/test/index.php";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_shift_submit,container,false);
        shiftListView = fragmentView.findViewById(R.id.shiftListView);
        _helper = new DatabaseHelper(getContext());



        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();

        Log.e("kkk",dataList.toString());
        Log.e("kkk",getData().toString());
    }

    private void initView(){
        //次の月（シフト）の日数を取得
        ShiftTypeDataReceiver shiftTypeDataReceiver = new ShiftTypeDataReceiver();
        shiftTypeDataReceiver.execute(URL);
        shiftMonthListAdapter = new ShiftMonthListAdapter(getContext());
        dataList = getData();
        shiftMonthListAdapter.setList(dataList);
        shiftMonthListAdapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//縦並び
        shiftListView.setLayoutManager(layoutManager);
        shiftListView.setAdapter(shiftMonthListAdapter);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        dateLabel = fragmentView.findViewById(R.id.day_text);
        dateLabel.setText(cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月");
    }

    private class ShiftTypeDataReceiver extends AsyncTask<String,Void,String> {

        private static final String DEBUG_TAG = "ListReceiver";
        @Override
        protected String doInBackground(String... params) {
            String urlStr = params[0];
            HttpURLConnection con = null;
            InputStream is = null;
            String result = "";
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                con.setConnectTimeout(2000);
                is = con.getInputStream();

                result = GlobalUtils.getInstance().is2String(is);

            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG,"URL変換失敗",ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG,"通信失敗aaa",ex);
            }
            finally {
                if(con != null){
                    con.disconnect();
                }
                if(is!=null){
                    try{
                        is.close();
                    }
                    catch (IOException ex){
                        Log.e(DEBUG_TAG,"InputStream解放失敗",ex);
                    }
                }
            }
            return  result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                JSONArray jsonArray = jsonObject.getJSONArray("list");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH,+1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int days = DateUtils.getDaysByYearMonth(year,month);
                db = _helper.getWritableDatabase();

                //shift_idとmodify_versionを取得(一意)
                List<ShiftTypeBean> list = new ArrayList<>();
                int numOfCol = 0;
                for(int num1 = 0;num1<jsonArray.length();num1++){
                    JSONObject dataObject = jsonArray.getJSONObject(num1);
                    shiftId = dataObject.getInt("shift_id");
                    modifyVersion = dataObject.getInt("modify_version");
                }
                //SharedPreferencesの中に保存したshift_idとmodify_versionを取り出す
                SharedPreferences ps = PreferenceManager.getDefaultSharedPreferences(getContext());
                int savedShiftId = ps.getInt("shiftId",0);
                int savedModifyVersion = ps.getInt("modifyVersion",0);

                //同じ場合、変更なし、テーブル更新なし　同じではない場合テーブル更新
                //TODO　サーバ側shift_typeテーブルフィールド変更した場合、こっちも変更
//                int num = 0;
                if(shiftId != savedShiftId||modifyVersion != savedModifyVersion){
                    ps.edit().putInt("shiftId",shiftId).putInt("modifyVersion",modifyVersion).apply();
                    DataAccess.deleteAllShiftType(db);
                    //既存table删除？？？？？
                    for(int num2 = 0;num2<jsonArray.length();num2++){
                        JSONObject dataObject = jsonArray.getJSONObject(num2);
                        ShiftTypeBean bean = new ShiftTypeBean();
                        bean.setShiftTypeId(dataObject.getInt("type_id"));
                        bean.setShiftId(dataObject.getInt("shift_id"));
                        bean.setBeginTime(dataObject.getString("begin_time"));
                        bean.setEndTime(dataObject.getString("end_time"));
                        bean.setTypeName(dataObject.getString("type_name"));
                        bean.setComment(dataObject.getString("comment"));
                        list.add(bean);
                        DataAccess.shiftTypeInsert(db,bean);
//                        num++;
                    }
                    for(int i = 1;i<=days;i++){
                        ShiftRequestBean shiftRequestBean = new ShiftRequestBean();
                        String date = year + "-" + month + "-" + i;
                        if(i < 10){
                            date = year + "-" + month + "-" + "0" + i;
                        }
                        for(int j=0;j<list.size();j++){
                            int tempShiftId = list.get(j).getShiftId();
                            shiftRequestBean.setShiftId(tempShiftId);
                            int tempTypeId = list.get(j).getShiftTypeId();
                            shiftRequestBean.setShiftTypeId(tempTypeId);
                            shiftRequestBean.setDate(date);
                            shiftRequestBean.setSelectedFlag(0);
                            DataAccess.shiftRequestInsert(db,shiftRequestBean);
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private  List<List<ShiftTypeBean>> getData(){
        db = _helper.getWritableDatabase();
        SharedPreferences ps = PreferenceManager.getDefaultSharedPreferences(getContext());
        int shiftId = ps.getInt("shiftId",0);
        Log.e("asdd",shiftId+"");
        List<List<ShiftTypeBean>> data = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,+1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int days = DateUtils.getDaysByYearMonth(year,month);
        List<ShiftTypeBean> list;
        list = DataAccess.getAllShiftTypeByShiftId(db,shiftId);
        int num = DataAccess.getShiftTypeNum(db,shiftId);
        Log.e("asdd",num+"");
        for(int i = 1;i<=days;i++){
            List<ShiftTypeBean> a =new ArrayList<>();

            for(int j = 1 ;j<=num;j++){
                ShiftTypeBean bean = new ShiftTypeBean();
                ShiftTypeBean test = DataAccess.getOneShiftTypeBean(db,j);
                bean.setTypeName(test.getTypeName());
                bean.setComment(test.getComment());
                bean.setEndTime(test.getEndTime());
                bean.setBeginTime(test.getBeginTime());
                bean.setShiftTypeId(test.getShiftTypeId());
                bean.setShiftId(test.getShiftId());
                a.add(bean);
            }




            data.add(a);

        }

        return data;
    }


//    private  List<ShiftRequestBean> getData(){
//        db = _helper.getWritableDatabase();
//        SharedPreferences ps = PreferenceManager.getDefaultSharedPreferences(getContext());
//        int shiftId = ps.getInt("shiftId",0);
//        List<ShiftRequestBean> data = new ArrayList<>();
//        data = DataAccess.getShiftRequestByShiftId(db,shiftId);
//
//        return data;
//    }

    private  List<List<ShiftTypeBean>> getTestData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+2;
        int days = DateUtils.getDaysByYearMonth(year,month);

        List<List<ShiftTypeBean>> data = new ArrayList<>();

        for(int i = 1;i<=days;i++){
            List<ShiftTypeBean> list = new ArrayList<>();
            ShiftTypeBean bean = new ShiftTypeBean();
            bean.setShiftId(1);
            bean.setShiftTypeId(1);
            bean.setBeginTime("10:00");
            bean.setEndTime("19:00");
            bean.setTypeName("A勤");
            bean.setComment("休憩1時間");
            list.add(bean);
            bean = new ShiftTypeBean();
            bean.setShiftId(2);
            bean.setShiftTypeId(2);
            bean.setBeginTime("12:00");
            bean.setEndTime("21:00");
            bean.setTypeName("B勤");
            bean.setComment("休憩1時間");
            list.add(bean);
            bean = new ShiftTypeBean();
            bean.setShiftId(3);
            bean.setShiftTypeId(3);
            bean.setBeginTime("12:00");
            bean.setEndTime("21:00");
            bean.setTypeName("C勤");
            bean.setComment("休憩1時間");
            list.add(bean);
//            bean = new ShiftTypeBean();
//            bean.setShiftId(4);
//            bean.setShiftTypeId(4);
//            bean.setBeginTime("12:00");
//            bean.setEndTime("21:00");
//            bean.setTypeName("D勤");
//            bean.setComment("休憩1時間");
//            list.add(bean);
//            bean = new ShiftTypeBean();
//            bean.setShiftId(5);
//            bean.setShiftTypeId(5);
//            bean.setBeginTime("12:00");
//            bean.setEndTime("21:00");
//            bean.setTypeName("E勤");
//            bean.setComment("休憩1時間");
//            list.add(bean);

            data.add(list);
        }

        return data;
    }

}
