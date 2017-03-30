package fukuchi.junpou.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesHelper {

    private static final String FIRST_LAUNCH = "first_launch";

    private final Context mContext;
    private final SharedPreferences mPreferences;

    public SharedPreferencesHelper(@NonNull Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
    }

    public void setFirstLaunch(boolean isLaunched) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(FIRST_LAUNCH, isLaunched);
        editor.commit();
    }

    public boolean getFirstLaunch() {
        return mPreferences.getBoolean(FIRST_LAUNCH, true);
    }
}
