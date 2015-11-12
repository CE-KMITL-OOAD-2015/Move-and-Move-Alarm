package com.fatel.mamtv1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Monthon on 12/10/2558.
 */
public class DBAlarmHelper extends SQLiteOpenHelper{
    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public DBAlarmHelper(Context context){
        super(context, "fatel_alarm.db", null, Alarm.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_ALRAM_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
                Alarm.TABLE,
                Alarm.Column.ID,
                Alarm.Column.START_HR,
                Alarm.Column.START_MIN,
                Alarm.Column.STOP_HR,
                Alarm.Column.STOP_MIN,
                Alarm.Column.START_INTERVAL,
                Alarm.Column.STOP_INTERVAL,
                Alarm.Column.FRQ,
                Alarm.Column.DAY);
        Log.i(TAG,CREATE_ALRAM_TABLE);
        db.execSQL(CREATE_ALRAM_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_ALRAM_TABLE = "DROP TABLE IF EXISTS"+ Alarm.TABLE;
        db.execSQL(DROP_ALRAM_TABLE);
        Log.i(TAG,"Upgrade Database from "+oldVersion+" to "+newVersion);
        onCreate(db);
    }
    public void addAlarm(Alarm alarm) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Friend.Column.ID, friend.getId());
        values.put(Alarm.Column.START_HR, alarm.getStarthr());
        values.put(Alarm.Column.START_MIN, alarm.getStartmin());
        values.put(Alarm.Column.STOP_HR, alarm.getStophr());
        values.put(Alarm.Column.STOP_MIN, alarm.getStopmin());
        values.put(Alarm.Column.START_INTERVAL, alarm.getStartinterval());
        values.put(Alarm.Column.STOP_INTERVAL,alarm.getStopinterval());
        values.put(Alarm.Column.FRQ, alarm.getFrq());
        values.put(Alarm.Column.DAY, alarm.getDay());
        sqLiteDatabase.insert(Alarm.TABLE, null, values);
        sqLiteDatabase.close();
    }
    public void UpdateAlarm(Alarm alarm){
        sqLiteDatabase  = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Alarm.Column.START_HR, alarm.getStarthr());
        values.put(Alarm.Column.START_MIN, alarm.getStartmin());
        values.put(Alarm.Column.STOP_HR, alarm.getStophr());
        values.put(Alarm.Column.STOP_MIN, alarm.getStopmin());
        values.put(Alarm.Column.START_INTERVAL, alarm.getStartinterval());
        values.put(Alarm.Column.STOP_INTERVAL,alarm.getStopinterval());
        values.put(Alarm.Column.FRQ, alarm.getFrq());
        values.put(Alarm.Column.DAY, alarm.getDay());
        int row = sqLiteDatabase.update(alarm.TABLE,
                values,
                Alarm.Column.ID + " = ? ",
                new String[] { String.valueOf(1) });
        Log.d("row", row + "");
        sqLiteDatabase.close();
    }
    public int checkdata(){
        int temp = 0;
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query
                (Alarm.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            temp = 1;
        }
        sqLiteDatabase.close();
        Log.d("temp", temp + "");
        return temp;
    }
    public Alarm getAlarm(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Alarm.TABLE, new String[]{Alarm.Column.ID,
                        Alarm.Column.START_HR, Alarm.Column.START_MIN,
                        Alarm.Column.STOP_HR, Alarm.Column.STOP_MIN, Alarm.Column.START_INTERVAL,
                        Alarm.Column.STOP_INTERVAL, Alarm.Column.FRQ,
                        Alarm.Column.DAY
                }, Alarm.Column.ID + " = ? ",
                new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Alarm alarm = new Alarm(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
        cursor.close();
        db.close();
        return alarm;
    }
    public void deleteSetAlarm(String id){
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Alarm.TABLE,Alarm.Column.ID+" = "+id,null);
        sqLiteDatabase.close();
    }
}
