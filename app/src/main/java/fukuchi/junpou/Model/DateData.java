package fukuchi.junpou.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import ajd4jp.Week;
import fukuchi.junpou.Util.PublicVariable;

public class DateData {

    private static final String SUNDAY_SHORT_NAME = "Sun";
    private static final String SATURDAY_SHORT_NAME = "Sat";

    private final String mDay;
    private final String mDayOfWeek;
    private String mHolidayText;

    private int mVisibility = View.VISIBLE;

    private int mColor = Color.BLACK;

    private String mPlanAttend;
    private String mPlanLeave;
    private String mPlanBreakTime;
    private String mRealAttend;
    private String mRealLeave;
    private String mRealBreakTime;
    private String mDeepNightBreakTime;
    private String mWorkContent;

    // this param is Database key
    private final String mId;
    private final JunpouValues mValues;

    private boolean mIsHoliday = false;

    private DateDetailsContainer mDetailContainer;

    public DateData(String day, Week dayOfWeek,
                    String holiday, Context context, String id, JunpouValues junpouValues,
                    DateDetailsContainer container) {
        mId = id;
        mDay = day;
        mValues = junpouValues;

        if (dayOfWeek != null) {
            mDayOfWeek = dayOfWeek.getJpName();

            String shortName = dayOfWeek.getShortName();
            mHolidayText = holiday;
            if (shortName.equals(SATURDAY_SHORT_NAME)) {
                mVisibility = View.GONE;
                mColor = Color.BLUE;
                mIsHoliday = true;
            } else if (shortName.equals(SUNDAY_SHORT_NAME)) {
                mVisibility = View.GONE;
                mColor = Color.RED;
                mIsHoliday = true;
            } else if (this.mHolidayText != null && !this.mHolidayText.isEmpty()) {
                mVisibility = View.GONE;
                mColor = Color.MAGENTA;
                mIsHoliday = true;
            }
        } else {
            this.mDayOfWeek = null;
            this.mHolidayText = null;
        }

        if (context != null) {
            SharedPreferences pref = context.getApplicationContext()
                    .getSharedPreferences(PublicVariable.USER_DATA_PREFERENCE, Context.MODE_PRIVATE);

            mPlanAttend = pref.getString(PublicVariable.KEY_BASE_ATTENDING, null);
            mPlanLeave = pref.getString(PublicVariable.KEY_BASE_LEAVING_OFFICE, null);
            mPlanBreakTime = pref.getString(PublicVariable.KEY_BASE_BREAK_TIME, null);
        }

        mDetailContainer = container;
    }

    public void setPlanAttend(@Nullable String val) {
        android.util.Log.v("Fukuchi", "setPlanAttend : " + val);
        mPlanAttend = val != null ? val : "";
    }

    public void setPlanLeave(@Nullable String val) {
        mPlanLeave = val != null ? val : "";
    }

    public void setPlanBreakTime(@Nullable String val) {
        mPlanBreakTime = val != null ? val : "";
    }

    public void setRealAttend(@Nullable String val) {
        mRealAttend = val != null ? val : "";
    }

    public void setRealLeave(@Nullable String val) {
        mRealLeave = val != null ? val : "";
    }

    public void setRealBreakTime(@Nullable String val) {
        mRealBreakTime = val != null ? val : "";
    }

    public void setDeepNightBreakTime(@Nullable String val) {
        mDeepNightBreakTime = val != null ? val : "";
    }

    public void setHolidayFlag(@Nullable String val) {
        mHolidayText = val != null ? val : "";
    }

    public void setWorkContent(@Nullable String val) {
        mWorkContent = val != null ? val : "";
    }

    public void setDateDetailContainer(@Nullable DateDetailsContainer val) {
        if (val != null) {
            mDetailContainer = val;
        }
    }

    @Nullable
    public String getDay() {
        return mDay;
    }

    @Nullable
    public String getId() {
        return mId;
    }

    @Nullable
    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    @Nullable
    public String getHolidayText() {
        return mHolidayText;
    }

    @Nullable
    public int getVisibility() {
        return mVisibility;
    }

    @Nullable
    public int getColor() {
        return mColor;
    }

    @Nullable
    public String getPlanAttend() {
        return mPlanAttend;
    }

    @Nullable
    public String getPlanLeave() {
        return mPlanLeave;
    }

    @Nullable
    public String getPlanBreakTime() {
        return mPlanBreakTime;
    }

    @Nullable
    public String getRealAttend() {
        return mRealAttend;
    }

    @Nullable
    public String getRealLeave() {
        return mRealLeave;
    }

    @Nullable
    public String getRealBreakTime() {
        return mRealBreakTime;
    }

    @Nullable
    public String getDeepNightBreakTime() {
        return mDeepNightBreakTime;
    }

    @Nullable
    public String getWorkContent() {
        return mWorkContent;
    }

    @Nullable
    public boolean isHoliday() {
        return mIsHoliday;
    }

    @Nullable
    public JunpouValues getJunpouValues() {
        return mValues;
    }

    @Nullable
    public DateDetailsContainer getDetailContainer() {
        return mDetailContainer;
    }
}
