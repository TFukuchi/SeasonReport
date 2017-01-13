package fukuchi.junpou.Model;

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

    Month mMonth = null;

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
                if (note.equals("振替休日")) {
                    Date convertedDate = dateConverter(day.toDate());
                    if (convertedDate != null) {
                        AJD ajd = new AJD(convertedDate);
                        candoHolidayList.add(new AJD(convertedDate));
                    }
                }
            }
        }

        return candoHolidayList;
    }

    //TODO: ここらへんの処理微妙
    //TODO: 振替休日が(基本)月曜日とかに充てられるので、それを金曜日に変えてる。どうすんべ。
    //TODO: どう考えてもError発生する。
    public Date dateConverter(Date targetDate) {
        long time = targetDate.getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(targetDate);

        cal.set(Calendar.DATE, targetDate.getDate() - 3);

        return cal.getTime();
    }
}
