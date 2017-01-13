package fukuchi.junpou.Model;

public class DateDetailsContainer {

    private boolean mPaidHolidayFlag = false;
    private String mPaidTime = "0";
    private boolean mTransferWorkFlag = false;
    private boolean mFlexNotApplicable = false;
    private boolean mHolidayWorkFlag = false;
    private String mDate = "";

    public void setDate(String date) {
        mDate = date;
    }

    public void setPaidHolidayFlag(boolean flag) {
        mPaidHolidayFlag = flag;
    }

    public void setPaidTime(String paidTime) {
        mPaidTime = paidTime;
    }

    public void setTransferWorkFrag(boolean flag) {
        mTransferWorkFlag = flag;
    }

    public void setFlex(boolean flag) {
        mFlexNotApplicable = flag;
    }

    public void setHolidayWorkFlag(boolean flag) {
        mHolidayWorkFlag = flag;
    }

    public String getDate() {
        return mDate;
    }

    public boolean getPaidHolidayFrag() {
        return mPaidHolidayFlag;
    }

    public String getPaidTime() {
        return mPaidTime;
    }

    public boolean getTransferWorkFlag() {
        return mTransferWorkFlag;
    }

    public boolean getFlex() {
        return mFlexNotApplicable;
    }

    public boolean getHolidayWorkFlag() {
        return mHolidayWorkFlag;
    }
}
