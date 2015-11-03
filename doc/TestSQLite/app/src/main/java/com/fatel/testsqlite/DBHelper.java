package com.fatel.testsqlite;

/**
 * Created by kid14 on 10/12/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, Alarm.DATABASE_NAME, null, Alarm.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FRIEND_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                Alarm.TABLE,
                Alarm.Column.ID,
                Alarm.Column.START_HR,
                Alarm.Column.START_MIN,
                Alarm.Column.END_HR,
                Alarm.Column.END_MIN,
                Alarm.Column.FRQ,
                Alarm.Column.DAY);

        Log.i(TAG, CREATE_FRIEND_TABLE);

        // create friend table
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_FRIEND_TABLE = "DROP TABLE IF EXISTS " + Alarm.TABLE;

        db.execSQL(DROP_FRIEND_TABLE);

        Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);

        onCreate(db);
    }

    public List<String> getAlarm() {
        List<String> alarm = new ArrayList<String>();

        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (Alarm.TABLE, null, null, null, null, null, null,null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while(!cursor.isAfterLast()) {

            alarm.add(cursor.getLong(0) + " " +
                    cursor.getString(1) + " " +
                    cursor.getString(2));

            cursor.moveToNext();
        }

        sqLiteDatabase.close();

        return alarm;
    }

    public void addAlarm(Alarm alarm) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Alarm.Column.START_HR, alarm.getStartHr());
        values.put(Alarm.Column.START_MIN, alarm.getStartMin());
        values.put(Alarm.Column.END_HR, alarm.getEndHr());
        values.put(Alarm.Column.END_MIN, alarm.getEndMin());
        values.put(Alarm.Column.FRQ, alarm.getFrq());
        values.put(Alarm.Column.DAY, alarm.getDay());

        sqLiteDatabase.insert(Alarm.TABLE, null, values);

        sqLiteDatabase.close();
    }
    public Alarm getFriend(String id) {

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query( Alarm.TABLE,
                null,
                Alarm.Column.ID + " = ? ",
                new String[] { id },
                null,
                null,
                null,
                null);


        if (cursor != null) {
            cursor.moveToFirst();
        }

        Alarm alarm = new Alarm();

        alarm.setId((int) cursor.getLong(0));
        alarm.setStartHr(cursor.getString(1));
        alarm.setStartMin(cursor.getString(2));
        alarm.setEndHr(cursor.getString(3));
        alarm.setEndMin(cursor.getString(4));
        alarm.setFrq(cursor.getString(5));
        alarm.setDay(cursor.getString(6));
        return alarm;
    }
}