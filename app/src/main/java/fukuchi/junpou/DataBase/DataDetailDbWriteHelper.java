package fukuchi.junpou.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fukuchi.junpou.Model.DateDetailsContainer;
import fukuchi.junpou.Util.JunpouUtil;

public class DataDetailDbWriteHelper extends JunpouDbWriteHelperBase {

    public DataDetailDbWriteHelper(Context context) {
        super(context);
    }

    //put Data
    public ContentValues createContentValue(String id, String date, boolean paidHolidayFlag,
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
        super.upsert(valueList, mContext, DataDetailDbColumns.TABLE_NAME);
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
                    cursor = null;
                }
            }
        }

        return dataContainerList;
    }
}