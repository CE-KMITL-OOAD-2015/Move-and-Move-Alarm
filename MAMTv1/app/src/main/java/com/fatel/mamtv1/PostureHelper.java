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

public class PostureHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase ;
    public static final int DATABASE_VERSION = 1;

    public PostureHelper(Context context){
        super(context, "fatel_image.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_IMAGE_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER, %s INTEGER, %s TEXT)",
                Posture.TABLE,
                Posture.Column.ID,
                Posture.Column.IMAGE,
                Posture.Column.DESCRIPTION);
        // Log.i(TAG,CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS"+ Posture.TABLE;
        db.execSQL(DROP_IMAGE_TABLE);
        Log.i(TAG,"Upgrade Database from "+oldVersion+" to "+newVersion);
        onCreate(db);
    }

    public void delete (){

        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        db.execSQL("delete from" + Posture.TABLE);
        db.close();
    }

    public int loadImage(int id){

        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.query(Posture.TABLE, new String[]{Posture.Column.ID, Posture.Column.IMAGE, Posture.Column.DESCRIPTION}, Posture.Column.ID + " = ? ",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        int img = Integer.parseInt(cursor.getString(1));
        return img;

    }

    public String loadDescription(int id){

        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.query(Posture.TABLE, new String[]{Posture.Column.ID, Posture.Column.IMAGE, Posture.Column.DESCRIPTION}, Posture.Column.ID + " = ? ",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String des = cursor.getString(2);
        return des;

    }

    public void saveImage(int id,int image,String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Posture.Column.ID, id);
        values.put(Posture.Column.IMAGE, image);
        values.put(Posture.Column.DESCRIPTION, description);
        db.insert(Posture.TABLE, null, values);
        db.close();
    }

    public boolean hasImage(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.query(Posture.TABLE, new String[]{Posture.Column.ID}, null, null, null, null, null);
        Cursor cursor = db.query(Posture.TABLE, new String[]{Posture.Column.ID, Posture.Column.IMAGE, Posture.Column.DESCRIPTION}, Posture.Column.ID + " = ? ",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }



}
