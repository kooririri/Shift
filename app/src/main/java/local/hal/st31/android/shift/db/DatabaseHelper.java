package local.hal.st31.android.shift.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shift.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE selfSchedule (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("work TEXT,");
        sb.append("memo TEXT,");
        sb.append("start_time TEXT,");
        sb.append("end_time TEXT,");
        sb.append("date DATE");
        sb.append(")");
        String sql = sb.toString();

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
