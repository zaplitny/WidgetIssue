package com.chronometer.widgetissue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mInstance = null;

    public static DBHelper getInstance(Context applicationContext) {
        if (mInstance == null) {
            mInstance = new DBHelper(applicationContext);
        }
        return mInstance;
    }

    private DBHelper(Context applicationcontext) {
        super(applicationcontext, "application.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE activity (id INTEGER PRIMARY KEY, start INTEGER NOT NULL, status INTEGER NOT NULL)";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {

    }

    List<CustomTimer> findActivities() {
        ArrayList<CustomTimer> result = new ArrayList<>();
        SQLiteDatabase database = getDatabase();
        Cursor cursor = database.rawQuery("SELECT id, start, status FROM activity WHERE status = 0", null);
        if (cursor.moveToFirst()) {
            do {
                CustomTimer customTimer = new CustomTimer();
                customTimer.setId(cursor.getLong(0));
                customTimer.setStart(cursor.getLong(1));
                customTimer.setStatus(cursor.getInt(2));
                result.add(customTimer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    void createActivity(CustomTimer a) {
        CustomTimer customTimer = new CustomTimer();
        customTimer.setStart(new Date().getTime());

        ContentValues values = new ContentValues();
        values.put("start", customTimer.getStart());
        values.put("status", customTimer.getStatus());
        Long id = getDatabase().insert("activity", null, values);
        customTimer.setId(id.intValue());
    }

    void stopActivity(Long activityId) {
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
