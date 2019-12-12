package local.hal.st31.android.shift.beans;

import android.support.annotation.NonNull;

public class ShiftRequestBean {

    private int id;//シフト希望ID
    private int shiftId;
    private int shiftTypeId;//シフトの番タイプID
    private String date;
    private String beginTime;//09:30　開始時間
    private String endTime;//終了時間
    private String typeName;//シフト番名 例:A勤
    private String comment;//説明とかメモ
    private int selectedFlag;

    public ShiftRequestBean(){

    }

    @NonNull
    @Override
    public String toString() {
        return this.beginTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getShiftTypeId() {
        return shiftTypeId;
    }

    public void setShiftTypeId(int shiftTypeId) {
        this.shiftTypeId = shiftTypeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSelectedFlag() {
        return selectedFlag;
    }

    public void setSelectedFlag(int selectedFlag) {
        this.selectedFlag = selectedFlag;
    }
}
