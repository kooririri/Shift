package local.hal.st31.android.shift.beans;

import android.support.annotation.NonNull;

public class ShiftRequestBean {

    private int id;//シフト希望ID
    private int shiftId;
    private int shiftTypeId;//シフトの番タイプID
    private String date;
    private int selectedFlag;

    public ShiftRequestBean(){

    }

    @NonNull
    @Override
    public String toString() {
        return this.date;
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
