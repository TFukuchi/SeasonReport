package fukuchi.junpou.Model;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import ajd4jp.Month;
import fukuchi.junpou.Util.LogUtil;

public class CandoHoliday {

    private static final String TAG = CandoHoliday.class.getSimpleName();

    private Month mMonth = null;

    @IntDef({SUT_DAY, SUN_DAY})
    public @interface WEEK_END {
    }

    public static final int SUT_DAY = 1;
    public static final int SUN_DAY = 2;

    public CandoHoliday(int year, int month) {
        try {
            mMonth = new Month(year, month);
        } catch (AJDException e) {
            LogUtil.Log(TAG, e.getMessage());
        }
    }

    public List<AJD> getCandoHoliday() {
        List<AJD> candoHolidayList = new ArrayList<AJD>();

        if (mMonth == null) {
            return null;
        }

        for (AJD day : mMonth.getDays()) {
            int d = day.getDay();
            Holiday holiday = Holiday.getHoliday(day);

            String note = null;
            if (holiday != null) {
                note = holiday.getName(day);
                if (note != null) {
                    String week = day.getWeek().getJpName();
                    int val = 0;
                    android.util.Log.v("Fukuchi","day / " + day);
                    switch (week) {
                        case "土":
                            val = 1;
                            break;
                        case "日":
                            val = 2;
                            break;
                        default:
                            continue;
                    }
                    Date convertedDate = dateConverter(day, val);
                    AJD ajd = new AJD(convertedDate);
                    candoHolidayList.add(ajd);
                }

                android.util.Log.v("Fukuchi","list / " + candoHolidayList.size());
            }
        }
        return candoHolidayList;
    }

    public Date dateConverter(AJD date, int val) {
        int year = date.getYear();
        int month = date.getMonth() -1;
        int day = date.getDay();

        if (day > val) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day - val);

            return cal.getTime();
        } else {
            int diffDate = val - day;
            if (month == 0) {
                year--;
                month = 12;
                day = 31 - diffDate;
            } else {
                month--;
                switch (month) {
                    case 1:
                        day = 28 - diffDate;
                        break;
                    case 3:
                    case 5:
                    case 8:
                    case 10:
                        day = 30 - diffDate;
                        break;
                    default:
                        day = 31 - diffDate;
                        break;
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day - val);

            return cal.getTime();
        }
    }
}
