package com.fatel.mamtv1;
import android.provider.BaseColumns;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
/**
 * Created by Administrator on 23/10/2558.
 */
public class Image {

    private int id;
    private int image;
    private String description;
    private ImageHelper helper;

    public static final String DATABASE_NAME = "fatel_alarm.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE = "imageTB";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String IMAGE= "image";
        public static final String DESCRIPTION = "description";
    }

    public Image (int id,int image,String description){
        this.id=id;
        this.image=image;
        this.description=description;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Column.ID, this.id);
        values.put(Column.IMAGE, this.image);
        values.put(Column.DESCRIPTION, this.description);
        db.insert(this.TABLE, null, values);
        db.close();
    }



   // public Image loadImage(){



   // }



}
