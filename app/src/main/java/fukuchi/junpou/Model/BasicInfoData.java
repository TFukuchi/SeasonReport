package fukuchi.junpou.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import fukuchi.junpou.Util.PublicVariable;

public class BasicInfoData {

    private final String mName;
    private final String mUserId;
    private final String mWorkCode;
    private final String mWorkLocation;
    private final String mBaseAttendingTime;
    private final String mBaseLeavingOfficeTime;
    private final String mBaseBreakTime;

    public BasicInfoData(@NonNull Context context) {
        SharedPreferences pref = context
                .getSharedPreferences(PublicVariable.USER_DATA_PREFERENCE, Context.MODE_PRIVATE);
        mName = pref.getString(PublicVariable.KEY_USER_NAME, null);
        mUserId = pref.getString(PublicVariable.KEY_USER_ID, null);
        mWorkCode = pref.getString(PublicVariable.KEY_WORK_CODE, null);
        mWorkLocation = pref.getString(PublicVariable.KEY_WORK_LOCATION, null);
        mBaseAttendingTime = pref.getString(PublicVariable.KEY_BASE_ATTENDING, null);
        mBaseLeavingOfficeTime = pref.getString(PublicVariable.KEY_BASE_LEAVING_OFFICE, null);
        mBaseBreakTime = pref.getString(PublicVariable.KEY_BASE_BREAK_TIME, null);
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
}
