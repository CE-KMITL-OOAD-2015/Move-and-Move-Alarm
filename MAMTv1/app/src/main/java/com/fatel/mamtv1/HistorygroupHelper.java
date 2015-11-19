package com.fatel.mamtv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Monthon on 17/11/2558.
 */
public class HistorygroupHelper extends SQLiteOpenHelper{
    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;
    public HistorygroupHelper(Context context){
        super(context,"fatel_historygroup.db",null,History.DATABASE_VerSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_HISTORYGROUP_TEBLE = String.format("CREATE TABLE %s"+
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER,%s INTEGER,%s INTEGER)",
                Historygroup.TABLE,
                Historygroup.Column.ID,
                Historygroup.Column.IDGROUP,
                Historygroup.Column.NUMACCEPT,
                Historygroup.Column.CANCEL);
        db.execSQL(CREATE_HISTORYGROUP_TEBLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        String DROP_HISTORYGROUP_TABLE ="DROP TABLE IF EXISTS"+Historygroup.TABLE;
        db.execSQL(DROP_HISTORYGROUP_TABLE);
        onCreate(db);
    }
    public int addHistoryGroup(Historygroup history){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Historygroup.Column.IDGROUP,history.getIdGroup());
        values.put(Historygroup.Column.NUMACCEPT,history.getNumberOfAccept());
        values.put(Historygroup.Column.CANCEL, history.getCancelEvent());
        long id = sqLiteDatabase.insert(Historygroup.TABLE,null,values);
        sqLiteDatabase.close();
        return ((int)id);
    }
    public void updateHistoryGroup(Historygroup history){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Historygroup.Column.IDGROUP,history.getIdGroup());
        values.put(Historygroup.Column.NUMACCEPT,history.getNumberOfAccept());
        values.put(Historygroup.Column.CANCEL, history.getCancelEvent());
        int row = sqLiteDatabase.update(Historygroup.TABLE,
                values,
                Historygroup.Column.ID + " = ? ",
                new String[] {String.valueOf(history.getId())});
        sqLiteDatabase.close();
    }
    public boolean checkdata(){
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query
                (Historygroup.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        sqLiteDatabase.close();
        //Log.d("temp", temp + "");
        return false;
    }
    public Historygroup getHistoryGroup(int idGroup){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Historygroup.TABLE, new String[]{Historygroup.Column.ID,
                Historygroup.Column.IDGROUP, Historygroup.Column.NUMACCEPT, Historygroup.Column.CANCEL
        }, Historygroup.Column.IDGROUP + " = ? ", new String[]
                {String.valueOf(idGroup)}, null, null, null, null);
        Historygroup history = null;
        boolean check = false;
        if(cursor != null){
            check = cursor.moveToFirst();
        }
        if(check){
            history = new Historygroup(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3));
            cursor.close();
        }
        db.close();
        return history;
    }
    public void deleteGroup(int id) {

        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(Historygroup.TABLE, Historygroup.Column.ID + " = ? ",
                new String[] { String.valueOf(id) });
        //  sqLiteDatabase.delete(History.TABLE, History.Column.ID + " = " + id, null);

        sqLiteDatabase.close();
    }
}
