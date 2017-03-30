package fukuchi.junpou.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fukuchi.junpou.Util.PublicVariable;

import static fukuchi.junpou.Util.PublicVariable.KEY_BASE_ATTENDING;
import static fukuchi.junpou.Util.PublicVariable.KEY_BASE_BREAK_TIME;
import static fukuchi.junpou.Util.PublicVariable.KEY_BASE_LEAVING_OFFICE;
import static fukuchi.junpou.Util.PublicVariable.KEY_DATE_CONVERSION;
import static fukuchi.junpou.Util.PublicVariable.KEY_PAID_HOLIDAY_COUNT;
import static fukuchi.junpou.Util.PublicVariable.KEY_PAID_TIME_COUNT;
import static fukuchi.junpou.Util.PublicVariable.KEY_USER_ID;
import static fukuchi.junpou.Util.PublicVariable.KEY_USER_NAME;
import static fukuchi.junpou.Util.PublicVariable.KEY_WORK_CODE;
import static fukuchi.junpou.Util.PublicVariable.KEY_WORK_LOCATION;
import static fukuchi.junpou.Util.PublicVariable.USER_DATA_PREFERENCE;

public class BasicInfoData {

    private final String mName;
    private final String mUserId;
    private final String mWorkCode;
    private final String mWorkLocation;
    private final String mBaseAttendingTime;
    private final String mBaseLeavingOfficeTime;
    private final String mBaseBreakTime;
    private final String mPaidHolidayCount;
    private final String mPaidTimeCount;
    private final String mDateConversion;

    public BasicInfoData(@NonNull Context context) {
        SharedPreferences pref = context
                .getSharedPreferences(USER_DATA_PREFERENCE, Context.MODE_PRIVATE);
        mName = pref.getString(KEY_USER_NAME, null);
        mUserId = pref.getString(KEY_USER_ID, null);
        mWorkCode = pref.getString(KEY_WORK_CODE, null);
        mWorkLocation = pref.getString(KEY_WORK_LOCATION, null);
        mBaseAttendingTime = pref.getString(KEY_BASE_ATTENDING, null);
        mBaseLeavingOfficeTime = pref.getString(KEY_BASE_LEAVING_OFFICE, null);
        mBaseBreakTime = pref.getString(KEY_BASE_BREAK_TIME, null);
        mPaidHolidayCount = pref.getString(KEY_PAID_HOLIDAY_COUNT, null);
        mPaidTimeCount = pref.getString(KEY_PAID_TIME_COUNT, null);
        mDateConversion = pref.getString(KEY_DATE_CONVERSION, null);
    }

    @Nullable
    public String getName() {
        return mName;
    }

    @Nullable
    public String getUserId() {
        return mUserId;
    }

    @Nullable
    public String getWorkCode() {
        return mWorkCode;
    }

    @Nullable
    public String getWorkLocation() {
        return mWorkLocation;
    }

    @Nullable
    public String getBaseAttendingTime() {
        return mBaseAttendingTime;
    }

    @Nullable
    public String getBaseLeavingOfficeTime() {
        return mBaseLeavingOfficeTime;
    }

    @Nullable
    public String getBaseBreakTime() {
        return mBaseBreakTime;
    }

    @Nullable
    public String getPaidHolidayCount() {
        return mPaidHolidayCount;
    }

    @Nullable
    public String getPaidTimeCount() {
        return mPaidTimeCount;
    }

    @Nullable
    public String getDateConversion() {
        return mDateConversion;
    }
}
