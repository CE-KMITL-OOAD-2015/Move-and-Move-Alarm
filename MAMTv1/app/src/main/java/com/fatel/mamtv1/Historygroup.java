package com.fatel.mamtv1;

import android.content.Context;
import android.provider.BaseColumns;

import java.util.HashMap;

/**
 * Created by Monthon on 17/11/2558.
 */
public class Historygroup {
    private int id;
    private int idGroup;
    private int numberOfAccept;
    private int cancelEvent;

    public static final int DATABASE_VerSION = 1;
    public static final String TABLE = "viewprogressgroup";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String IDGROUP = "idgroup";
        public static final String NUMACCEPT = "numberOfAccept";
        public static final String CANCEL = "cancelevent";
    }

    public Historygroup() {

    }

    public Historygroup(int idGroup) {
        this.id = -1;
        this.idGroup = idGroup;
        this.numberOfAccept = 0;
        this.cancelEvent = 0;
    }

    public Historygroup(int id, int idGroup, int numberOfAccept, int cancelEvent) {
        this.id = id;
        this.idGroup = idGroup;
        this.numberOfAccept = numberOfAccept;
        this.cancelEvent = cancelEvent;
    }

    public int getId() {
        return id;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public int getNumberOfAccept() {
        return numberOfAccept;
    }

    public int getCancelEvent() {
        return cancelEvent;
    }

    public int gettotal() {
        return numberOfAccept + cancelEvent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public void setNumberOfAccept(int numberOfAccept) {
        this.numberOfAccept = numberOfAccept;
    }

    public void setCancelEvent(int cancelEvent) {
        this.cancelEvent = cancelEvent;
    }

    public void addaccept(int numberOfAccept) {
        this.numberOfAccept += numberOfAccept;
        Cache.getInstance().putData("numberOfAccept", 1);
    }

    public void addcancel(int cancelEvent) {
        this.cancelEvent += cancelEvent;
        Cache.getInstance().putData("cancelActivity", 1);
    }

    public void subaccept(int numberOfAccept) {
        this.numberOfAccept -= numberOfAccept;
    }

    //
    public void save(Context context) {
        HistorygroupHelper historygroupHelper = new HistorygroupHelper(context);
        if (this.id == -1) {
            this.id = historygroupHelper.addHistoryGroup(this);
        }
        else {
            historygroupHelper.updateHistoryGroup(this);
        }
    }

    public static Historygroup findHistorygroup(int idGroup,Context context){
        HistorygroupHelper historygroupHelper = new HistorygroupHelper(context);
        return historygroupHelper.getHistoryGroup(idGroup);
    }

    public HashMap<String, Object> getGeneralValues() {
        Cache cache = Cache.getInstance();
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("numberOfAccept", "" + cache.getData("numberOfAccept"));
        temp.put("cancelActivity", "" + cache.getData("cancelActivity"));
        cache.putData("numberOfAccept", 0);
        cache.putData("cancelActivity", 0);

        return temp;
    }
}