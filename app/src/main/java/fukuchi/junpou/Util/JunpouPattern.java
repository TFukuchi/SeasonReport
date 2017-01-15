package fukuchi.junpou.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import ajd4jp.Month;
import fukuchi.junpou.DataBase.DataContainer;
import fukuchi.junpou.DataBase.DataDetailDbWriteHelper;
import fukuchi.junpou.DataBase.InputValueDbWriteHelper;
import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Model.DateDetailsContainer;

import static fukuchi.junpou.Util.JunpouUtil.createId;

public class JunpouPattern {

    @IntDef({FIRST_SEASON_NUM, SECOND_SEASON_NUM, THIRD_SEASON_NUM})
    public @interface SeasonNumber {
    }

    public static final int FIRST_SEASON_NUM = 1;
    public static final int SECOND_SEASON_NUM = 2;
    public static final int THIRD_SEASON_NUM = 3;

    private static final List<Integer> FIRST_SEASON_LIST;
    private static final List<Integer> SECOND_SEASON_LIST;
    private static final List<Integer> THIRD_SEASON_LIST;

    private final Context mContext;

    // static initializer
    static {
        final int[] FIRST_SEASON = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final int[] SECOND_SEASON = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        final int[] THIRD_SEASON = {21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        //initialize FIRST_SEASON
        FIRST_SEASON_LIST = new ArrayList<>();
        for (int i : FIRST_SEASON) {
            FIRST_SEASON_LIST.add(i);
        }

        //initialize SECOND_SEASON
        SECOND_SEASON_LIST = new ArrayList<>();
        for (int i : SECOND_SEASON) {
            SECOND_SEASON_LIST.add(i);
        }

        //initialize THIRD_SEASON
        THIRD_SEASON_LIST = new ArrayList<>();
        for (int i : THIRD_SEASON) {
            THIRD_SEASON_LIST.add(i);
        }
    }

    public interface OnDataQueryListener {
        void onDataQueryFinished(List<DateData> data);
    }

    public JunpouPattern(Context context) {
        mContext = context;
    }

    private List<Integer> getSeasonListFromDay(Calendar currentCal) {
        int day = currentCal.get(Calendar.DAY_OF_MONTH);

        if (FIRST_SEASON_LIST.contains(day)) {
            return FIRST_SEASON_LIST;
        } else if (SECOND_SEASON_LIST.contains(day)) {
            return SECOND_SEASON_LIST;
        } else if (THIRD_SEASON_LIST.contains(day)) {
            return THIRD_SEASON_LIST;
        }

        return FIRST_SEASON_LIST;
    }

    public List<Integer> getDayListFromSeason(@SeasonNumber int seasonNum) {
        switch (seasonNum) {
            case FIRST_SEASON_NUM:
                return FIRST_SEASON_LIST;
            case SECOND_SEASON_NUM:
                return SECOND_SEASON_LIST;
            case THIRD_SEASON_NUM:
                return THIRD_SEASON_LIST;
            default:
                return FIRST_SEASON_LIST;
        }
    }

    @SeasonNumber
    public int convertSeasonInt(int seasonNum) {
        switch (seasonNum) {
            case 1:
                return FIRST_SEASON_NUM;
            case 2:
                return SECOND_SEASON_NUM;
            case 3:
                return THIRD_SEASON_NUM;
            default:
                return FIRST_SEASON_NUM;
        }
    }

    @SeasonNumber
    public int getSeasonInteger(int day) {
        if (FIRST_SEASON_LIST.contains(day)) {
            return FIRST_SEASON_NUM;
        } else if (SECOND_SEASON_LIST.contains(day)) {
            return SECOND_SEASON_NUM;
        } else if (THIRD_SEASON_LIST.contains(day)) {
            return THIRD_SEASON_NUM;
        }

        return FIRST_SEASON_NUM;
    }

    public void getDateData(Context context, OnDataQueryListener listener) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        List<Integer> currentSeason = getSeasonListFromDay(cal);
        getDateData(context, year, month, currentSeason, listener);
    }

    public void getDateData(Context context, int year, int month,
                            @NonNull List<Integer> targetList,
                            OnDataQueryListener listener) {

        Month mon = null;
        try {
            mon = new Month(year, month);
        } catch (AJDException e) {
            e.printStackTrace();
        }

        CandoHoliday candoHolidayContainer = new CandoHoliday(year, month);
        List<AJD> candoHolidayList = candoHolidayContainer.getCandoHoliday();

        List<DateData> dateDataList = new ArrayList<>();

        for (AJD ajd : mon.getDays()) {
            JunpouValues junpouValues = new JunpouValues(ajd.getYear(), ajd.getMonth(),
                    getSeasonInteger(targetList.get(0)), mContext);
            int day = ajd.getDay();

            if (targetList.contains(day)) {
                Holiday holiday = Holiday.getHoliday(ajd);
                String note = null;
                if (holiday != null) {
                    note = holiday.getName(ajd);
                }
                for (AJD candoHoliday : candoHolidayList) {
                    if (JunpouUtil.isSameDayAJDChecker(ajd, candoHoliday)) {
                        note = "Cando休日";
                    }
                }

                dateDataList.add(new DateData(ajd.getMonth() + "/" + day, ajd.getWeek(),
                        note, context, createId(year, month, day), junpouValues,
                        new DateDetailsContainer()));
            }
        }

        DateDataQueryTask queryTask = new DateDataQueryTask(context, dateDataList, listener);
        queryTask.execute();
    }

    private class DateDataQueryTask extends AsyncTask<Void, Void, List<DateData>> {

        private final Context mContext;
        private final List<DateData> mDateDataList;
        private final OnDataQueryListener mListener;

        DateDataQueryTask(Context context, List<DateData> dateDataList, OnDataQueryListener listener) {
            mContext = context;
            mDateDataList = dateDataList;
            mListener = listener;
        }

        @Override
        protected List<DateData> doInBackground(Void... params) {
            InputValueDbWriteHelper writer = new InputValueDbWriteHelper(mContext);
            List<String> dateList = new ArrayList<>();
            for (DateData dateData : mDateDataList) {
                dateList.add(dateData.getId());
            }
            List<DataContainer> entities = writer.dateDataQuery(dateList);

            DataDetailDbWriteHelper detailWriter = new DataDetailDbWriteHelper(mContext);
            List<DateDetailsContainer> detailEntities = detailWriter.dataDetailQuery(dateList);

            for (DataContainer entity : entities) {
                for (DateData dateData : mDateDataList) {
                    if (entity.getDate().equals(dateData.getDay())) {
                        dateData.setPlanAttend(entity.getPlanAttend());
                        dateData.setPlanLeave(entity.getPlanLeaves());
                        dateData.setPlanBreakTime(entity.getPlanBreakTime());
                        dateData.setRealAttend(entity.getRealAttend());
                        dateData.setRealLeave(entity.getRealLeaves());
                        dateData.setRealBreakTime(entity.getRealBreakTime());
                        dateData.setDeepNightBreakTime(entity.getDeepNightBreakTime());
                        dateData.setHolidayFlag(entity.getHolidayFlag());
                        dateData.setWorkContent(entity.getWorkContent());

                        for (DateDetailsContainer container : detailEntities) {
                            if (container.getDate().equals(dateData.getDay())) {
                                dateData.setDateDetailContainer(container);
                            }
                        }
                    }
                }
            }
            return mDateDataList;
        }

        @Override
        protected void onPostExecute(List<DateData> param) {
            mListener.onDataQueryFinished(param);
        }
    }
}
