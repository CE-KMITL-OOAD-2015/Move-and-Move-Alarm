package com.fatel.mamtv1;

import android.provider.BaseColumns;

/**
 * Created by Monthon on 12/10/2558.
 */
public class Alarm {
    private int id ;
    private String starthr;
    private String startmin;
    private String stophr;
    private String stopmin;
    private String startinterval;
    private String stopinterval;
    private String frq;
    private String day;
    public static final String DATABASE_NAME = "fatel_alarm.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "alram";
    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String START_HR = "start_hr";
        public static final String START_MIN = "start_min";
        public static final String STOP_HR = "stop_hr";
        public static final String STOP_MIN = "stop_min";
        public static final String START_INTERVAL = "start_interval";
        public static final String STOP_INTERVAL = "stop_interval";
        public static final String FRQ = "frq";
        public static final String DAY = "day";
    }
    public Alarm(){
    }
    public Alarm(int id, String starthr, String startmin, String stophr, String stopmin,
                 String startinterval, String stopinterval, String frq, String day){
        this.id = id;
        this.starthr = starthr;
        this.startmin = startmin;
        this.stophr = stophr;
        this.stopmin = stopmin;
        this.startinterval = startinterval;
        this.stopinterval = stopinterval;
        this.frq = frq;
        this.day = day;
    }

    public int getId(){
        return id;
    }
    public String getStarthr(){
        return starthr;
    }
    public String getStartmin(){
        return startmin;
    }
    public String getStophr(){
        return stophr;
    }
    public String getStopmin(){
        return stopmin;
    }
    public String getStartinterval(){
        return startinterval;
    }
    public String getStopinterval(){
        return stopinterval;
    }
    public String getFrq(){
        return frq;
    }
    public String getDay(){
        return day;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setStarthr(String starthr){
        this.starthr = starthr;
    }
    public void setStartmin(String startmin){
        this.startmin = startmin;
    }
    public void setStophr(String stophr){
        this.stophr = stophr;
    }
    public void setStopmin(String stopmin){
        this.stopmin = stopmin;
    }
    public void setStartinterval(String startinterval){
        this.startinterval = startinterval;
    }
    public void setStopinterval(String stopinterval1){
        this.stopinterval = stopinterval1;
    }
    public void setFrq(String frq){
        this.frq = frq;
    }
    public void setDay(String day){
        this.day = day;
    }
}

