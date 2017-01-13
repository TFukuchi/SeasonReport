package fukuchi.junpou.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InputValueDbOpenHelper extends SQLiteOpenHelper{

    private final static String DB_NAME = "junpou_data";
    private final static int version = 1;

    public InputValueDbOpenHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";
        sql += "create table ";
        sql += InputValueDbColumns.TABLE_NAME;
        sql += " ( " + InputValueDbColumns.COLUMN_ID + " TEXT primary key";
        sql += "," + InputValueDbColumns.COLUMN_DATE + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_PLAN_ATTEND + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_PLAN_LEAVE + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_PLAN_BREAK_TIME + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_REAL_ATTEND + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_REAL_LEAVE + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_REAL_BREAK_TIME + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_DEEP_NIGHT_BREAK_TIME + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_WORKING_TIME + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_HOLIDAY_FLAG + " TEXT";
        sql += "," + InputValueDbColumns.COLUMN_WORK_CONTENT + " TEXT";
        sql += ")";

        db.execSQL(sql);

        String detailTable = "";
        detailTable += "create table ";
        detailTable += DataDetailDbColumns.TABLE_NAME;
        detailTable += " ( " + DataDetailDbColumns.COLUMN_ID + " TEXT primary key";
        detailTable += "," + DataDetailDbColumns.COLUMN_DATE + " TEXT";
        detailTable += "," + DataDetailDbColumns.COLUMN_PAID_HOLIDAY_FLAG + " TEXT";
        detailTable += "," + DataDetailDbColumns.COLUMN_PAID_TIME_FLAG + " TEXT";
        detailTable += "," + DataDetailDbColumns.COLUMN_TRANSFER_WORK_FLAG + " TEXT";
        detailTable += "," + DataDetailDbColumns.COLUMN_FLEX_FLAG + " TEXT";
        detailTable += "," + DataDetailDbColumns.COLUMN_HOLIDAY_WORK_FLAG + " TEXT";
        detailTable += ")";

        db.execSQL(detailTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
