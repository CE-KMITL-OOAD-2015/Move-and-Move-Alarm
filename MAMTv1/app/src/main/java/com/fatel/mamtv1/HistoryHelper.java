package com.fatel.mamtv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.net.ssl.HostnameVerifier;

/**
 * Created by Monthon on 16/11/2558.
 */
public class HistoryHelper extends SQLiteOpenHelper {
    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;
    public HistoryHelper(Context context){
        super(context,"fatel_historyuser.db",null,History.DATABASE_VerSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_HISTORYUSER_TEBLE = String.format("CREATE TABLE %s"+
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER,%s INTEGER,%s INTEGER)",
                History.TABLE,
                History.Column.ID,
                History.Column.IDUSER,
                History.Column.NUMACCEPT,
                History.Column.CANCEL);
        db.execSQL(CREATE_HISTORYUSER_TEBLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_HISTORYUSER_TABLE ="DROP TABLE IF EXISTS"+History.TABLE;
        db.execSQL(DROP_HISTORYUSER_TABLE);
        onCreate(db);
    }
    public int addHistoryUser(History history){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(History.Column.IDUSER,history.getIdUser());
        values.put(History.Column.NUMACCEPT,history.getNumberOfAccept());
        values.put(History.Column.CANCEL, history.getCancelActivity());
        long id = sqLiteDatabase.insert(History.TABLE,null,values);
        sqLiteDatabase.close();
        return ((int)id);
    }
    public void updateHistoryUser(History history){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(History.Column.IDUSER,history.getIdUser());
        values.put(History.Column.NUMACCEPT,history.getNumberOfAccept());
        values.put(History.Column.CANCEL, history.getCancelActivity());
        int row = sqLiteDatabase.update(History.TABLE,
                values,
                History.Column.ID + " = ? ",
                new String[] {String.valueOf(history.getId())});
        sqLiteDatabase.close();
    }
    public boolean checkdata(){
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query
                (History.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        sqLiteDatabase.close();
        //Log.d("temp", temp + "");
        return false;
    }
    public History getHistoryUser(int idUser){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(History.TABLE, new String[]{History.Column.ID,
                History.Column.IDUSER, History.Column.NUMACCEPT, History.Column.CANCEL
        }, History.Column.IDUSER + " = ? ", new String[]
                {String.valueOf(idUser)}, null, null, null, null);
        History history = null;
        boolean check = false;
        if(cursor != null){
            check = cursor.moveToFirst();
        }
        if(check){
            history = new History(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3));
            cursor.close();
        }
        db.close();
        return history;
    }
    public void deleteUser(int id) {

        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(History.TABLE, History.Column.ID + " = ? ",
                new String[] { String.valueOf(id) });
        //  sqLiteDatabase.delete(History.TABLE, History.Column.ID + " = " + id, null);

        sqLiteDatabase.close();
    }
}
