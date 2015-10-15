package com.fatel.mamtv1;

/**
 * Created by kid14 on 10/15/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.fatel.mamtv1.Constants.DESCRIPTION;
import static com.fatel.mamtv1.Constants.IMAGE;
import static com.fatel.mamtv1.Constants.TABLE_NAME;
import static com.fatel.mamtv1.Constants._ID;

public class ActivityHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "images.db";
    private static final int DATABASE_VERSION = 1;

    public ActivityHelper(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMAGE
                + " INTEGER, " + DESCRIPTION + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        //upgradedatabase code
    }

}
