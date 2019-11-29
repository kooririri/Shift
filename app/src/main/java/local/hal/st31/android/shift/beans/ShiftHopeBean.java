package local.hal.st31.android.shift.beans;

public class ShiftHopeBean {
    private String shiftHopeId;//シフト希望ID
    private int shiftId;//シフト自体のID
    private int shiftTypeId;//シフトの番タイプID
    private String date;

    public ShiftHopeBean(String shiftHopeId){
        this.shiftHopeId = shiftHopeId;
    }

    public String getShiftHopeId() {
        return shiftHopeId;
    }

    public void setShiftHopeId(String shiftHopeId) {
        this.shiftHopeId = shiftHopeId;
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
}
