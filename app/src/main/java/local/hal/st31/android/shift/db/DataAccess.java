package local.hal.st31.android.shift.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import local.hal.st31.android.shift.beans.SelfScheduleBean;

public class DataAccess {
    /**
     * selfSchedule の　INSERT
     * @param db selfSchedule　自分の予定を管理するDB
     * @param selfScheduleBean bean
     * @return  成功かどうか
     */
    public static long selfScheduleInsert(SQLiteDatabase db,SelfScheduleBean selfScheduleBean){
        String sql = "INSERT INTO selfSchedule(work,memo,start_time,end_time,date)VALUES(?,?,?,?,?)";
        SQLiteStatement stmt =db.compileStatement(sql);
        stmt.bindString(1,selfScheduleBean.getWork());
        stmt.bindString(2,selfScheduleBean.getMemo());
        stmt.bindString(3,selfScheduleBean.getStartTime());
        stmt.bindString(4,selfScheduleBean.getEndTime());
        stmt.bindString(5,selfScheduleBean.getDate());
        return stmt.executeInsert();
    }

    public static ArrayList<SelfScheduleBean> selfScheduleSelectByDate(SQLiteDatabase db,String date){
        ArrayList<SelfScheduleBean> list = new ArrayList<>();
        String sql = "SELECT * FROM selfSchedule WHERE date = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{date});
        if(cursor.moveToFirst()){
            do{
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String work = cursor.getString(cursor.getColumnIndex("work"));
                String memo = cursor.getString(cursor.getColumnIndex("memo"));
                String startTime = cursor.getString(cursor.getColumnIndex("start_time"));
                String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
                String workDate = cursor.getString(cursor.getColumnIndex("date"));

                SelfScheduleBean bean = new SelfScheduleBean();
                bean.setId(id);
                bean.setWork(work);
                bean.setMemo(memo);
                bean.setStartTime(startTime);
                bean.setEndTime(endTime);
                bean.setDate(date);
                list.add(bean);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static int selfScheduleDelete(SQLiteDatabase db,long id){
        String sql = "DELETE FROM selfSchedule WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1,id);
        return stmt.executeUpdateDelete();
    }

    public static int selfScheduleUpdate(SQLiteDatabase db,SelfScheduleBean selfScheduleBean,long id){
        String sql = "UPDATE selfSchedule SET work = ?, memo = ?,start_time = ?, end_time = ?, date = ?  WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1,selfScheduleBean.getWork());
        stmt.bindString(2,selfScheduleBean.getMemo());
        stmt.bindString(3,selfScheduleBean.getStartTime());
        stmt.bindString(4,selfScheduleBean.getEndTime());
        stmt.bindString(5,selfScheduleBean.getDate());
        stmt.bindLong(6,id);
        return stmt.executeUpdateDelete();
    }

}
