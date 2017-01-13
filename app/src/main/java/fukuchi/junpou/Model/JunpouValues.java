package fukuchi.junpou.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fukuchi.junpou.DataBase.InputValueDbWriteHelper;
import fukuchi.junpou.Util.JunpouUtil;

import static fukuchi.junpou.Model.JunpouPattern.FIRST_SEASON_NUM;
import static fukuchi.junpou.Model.JunpouPattern.SECOND_SEASON_NUM;
import static fukuchi.junpou.Model.JunpouPattern.THIRD_SEASON_NUM;

public class JunpouValues {

    private final int mYear;
    private final int mMonth;
    @JunpouPattern.SeasonNumber
    private final int mSeason;
    private final Context mContext;

    private final String mTotalWorkingTime;

    /**
     * @param year   : 2016
     * @param month  : 12
     * @param season : 1
     */
    public JunpouValues(int year, int month, @JunpouPattern.SeasonNumber int season, Context context) {
        mYear = year;
        mMonth = month;
        mSeason = season;

        mContext = context;
        mTotalWorkingTime = calTotalTime();
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    @JunpouPattern.SeasonNumber
    public int getSeason() {
        return mSeason;
    }

    public String getTotalWorkingTime() {
        return mTotalWorkingTime;
    }

    private String calTotalTime() {
        List<Integer> targetSeasonList = getNecessarySeason();
        InputValueDbWriteHelper helper = new InputValueDbWriteHelper(mContext);
        String totalTime = "00:00";

        for (int i = 0; i < targetSeasonList.size(); i++) {
            int target = targetSeasonList.get(i);
            int targetMonth = mMonth;
            int targetYear = mYear;
            if (mSeason == JunpouPattern.FIRST_SEASON_NUM) {
                if (mMonth != 1) {
                    targetMonth = mMonth - 1;
                } else {
                    targetMonth = 12;
                    targetYear--;
                }
            }

            @JunpouPattern.SeasonNumber int season = FIRST_SEASON_NUM;
            if (target == 1) {
                season = FIRST_SEASON_NUM;
            } else if (target == 2) {
                season = SECOND_SEASON_NUM;
            } else if (target == 3) {
                season = THIRD_SEASON_NUM;
            }

            Map<String, String> recentWorkingTimeMap = helper.workTimeQuery(targetYear,
                    targetMonth, season);
            for (String time : recentWorkingTimeMap.values()) {
                if (time == null || time.isEmpty()) {
                    continue;
                }
                totalTime = JunpouUtil.calSumTime(totalTime, time);
            }
        }

        return totalTime;
    }

    // 累計時間の出力に必要なシーズンを取得する
    // 例えば、2旬の際は2旬だけで問題ないが、
    // 1旬の場合は、前の月の2旬と3旬が必要になる。
    // 3旬の場合は2旬が必要
    private List<Integer> getNecessarySeason() {
        List<Integer> necessarySeasonList = new ArrayList<>();

        switch (mSeason) {
            case FIRST_SEASON_NUM:
                necessarySeasonList.add(THIRD_SEASON_NUM); // FALL_THROUGH
            case THIRD_SEASON_NUM:
                necessarySeasonList.add(SECOND_SEASON_NUM);
            case SECOND_SEASON_NUM: // FALL_THROUGH
            default:
                break;
        }

        return necessarySeasonList;
    }
}
