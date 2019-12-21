package local.hal.st31.android.shift.beans;

public class BlackListBean {
    private int userId;
    private String nickName;
    private int blackRank;
    private String colorCode;

    public BlackListBean(){
        blackRank = 0;
    }

    @Override
    public String toString() {
        return "BlackListBean{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", blankRank=" + blackRank +
                ", colorCode='" + colorCode + '\'' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getBlackRank() {
        return blackRank;
    }

    public void setBlackRank(int blackRank) {
        this.blackRank = blackRank;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
