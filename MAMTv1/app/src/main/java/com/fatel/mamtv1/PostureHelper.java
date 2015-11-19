package com.fatel.mamtv1;

/**
 * Created by Administrator on 23/10/2558.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostureHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase ;
    public static final int DATABASE_VERSION = 1;

    public PostureHelper(Context context){
        super(context, "fatel_posture.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_POSTURE_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER, %s INTEGER, %s TEXT)",
                Posture.TABLE,
                Posture.Column.ID,
                Posture.Column.IDPOSTURE,
                Posture.Column.IMAGE,
                Posture.Column.DESCRIPTION);
        db.execSQL(CREATE_POSTURE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_POSTURE_TABLE = "DROP TABLE IF EXISTS"+ Posture.TABLE;
        db.execSQL(DROP_POSTURE_TABLE);
        onCreate(db);
    }
    public int addPosture(Posture posture){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Posture.Column.IDPOSTURE, posture.getIdPosture());
        values.put(Posture.Column.IMAGE, posture.getImage());
        values.put(Posture.Column.DESCRIPTION, posture.getDescription());
        long id =sqLiteDatabase.insert(Posture.TABLE, null, values);
        sqLiteDatabase.close();
        return ((int)id);
    }
    public void updatePosture(Posture posture){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Posture.Column.IDPOSTURE,posture.getIdPosture());
        values.put(Posture.Column.IMAGE,posture.getImage());
        values.put(Posture.Column.DESCRIPTION, posture.getDescription());
        int row = sqLiteDatabase.update(Posture.TABLE,
                values,
                Posture.Column.ID + " = ? ",
                new String[]{String.valueOf(posture.getId())});
        sqLiteDatabase.close();
    }
    public Posture getPosture(int idPosture){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Posture.TABLE, new String[]{Posture.Column.ID,
                        Posture.Column.IDPOSTURE, Posture.Column.IMAGE, Posture.Column.DESCRIPTION
                }, Posture.Column.IDPOSTURE + " = ? ",
                new String[]{String.valueOf(idPosture)}, null, null, null, null);
        Posture posture;
        boolean check=false;
        if (cursor != null) {
            check = cursor.moveToFirst();
        }
        if(check){

            posture = new Posture(cursor.getInt(0), cursor.getInt(1),
                    cursor.getInt(2), cursor.getString(3));

            cursor.close();
            db.close();
            return posture;
        }
        else {
            db.close();
            return null;
        }
    }
    public int postureCount (){
        sqLiteDatabase=this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + Posture.TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void deletePosture(int id) {

        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(Posture.TABLE, Posture.Column.ID + " = ? ",
                new String[]{String.valueOf(id)});

        sqLiteDatabase.close();
    }




}
