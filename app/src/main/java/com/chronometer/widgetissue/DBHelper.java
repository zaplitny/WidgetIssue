package com.chronometer.widgetissue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mInstance = null;
    private Context context;

    public static DBHelper getInstance(Context applicationContext) {
        if (mInstance == null) {
            mInstance = new DBHelper(applicationContext);
        }
        return mInstance;
    }

    private DBHelper(Context applicationcontext) {
        super(applicationcontext, "application.db", null, 1);
        this.context = applicationcontext;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE activity (id INTEGER PRIMARY KEY, start INTEGER NOT NULL, status INTEGER NOT NULL)";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {

    }

    public List<Activity> findActivities() {
        ArrayList<Activity> result = new ArrayList<>();
        SQLiteDatabase database = getDatabase();
        Cursor cursor = database.rawQuery("SELECT id, start, status FROM activity WHERE status = 0", null);
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(cursor.getInt(0));
                activity.setStart(cursor.getLong(1));
                activity.setStatus(cursor.getInt(2));
                result.add(activity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public void createActivity(Activity activity) {
        ContentValues values = new ContentValues();
        values.put("start", activity.getStart());
        values.put("status", activity.getStatus());
        Long id = getDatabase().insert("activity", null, values);
        activity.setId(id.intValue());
    }

    public void stopActivity(Long activityId) {
        SQLiteDatabase database = getDatabase();
        try {
            database.beginTransaction();
            database.execSQL("UPDATE activity SET status=1 WHERE id=?", new String[]{
                    activityId.toString()
            });
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }

    private SQLiteDatabase db = null;

    private SQLiteDatabase getDatabase() {
        if (db == null) {
            db = this.getWritableDatabase();
        }
        return db;
    }

}
