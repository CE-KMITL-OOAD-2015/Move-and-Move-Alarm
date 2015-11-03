package com.fatel.testsqlite;

import android.provider.BaseColumns;

/**
 * Created by kid14 on 10/12/2015.
 */
public class Alarm {
    private int id;
    private String startHr;
    private String startMin;
    private String endHr;
    private String endMin;
    private String frq;
    private String day;

    //Database
    public static final String DATABASE_NAME = "set_alarm.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "alarm";

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String START_HR = "start_hr";
        public static final String START_MIN = "start_min";
        public static final String END_HR = "end_hr";
        public static final String END_MIN = "end_min";
        public static final String FRQ = "frq";
        public static final String DAY = "day";
    }

    //Default Constructor
    public Alarm() {

    }
    //Constructor
    public Alarm(int id, String startHr, String startMin, String endHr,
                  String endMin, String frq,String day) {

        this.id = id;
        this.startHr = startHr;
        this.startMin = startMin;
        this.endHr = endHr;
        this.endMin = endMin;
        this.frq = frq;
        this.day = day;
    }

    //Getter, Setter
    public String getStartHr()
    {
        return startHr;
    }

    public String getStartMin()
    {
        return startMin;
    }

    public String getEndHr()
    {
        return endHr;
    }

    public String getEndMin()
    {
        return endMin;
    }

    public String getFrq()
    {
        return frq;
    }

    public String getDay()
    {
        return day;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setStartHr(String temp)
    {
        startHr = temp;
    }

    public void setStartMin(String temp)
    {
        startMin = temp;
    }

    public void setEndHr(String temp)
    {
        endHr = temp;
    }

    public void setEndMin(String temp)
    {
        endMin = temp;
    }

    public void setFrq(String temp)
    {
        frq = temp;
    }

    public void setDay(String temp)
    {
        day = temp;
    }
}
