package com.fatel.mamtv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;

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
        //not sure %s int for image
        String CREATE_USER_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT,%s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s INTEGER" +
                        ",%s INTEGER,%s INTEGER, %s TEXT, %s TEXT,%s TEXT,%s TEXT,%s INTEGER,%s INTEGER,%s INTEGER,%s INTEGER)",
                User.TABLE,
                User.Column.ID,
                User.Column.IDUSER,
                User.Column.FIRSTNAME,
                User.Column.LASTNAME,
                User.Column.USERNAME,
                User.Column.AGE,
                User.Column.SCORE,
                User.Column.GENDER,
                User.Column.EMAIL,
                User.Column.FACEBOOKID,
                User.Column.FACEBOOKFIRSTNAME,
                User.Column.FACEBOOKLASTNAME,
                User.Column.PROFILEIMAGE,
                User.Column.LOGIN,
                User.Column.IDGROUP,
                User.Column.STATESW);
        db.execSQL(CREATE_USER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS"+ User.TABLE;
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }
    public int addUser(User user) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.Column.IDUSER, user.getIdUser());
        values.put(User.Column.FIRSTNAME, user.getFirstName());
        values.put(User.Column.LASTNAME, user.getLastName());
        values.put(User.Column.USERNAME, user.getUserName());
        values.put(User.Column.AGE, user.getAge());
        values.put(User.Column.SCORE, user.getScore());
        values.put(User.Column.GENDER,user.getGender());
        values.put(User.Column.EMAIL, user.getEmail());
        values.put(User.Column.FACEBOOKID, user.getFacebookID());
        values.put(User.Column.FACEBOOKFIRSTNAME, user.getFacebookFirstName());
        values.put(User.Column.FACEBOOKLASTNAME, user.getFacebookLastName());
        values.put(User.Column.PROFILEIMAGE,user.getProfileImage());
        values.put(User.Column.LOGIN, user.getLogin());
        values.put(User.Column.IDGROUP,user.getIdGroup());
        values.put(User.Column.STATESW,user.getStatesw());
        long id = sqLiteDatabase.insert(User.TABLE, null, values);
        sqLiteDatabase.close();
        return ((int)id);
    }
    public void updateUser(User user){
        sqLiteDatabase  = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.Column.IDUSER, user.getIdUser());
        values.put(User.Column.FIRSTNAME, user.getFirstName());
        values.put(User.Column.LASTNAME, user.getLastName());
        values.put(User.Column.USERNAME, user.getUserName());
        values.put(User.Column.AGE, user.getAge());
        values.put(User.Column.SCORE, user.getScore());
        values.put(User.Column.GENDER,user.getGender());
        values.put(User.Column.EMAIL, user.getEmail());
        values.put(User.Column.FACEBOOKID, user.getFacebookID());
        values.put(User.Column.FACEBOOKFIRSTNAME, user.getFacebookFirstName());
        values.put(User.Column.FACEBOOKLASTNAME, user.getFacebookLastName());
        values.put(User.Column.PROFILEIMAGE, user.getProfileImage());
        values.put(User.Column.LOGIN, user.getLogin());
        values.put(User.Column.IDGROUP,user.getIdGroup());
        values.put(User.Column.STATESW,user.getStatesw());
        int row = sqLiteDatabase.update(User.TABLE,
                values,
                User.Column.ID + " = ? ",
                new String[] { String.valueOf(user.getId())});
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
    public User getUser(int idUser){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE, new String[]{User.Column.ID,
                        User.Column.IDUSER, User.Column.FIRSTNAME, User.Column.LASTNAME,
                        User.Column.USERNAME, User.Column.AGE, User.Column.SCORE,
                        User.Column.GENDER, User.Column.EMAIL,
                        User.Column.FACEBOOKID, User.Column.FACEBOOKFIRSTNAME, User.Column.FACEBOOKLASTNAME,
                        User.Column.PROFILEIMAGE, User.Column.LOGIN, User.Column.IDGROUP, User.Column.STATESW
                }, User.Column.IDUSER + " = ? ",
                new String[]{String.valueOf(idUser)}, null, null, null, null);
        User user;
        boolean check=false;
        if (cursor != null) {
            check = cursor.moveToFirst();
        }
        if(check){

        user = new User(cursor.getInt(0), cursor.getInt(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                cursor.getInt(6), cursor.getInt(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getInt(12),
                cursor.getInt(13),cursor.getInt(14),cursor.getInt(15));



        cursor.close();
            db.close();
            return user;
        }
        else {
            db.close();
            return null;
        }

    }
    public User checkLoginUser(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE, new String[]{User.Column.ID,
                        User.Column.IDUSER, User.Column.FIRSTNAME, User.Column.LASTNAME,
                        User.Column.USERNAME, User.Column.AGE, User.Column.SCORE,
                        User.Column.GENDER, User.Column.EMAIL,
                        User.Column.FACEBOOKID, User.Column.FACEBOOKFIRSTNAME, User.Column.FACEBOOKLASTNAME,
                        User.Column.PROFILEIMAGE, User.Column.LOGIN , User.Column.IDGROUP, User.Column.STATESW
                }, User.Column.LOGIN + " = ? ",
                new String[]{String.valueOf(1)}, null, null, null, null);


        User user=null;
        boolean check=false;
        if (cursor != null) {
            check = cursor.moveToFirst();
        }
        if(check){
            user = new User(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5),
                    cursor.getInt(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getInt(12),
                    cursor.getInt(13),cursor.getInt((14)),cursor.getInt(15));

            cursor.close();
        }
        db.close();
        return user;

    }
    public void deleteUser(int id) {

        sqLiteDatabase = this.getWritableDatabase();

    sqLiteDatabase.delete(User.TABLE, User.Column.ID + " = ? ",
            new String[] { String.valueOf(id) });

        sqLiteDatabase.close();
    }
}