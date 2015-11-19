package com.fatel.mamtv1;

import android.content.Context;
import android.provider.BaseColumns;

import java.util.HashMap;

/**
 * Created by Monthon on 16/11/2558.
 */
public class History {
    private int id;
    private int idUser;
    private int numberOfAccept;
    private int cancelActivity;

    public static final int DATABASE_VerSION =1 ;
    public static final String TABLE = "viewprogress";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String IDUSER = "iduser";
        public static final String NUMACCEPT = "numberOfAccept";
        public static final String CANCEL = "numberofcancel";
    }
    public History(){

    }
    public History(int idUser){
        this.id=-1;
        this.idUser = idUser;
        this.numberOfAccept = 0;
        this.cancelActivity = 0;
    }
    public History(int id,int idUser,int numberOfAccept,int cancelActivity){
        this.id = id;
        this.idUser = idUser;
        this.numberOfAccept = numberOfAccept;
        this.cancelActivity = cancelActivity;
    }
    public int getId(){
        return id;
    }
    public int getIdUser(){
        return idUser;
    }
    public int getNumberOfAccept(){
        return numberOfAccept;
    }
    public int getCancelActivity(){
        return  cancelActivity;
    }
    public int gettotal(){
        return numberOfAccept+cancelActivity;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setIdUser(int idUser){
        this.idUser = idUser;
    }
    public void setNumberOfAccept(int numberOfAccept){
        this.numberOfAccept = numberOfAccept;
    }
    public void setCancelActivity(int cancelActivity){
        this.cancelActivity = cancelActivity;
    }
    public void addaccept(int numberOfAccept){
        this.numberOfAccept += numberOfAccept;
    }
    public void addcancel(int cancelActivity){
        this.cancelActivity += cancelActivity;
    }
    public void subaccept(int numberOfAccept){
        this.numberOfAccept -= numberOfAccept;
    }
    public void save (Context context){
        HistoryHelper historyHelper = new HistoryHelper(context);
        if(this.id == -1) {
            this.id = historyHelper.addHistoryUser(this);
        }
        else {
            historyHelper.updateHistoryUser(this);
        }
    }
    public static History findHistory(int idUser,Context context){
        HistoryHelper historyHelper = new HistoryHelper(context);
        return historyHelper.getHistoryUser(idUser);
    }

    public HashMap<String, Object> getGeneralValues()
    {
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("numberOfAccept", this.getNumberOfAccept());
        temp.put("cancelActivity", this.getCancelActivity());

        return temp;
    }
}
