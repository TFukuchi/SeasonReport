package fukuchi.junpou.DataBase;

import android.content.Context;
import android.content.SharedPreferences;

import fukuchi.junpou.Util.PublicVariable;

public class DataContainer {

    private final String mDate;
    private final String mPlanAttend;
    private final String mPlanLeaves;
    private final String mPlanBreakTime;
    private final String mRealAttend;
    private final String mRealLeaves;
    private final String mRealBreakTime;
    private final String mDeepNightBreakTime;
    private final String mHolidayFlag;
    private final String mWorkContent;

    public DataContainer(Context context, String date, String planAttend, String planLeaves, String planBreakTime,
                         String realAttend, String realLeaves, String realBreakTime,
                         String deepNightBreakTime, String holidayFlag, String workContent) {
        SharedPreferences pref = context
                .getSharedPreferences(PublicVariable.USER_DATA_PREFERENCE, Context.MODE_PRIVATE);

        mDate = date;
        if (planAttend == null || planAttend.isEmpty()) {
            mPlanAttend = pref.getString(PublicVariable.KEY_BASE_ATTENDING, null);
        } else {
            mPlanAttend = planAttend;
        }
        if (planLeaves == null || planLeaves.isEmpty()) {
            mPlanLeaves = pref.getString(PublicVariable.KEY_BASE_LEAVING_OFFICE, null);
        } else {
            mPlanLeaves = planLeaves;
        }
        if (planBreakTime == null || planBreakTime.isEmpty()) {
            mPlanBreakTime = pref.getString(PublicVariable.KEY_BASE_BREAK_TIME, null);
        } else {
            mPlanBreakTime = planBreakTime;
        }
        mRealAttend = realAttend;
        mRealLeaves = realLeaves;
        mRealBreakTime = realBreakTime;
        mDeepNightBreakTime = deepNightBreakTime;
        mHolidayFlag = holidayFlag;
        mWorkContent = workContent;
    }

    public String getDate() {
        return mDate;
    }

    public String getPlanAttend() {
        return mPlanAttend;
    }

    public String getPlanLeaves() {
        return mPlanLeaves;
    }

    public String getHolidayFlag() {
        return mHolidayFlag;
    }

    public String getRealAttend() {
        return mRealAttend;
    }

    public String getRealLeaves() {
        return mRealLeaves;
    }

    public String getRealBreakTime() {
        return mRealBreakTime;
    }

    public String getDeepNightBreakTime() {
        return mDeepNightBreakTime;
    }

    public String getPlanBreakTime() {
        return mPlanBreakTime;
    }

    public String getWorkContent() {
        return mWorkContent;
    }
}
