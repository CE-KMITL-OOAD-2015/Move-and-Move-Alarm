package com.fatel.mamtv1;

/**
 * Created by Administrator on 23/10/2558.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ImageHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public ImageHelper(Context context){
        super(context, "fatel_alarm.db", null, DatabaseAlarm.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_IMAGE_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT)",
                Image.TABLE,
                Image.Column.ID,
                Image.Column.IMAGE,
                Image.Column.DESCRIPTION);
        Log.i(TAG,CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS"+Image.TABLE;
        db.execSQL(DROP_IMAGE_TABLE);
        Log.i(TAG,"Upgrade Database from "+oldVersion+" to "+newVersion);
        onCreate(db);
    }


}
