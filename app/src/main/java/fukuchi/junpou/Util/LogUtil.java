package fukuchi.junpou.Util;

import android.os.Build;
import android.os.Debug;

public class LogUtil {

    private final static String TAG = "Junpou";

    //TODO

    public static void Log(String tag, String message) {
        android.util.Log.v(TAG, "[JunpouLog]" + " : " + message);
    }
}
