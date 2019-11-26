package local.hal.st31.android.shift.beans;

public class ShiftTypeBean {
    private int shiftId;
    private String beginTime;//09:30
    private String endTime;
    private String typeName;
    private String comment;

    public ShiftTypeBean(){

    }

    public ShiftTypeBean(int shiftId,String beginTime,String endTime,String typeName,String comment){
        this.shiftId = shiftId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.typeName = typeName;
        this.comment = comment;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
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
}
