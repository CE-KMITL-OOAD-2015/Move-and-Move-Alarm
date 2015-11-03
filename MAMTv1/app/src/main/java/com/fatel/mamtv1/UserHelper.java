package com.fatel.mamtv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Monthon on 3/11/2558.
 */
public class UserHelper extends SQLiteOpenHelper {
    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public UserHelper(Context context){
        super(context, "fatel_user.db", null, User.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_USER_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, " +
                        "%s TEXT,%s TEXT,%s TEXT,%s INTEGER)",
                User.TABLE,
                User.Column.ID,
                User.Column.NAME,
                User.Column.USERNAME,
                User.Column.PASSWORD,
                User.Column.EMAIL,
                User.Column.FACEBOOKID,
                User.Column.AGE,
                User.Column.GENDER,
                User.Column.IMAGE);
        Log.i(TAG, CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS"+ User.TABLE;
        db.execSQL(DROP_USER_TABLE);
        Log.i(TAG,"Upgrade Database from "+oldVersion+" to "+newVersion);
        onCreate(db);
    }
    public void addUser(User user) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Friend.Column.ID, friend.getId());
        values.put(User.Column.NAME, user.getName());
        values.put(User.Column.USERNAME, user.getUsername());
        values.put(User.Column.PASSWORD, user.getPassword());
        values.put(User.Column.EMAIL, user.getEmail());
        values.put(User.Column.FACEBOOKID, user.getFacebookid());
        values.put(User.Column.AGE,user.getAge());
        values.put(User.Column.GENDER, user.getGender());
        values.put(User.Column.IMAGE, user.getImage());
        sqLiteDatabase.insert(User.TABLE, null, values);
        sqLiteDatabase.close();
    }
    public void UpdateUser(User user){
        sqLiteDatabase  = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.Column.NAME, user.getName());
        values.put(User.Column.USERNAME, user.getUsername());
        values.put(User.Column.PASSWORD, user.getPassword());
        values.put(User.Column.EMAIL, user.getEmail());
        values.put(User.Column.FACEBOOKID, user.getFacebookid());
        values.put(User.Column.AGE,user.getAge());
        values.put(User.Column.GENDER, user.getGender());
        values.put(User.Column.IMAGE, user.getImage());
        int row = sqLiteDatabase.update(User.TABLE,
                values,
                User.Column.ID + " = ? ",
                new String[] { String.valueOf(1) });
        Log.d("row", row + "");
        sqLiteDatabase.close();
    }
    public boolean checkdata(){
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query
                (User.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        sqLiteDatabase.close();
        //Log.d("temp", temp + "");
        return false;
    }
    public User getUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE, new String[]{User.Column.ID,
                        User.Column.NAME, User.Column.USERNAME,
                        User.Column.PASSWORD, User.Column.EMAIL, User.Column.FACEBOOKID,
                        User.Column.AGE, User.Column.GENDER,
                        User.Column.IMAGE
                }, User.Column.ID + " = ? ",
                new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getInt(8));
        cursor.close();
        db.close();
        return user;
    }
}
