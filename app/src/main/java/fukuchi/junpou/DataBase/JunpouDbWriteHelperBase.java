package fukuchi.junpou.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;
import java.util.TreeSet;

import fukuchi.junpou.Util.JunpouPattern;

import static fukuchi.junpou.Util.JunpouUtil.formatSeasonText;

public class JunpouDbWriteHelperBase {

    protected final JunpouPattern mPattern;
    protected final InputValueDbOpenHelper mInputValOpenHelper;
    protected final Context mContext;

    protected JunpouDbWriteHelperBase(Context context) {
        mPattern = new JunpouPattern(context);
        mInputValOpenHelper = new InputValueDbOpenHelper(context);
        mContext = context;
    }

    protected void upsert(List<ContentValues> valueList, Context context, String tableName) {
        InputValueDbOpenHelper helper = new InputValueDbOpenHelper(context);
        for (ContentValues values : valueList) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (SQLiteException e) {
                // nop
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }
    }

    public TreeSet<String> seasonQuery() {

        TreeSet<String> seasonSet = new TreeSet<>();

        Cursor cursor = null;
        SQLiteDatabase db = mInputValOpenHelper.getReadableDatabase();

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
}
