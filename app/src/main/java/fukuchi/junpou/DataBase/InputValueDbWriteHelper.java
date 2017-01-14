package fukuchi.junpou.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import fukuchi.junpou.Model.DateData;
import fukuchi.junpou.Util.LogUtil;
import fukuchi.junpou.Util.JunpouPattern;

import static fukuchi.junpou.Util.JunpouUtil.createId;
import static fukuchi.junpou.Util.JunpouUtil.formatSeasonText;
import static fukuchi.junpou.Util.JunpouUtil.getTotalWorkingTime;

public class InputValueDbWriteHelper {

    private final InputValueDbOpenHelper mOpenHelper;
    private final Context mContext;
    private final JunpouPattern mPattern;

    public InputValueDbWriteHelper(Context context) {
        mOpenHelper = new InputValueDbOpenHelper(context);
        mContext = context;
        mPattern = new JunpouPattern(context);
    }

    //put Data
    public static ContentValues createContentValue(DateData dateData) {
        String realAttend = dateData.getRealAttend();
        String realLeaves = dateData.getRealLeave();
        String realBreakTime = dateData.getRealBreakTime();

        ContentValues values = new ContentValues();
        values.put(InputValueDbColumns.COLUMN_ID, dateData.getId());
        values.put(InputValueDbColumns.COLUMN_DATE, dateData.getDay());
        values.put(InputValueDbColumns.COLUMN_PLAN_ATTEND, dateData.getPlanAttend());
        values.put(InputValueDbColumns.COLUMN_PLAN_LEAVE, dateData.getPlanLeave());
        values.put(InputValueDbColumns.COLUMN_PLAN_BREAK_TIME, dateData.getPlanBreakTime());
        values.put(InputValueDbColumns.COLUMN_REAL_ATTEND, realAttend);
        values.put(InputValueDbColumns.COLUMN_REAL_LEAVE, realLeaves);
        values.put(InputValueDbColumns.COLUMN_REAL_BREAK_TIME, realBreakTime);
        values.put(InputValueDbColumns.COLUMN_DEEP_NIGHT_BREAK_TIME, dateData.getDeepNightBreakTime());

        String workingTime = getTotalWorkingTime(realAttend, realLeaves, realBreakTime,
                dateData.getDetailContainer().getPaidTime());
        values.put(InputValueDbColumns.COLUMN_WORKING_TIME, workingTime);
        values.put(InputValueDbColumns.COLUMN_HOLIDAY_FLAG, dateData.getHolidayText());
        values.put(InputValueDbColumns.COLUMN_WORK_CONTENT, dateData.getWorkContent());

        return values;
    }

    public void upsert(List<ContentValues> valueList) {
        for (ContentValues values : valueList) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            try {
                db.insertWithOnConflict(InputValueDbColumns.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (SQLiteException e) {
                LogUtil.Log("insert", e.getMessage());
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    // カラムIdと日付だけ取得している。
    // 用途としては、スピナーに何旬かを設定するためにDatabaseに登録されている、
    // 日付をすべて取得してくる
    public TreeSet<String> seasonQuery() {

        TreeSet<String> seasonSet = new TreeSet<>();

        Cursor cursor = null;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        String[] targetColumn = new String[]{InputValueDbColumns.COLUMN_ID,
                InputValueDbColumns.COLUMN_DATE};

        try {
            cursor = db.query(InputValueDbColumns.TABLE_NAME, targetColumn, null, null, null, null,
                    null);
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(
                        cursor.getColumnIndex(InputValueDbColumns.COLUMN_ID));
                String date = cursor.getString(
                        cursor.getColumnIndex(InputValueDbColumns.COLUMN_DATE));

                String seasonText = formatSeasonText(id, date, mPattern);
                seasonSet.add(seasonText);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return seasonSet;
    }

    // Excelに出力する際に使用する
    // 渡されてきた月と旬から、該当する日付を取得して、勤務時間を返す
    // つまるところ用途としては、現在の月と旬を渡すのではなく、
    // ここを呼び出す前に、必要な旬と月を計算してここを呼び出す。
    // で、ここはIDをkeyとしたdayを返すので、そこで計算する
    public Map<String, String> workTimeQuery(int year, int month,
                                             @JunpouPattern.SeasonNumber int season) {

        //Map<id, workingTime>
        Map<String, String> workingTimeMap = new HashMap<>();

        Cursor cursor = null;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String[] targetColumn = new String[]{InputValueDbColumns.COLUMN_ID,
                InputValueDbColumns.COLUMN_WORKING_TIME};

        String selection = InputValueDbColumns.COLUMN_ID + " = ? ";
        List<Integer> targetDayList = mPattern.getDayListFromSeason(season);
        String[] selectionArgs = new String[targetDayList.size()];
        for (int i = 0; i < targetDayList.size(); i++) {
            int day = targetDayList.get(i);
            String targetId = createId(year, month, day);
            selectionArgs[i] = targetId;
            if (i != 0) {
                selection += " OR " + InputValueDbColumns.COLUMN_ID + " = ? ";
            }
        }
        try {
            cursor = db.query(InputValueDbColumns.TABLE_NAME, targetColumn, selection,
                    selectionArgs, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                String targetId = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_ID));
                String workingTime =
                        cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_WORKING_TIME));

                workingTimeMap.put(targetId, workingTime);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return workingTimeMap;
    }

    /**
     * @param dateList
     * @return Selected season date data container list
     */
    public List<DataContainer> dateDataQuery(List<String> dateList) {
        String selection = InputValueDbColumns.COLUMN_ID + " = ?";
        List<DataContainer> dataContainerList = new ArrayList<>();

        // TODO
        // ここらへんfor文の数だけDbから取得してるから修正必要。

        for (int i = 0; i < dateList.size(); i++) {
            String[] selectionArgs = new String[1];
            selectionArgs[0] = dateList.get(i);
            InputValueDbOpenHelper helper = new InputValueDbOpenHelper(mContext);
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor cursor = null;
            try {
                cursor = db.query(InputValueDbColumns.TABLE_NAME, null,
                        selection, selectionArgs, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    String date = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_DATE));
                    String planAttend = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_PLAN_ATTEND));
                    String planLeaves = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_PLAN_LEAVE));
                    String planBreakTime = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_PLAN_BREAK_TIME));
                    String realAttend = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_REAL_ATTEND));
                    String realLeaves = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_REAL_LEAVE));
                    String realBreakTime = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_REAL_BREAK_TIME));
                    String deepNightBreakTime = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_DEEP_NIGHT_BREAK_TIME));
                    String holiday = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_HOLIDAY_FLAG));
                    String workContent = cursor.getString(cursor.getColumnIndex(InputValueDbColumns.COLUMN_WORK_CONTENT));
                    DataContainer entity = new DataContainer(mContext, date, planAttend, planLeaves,
                            planBreakTime, realAttend, realLeaves, realBreakTime, deepNightBreakTime,
                            holiday, workContent);
                    dataContainerList.add(entity);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return dataContainerList;
    }
}
