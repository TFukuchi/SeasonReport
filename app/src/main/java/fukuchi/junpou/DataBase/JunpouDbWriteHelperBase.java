package fukuchi.junpou.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;

import fukuchi.junpou.Util.LogUtil;

public class JunpouDbWriteHelperBase {

    protected void upsert(List<ContentValues> valueList, Context context, String tableName) {
        InputValueDbOpenHelper helper = new InputValueDbOpenHelper(context);
        for (ContentValues values : valueList) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (SQLiteException e) {
                LogUtil.Log("insert", e.getMessage());
            } finally {
                if (db != null && db.isOpen()) {
                    db.close();
                }
            }
        }
    }
}
