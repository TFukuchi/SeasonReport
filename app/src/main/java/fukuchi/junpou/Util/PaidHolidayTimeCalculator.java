package fukuchi.junpou.Util;

import android.content.Context;
import android.content.SharedPreferences;

import fukuchi.junpou.Model.BasicInfoData;

import static fukuchi.junpou.Util.PublicVariable.KEY_DATE_CONVERSION;
import static fukuchi.junpou.Util.PublicVariable.KEY_PAID_HOLIDAY_COUNT;

public class PaidHolidayTimeCalculator {

    private static final int MAX_PAID_DATE = 5;
    private static final int ONE_DAY_WORK_TIME = 8;

    public static void calculate(Context context) {
        BasicInfoData infoData = new BasicInfoData(context);

        // 残り9日
        String day = infoData.getPaidHolidayCount();
        // 残り38h
        String time = infoData.getPaidTimeCount();

        if (day == null || day.isEmpty()) {
            return;
        } else if (time == null || time.isEmpty()) {
            return;
        }

        //日付換算
        // 4 = 38 / 8
        int hidukekannsan = Integer.valueOf(time) / ONE_DAY_WORK_TIME;
        // 以前の計算分を取得
        // 5日
        String dateConversion = infoData.getDateConversion();
        int conversion = 5;
        if (dateConversion != null && !dateConversion.isEmpty()) {
            conversion = Integer.valueOf(dateConversion);
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(
                PublicVariable.USER_DATA_PREFERENCE, Context.MODE_PRIVATE).edit();

        int currentConversion = 0;
        // 5 == 4
        //前回の計算結果 != 今回の計算結果
        if (conversion != hidukekannsan){
            currentConversion = conversion - hidukekannsan;
        }
        editor.putString(KEY_PAID_HOLIDAY_COUNT, String.valueOf(Integer.valueOf(
                infoData.getPaidHolidayCount()) - currentConversion));
        editor.putString(KEY_DATE_CONVERSION, String.valueOf(hidukekannsan));

        editor.apply();
    }
}
