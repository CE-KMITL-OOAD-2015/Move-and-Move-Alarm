package com.fatel.mamtv1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monthon on 12/10/2558.
 */
public class DBAlarmHelper extends SQLiteOpenHelper{
    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public DBAlarmHelper(Context context){
        super(context, "fatel_alarm.db", null, DatabaseAlarm.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_ALRAM_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
                DatabaseAlarm.TABLE,
                DatabaseAlarm.Column.ID,
                DatabaseAlarm.Column.START_HR,
                DatabaseAlarm.Column.START_MIN,
                DatabaseAlarm.Column.STOP_HR,
                DatabaseAlarm.Column.STOP_MIN,
                DatabaseAlarm.Column.START_INTERVAL,
                DatabaseAlarm.Column.STOP_INTERVAL,
                DatabaseAlarm.Column.FRQ,
                DatabaseAlarm.Column.DAY);
        Log.i(TAG,CREATE_ALRAM_TABLE);
        db.execSQL(CREATE_ALRAM_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_ALRAM_TABLE = "DROP TABLE IF EXISTS"+DatabaseAlarm.TABLE;
        db.execSQL(DROP_ALRAM_TABLE);
        Log.i(TAG,"Upgrade Database from "+oldVersion+" to "+newVersion);
        onCreate(db);
    }
    public void addAlarm(DatabaseAlarm alarm) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Friend.Column.ID, friend.getId());
        values.put(DatabaseAlarm.Column.START_HR, alarm.getStarthr());
        values.put(DatabaseAlarm.Column.START_MIN, alarm.getStartmin());
        values.put(DatabaseAlarm.Column.STOP_HR, alarm.getStophr());
        values.put(DatabaseAlarm.Column.STOP_MIN, alarm.getStopmin());
        values.put(DatabaseAlarm.Column.START_INTERVAL, alarm.getStartinterval());
        values.put(DatabaseAlarm.Column.STOP_INTERVAL,alarm.getStopinterval());
        values.put(DatabaseAlarm.Column.FRQ, alarm.getFrq());
        values.put(DatabaseAlarm.Column.DAY, alarm.getDay());
        sqLiteDatabase.insert(DatabaseAlarm.TABLE, null, values);
        sqLiteDatabase.close();
    }
    public void UpdateAlarm(DatabaseAlarm alarm){
        sqLiteDatabase  = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseAlarm.Column.START_HR, alarm.getStarthr());
        values.put(DatabaseAlarm.Column.START_MIN, alarm.getStartmin());
        values.put(DatabaseAlarm.Column.STOP_HR, alarm.getStophr());
        values.put(DatabaseAlarm.Column.STOP_MIN, alarm.getStopmin());
        values.put(DatabaseAlarm.Column.START_INTERVAL, alarm.getStartinterval());
        values.put(DatabaseAlarm.Column.STOP_INTERVAL,alarm.getStopinterval());
        values.put(DatabaseAlarm.Column.FRQ, alarm.getFrq());
        values.put(DatabaseAlarm.Column.DAY, alarm.getDay());
        int row = sqLiteDatabase.update(alarm.TABLE,
                values,
                DatabaseAlarm.Column.ID + " = ? ",
                new String[] { String.valueOf(1) });
        Log.d("row", row + "");
        sqLiteDatabase.close();
    }
    public int checkdata(){
        int temp = 0;
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query
                (DatabaseAlarm.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            temp = 1;
        }
        sqLiteDatabase.close();
        Log.d("temp", temp + "");
        return temp;
    }
    public DatabaseAlarm getAlarm(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabaseAlarm.TABLE, new String[]{DatabaseAlarm.Column.ID,
                        DatabaseAlarm.Column.START_HR, DatabaseAlarm.Column.START_MIN,
                        DatabaseAlarm.Column.STOP_HR, DatabaseAlarm.Column.STOP_MIN, DatabaseAlarm.Column.START_INTERVAL,
                        DatabaseAlarm.Column.STOP_INTERVAL, DatabaseAlarm.Column.FRQ,
                        DatabaseAlarm.Column.DAY
                }, DatabaseAlarm.Column.ID + " = ? ",
                new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        DatabaseAlarm alarm = new DatabaseAlarm(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
        return alarm;
    }
}
