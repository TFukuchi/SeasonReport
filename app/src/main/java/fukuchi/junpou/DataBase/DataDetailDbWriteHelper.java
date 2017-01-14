package fukuchi.junpou.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.Util.JunpouUtil;
import fukuchi.junpou.Util.LogUtil;

public class DataDetailDbWriteHelper {

    private final InputValueDbOpenHelper mOpenHelper;
    private final Context mContext;

    public DataDetailDbWriteHelper(Context context) {
        mOpenHelper = new InputValueDbOpenHelper(context);
        mContext = context;
    }

    //put Data
    public static ContentValues createContentValue(String id, String date, boolean paidHolidayFlag,
                                                   String paidTime, boolean transferWorkFlag,
                                                   boolean flexFlag, boolean holidayWork) {
        ContentValues values = new ContentValues();
        values.put(DataDetailDbColumns.COLUMN_ID, id);
        values.put(DataDetailDbColumns.COLUMN_DATE, date);
        values.put(DataDetailDbColumns.COLUMN_PAID_HOLIDAY_FLAG,
                JunpouUtil.booleanToStringConverter(paidHolidayFlag));
        values.put(DataDetailDbColumns.COLUMN_PAID_TIME_FLAG, paidTime);
        values.put(DataDetailDbColumns.COLUMN_TRANSFER_WORK_FLAG,
                JunpouUtil.booleanToStringConverter(transferWorkFlag));
        values.put(DataDetailDbColumns.COLUMN_FLEX_FLAG,
                JunpouUtil.booleanToStringConverter(flexFlag));
        values.put(DataDetailDbColumns.COLUMN_HOLIDAY_WORK_FLAG,
                JunpouUtil.booleanToStringConverter(holidayWork));

        return values;
    }

    public void upsert(List<ContentValues> valueList) {
        for (ContentValues values : valueList) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            try {
                db.insertWithOnConflict(DataDetailDbColumns.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (SQLiteException e) {
                LogUtil.Log("insert", e.getMessage());
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    /**
     * @param dateList
     * @return Selected season date data container list
     */
    public List<DateDetailsContainer> dataDetailQuery(List<String> dateList) {
        String selection = DataDetailDbColumns.COLUMN_ID + " = ?";
        List<DateDetailsContainer> dataContainerList = new ArrayList<>();

        // TODO
        // ここらへんfor文の数だけDbから取得してるから修正必要。

        for (int i = 0; i < dateList.size(); i++) {
            String[] selectionArgs = new String[1];
            selectionArgs[0] = dateList.get(i);
            InputValueDbOpenHelper helper = new InputValueDbOpenHelper(mContext);
            SQLiteDatabase db = helper.getWritableDatabase();

            DateDetailsContainer container = new DateDetailsContainer();

            Cursor cursor = null;
            try {
                cursor = db.query(DataDetailDbColumns.TABLE_NAME, null,
                        selection, selectionArgs, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    String date = cursor.getString(cursor.getColumnIndex(
                            DataDetailDbColumns.COLUMN_DATE));
                    String paidHolidayFlag = cursor.getString(cursor.getColumnIndex(
                            DataDetailDbColumns.COLUMN_PAID_HOLIDAY_FLAG));
                    String paidTime = cursor.getString(cursor.getColumnIndex(
                            DataDetailDbColumns.COLUMN_PAID_TIME_FLAG));
                    String transferWorkFlag = cursor.getString(cursor.getColumnIndex(
                            DataDetailDbColumns.COLUMN_TRANSFER_WORK_FLAG));
                    String flexFlag = cursor.getString(cursor.getColumnIndex(
                            DataDetailDbColumns.COLUMN_FLEX_FLAG));
                    String holidayWorkFlag = cursor.getString(cursor.getColumnIndex(
                            DataDetailDbColumns.COLUMN_HOLIDAY_WORK_FLAG));

                    container.setDate(date);
                    container.setPaidHolidayFlag(
                            JunpouUtil.stringToBooleanConverter(paidHolidayFlag));
                    container.setPaidTime(paidTime);
                    container.setTransferWorkFrag(
                            JunpouUtil.stringToBooleanConverter(transferWorkFlag));
                    container.setFlex(JunpouUtil.stringToBooleanConverter(flexFlag));
                    container.setHolidayWorkFlag(
                            JunpouUtil.stringToBooleanConverter(holidayWorkFlag));

                    dataContainerList.add(container);
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