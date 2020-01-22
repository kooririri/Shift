package local.hal.st31.android.shift.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import local.hal.st31.android.shift.beans.BlackListBean;
import local.hal.st31.android.shift.beans.SelfScheduleBean;
import local.hal.st31.android.shift.beans.ShiftRequestBean;
import local.hal.st31.android.shift.beans.ShiftTypeBean;

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

    public static LinkedList<String> getAvailableDate(SQLiteDatabase db){
        LinkedList<String> dates = new LinkedList<>();
        String sql = "SELECT DISTINCT date FROM selfSchedule";
        Cursor cursor = db.rawQuery(sql,new String[]{});
        if(cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                if(!dates.contains(date)){
                    dates.add(date);
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }

    public static void shiftTypeReplace(SQLiteDatabase db, ShiftTypeBean bean){
        ContentValues values = new ContentValues();
        values.put("shift_type_id",bean.getShiftTypeId());
        values.put("shift_id",bean.getShiftId());
        values.put("begin_time",bean.getBeginTime());
        values.put("end_time",bean.getEndTime());
        values.put("type_name",bean.getTypeName());
        values.put("comment",bean.getComment());
        values.put("selected_flag",bean.getSelectedFlag());
        db.replace("shiftType",null,values);
        values.clear();
    }

    public static void deleteAllShiftType(SQLiteDatabase db){
        db.delete("shiftType","shift_id > ?",new String[]{"0"});
    }

    public static int getShiftTypeNum(SQLiteDatabase db,int shiftId){
        int num = 0;
        String sql = "SELECT COUNT(shift_type_id) AS num FROM shiftType WHERE shift_id = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(shiftId)});
        if(cursor.moveToFirst()){
            do{
                num = cursor.getInt(cursor.getColumnIndex("num"));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return num;
    }

    public static List<ShiftTypeBean>  getAllShiftTypeByShiftId(SQLiteDatabase db, int shiftId){
        List<ShiftTypeBean> list = new ArrayList<>();
        String sql = "SELECT * FROM shiftType WHERE shift_id = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(shiftId)});
        if(cursor.moveToFirst()){
            do{
                int shiftTypeId = cursor.getInt(cursor.getColumnIndex("shift_type_id"));
                String beginTime = cursor.getString(cursor.getColumnIndex("begin_time"));
                String endTime = cursor.getString(cursor.getColumnIndex("end_time"));
                String typeName = cursor.getString(cursor.getColumnIndex("type_name"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                int selectedFlag = cursor.getInt(cursor.getColumnIndex("selected_flag"));
                ShiftTypeBean bean = new ShiftTypeBean();
                bean.setShiftId(shiftId);
                bean.setShiftTypeId(shiftTypeId);
                bean.setBeginTime(beginTime);
                bean.setEndTime(endTime);
                bean.setComment(comment);
                bean.setTypeName(typeName);
                bean.setSelectedFlag(selectedFlag);
                list.add(bean);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public static void shiftRequestReplace(SQLiteDatabase db, ShiftRequestBean bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.getId());
        values.put("shift_id",bean.getShiftId());
        values.put("date",bean.getDate());
        values.put("shift_type_id",bean.getShiftTypeId());
        values.put("selected_flag",bean.getSelectedFlag());
        values.put("kaburu_flag",bean.getKaburuFlag());
        db.replace("shiftRequest",null,values);
        values.clear();
    }


    public static List<ShiftRequestBean> getShiftRequestByShiftId(SQLiteDatabase db,int shiftId,int selectedFlag){
        List<ShiftRequestBean> list = new ArrayList<>();
        String sql = "SELECT * FROM shiftRequest WHERE shift_id = ? AND selected_flag = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(shiftId),String.valueOf(selectedFlag)});
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndex("id"));
                int shiftID = cursor.getInt(cursor.getColumnIndex("shift_id"));
                int shiftTypeId = cursor.getInt(cursor.getColumnIndex("shift_type_id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                ShiftRequestBean bean = new ShiftRequestBean();
                bean.setId(id);
                bean.setShiftTypeId(shiftTypeId);
                bean.setShiftId(shiftID);
                bean.setDate(date);
                bean.setSelectedFlag(selectedFlag);
                list.add(bean);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    
}
