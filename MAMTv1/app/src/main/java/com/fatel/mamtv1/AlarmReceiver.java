package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager manager;
    private DBAlarmHelper mAlarmHelper;
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the service, keeping the device awake while it is launching.
        Log.i("go", "go to reeceiver");
        Bundle extras = intent.getExtras();
        String temp = extras.getString("key");
        mAlarmHelper = new DBAlarmHelper(context);
        if(temp==null){
            Log.i("null","temp null");
        }
        else{
            Log.i("temp",temp);
        }
        /*if(manager == null){
            Log.i("null","manager null");
        }else{
            Log.i("!null","manager");
        }*/
        if(checkday()){
            //choose day
            Log.i("Day", "true");
            //bug
            if(checkstarttime()&&(temp==null||!temp.equalsIgnoreCase("first"))){
                //bug
                    if (manager!= null) {
                       manager.cancel(pendingIntent);
                    }
                    Log.i("starttimr", "true");
                    Intent i = new Intent(context, actAlarm.class);
                    Log.i("AlarmReceiver", "CanJump");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
            }
            else if(inRange(timenow())) {
                Log.i("range", "true");
                setfrq(context,temp);
            }
            else{
                Log.i("range sttime", "false");
                setnextstart(context, temp);
            }
        }
        else{
            //don't choose day
            Log.i("Day", "false");
            setnextstart(context, temp);
        }
    }
    public boolean checkday(){
        //bug start set
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Alarm alarm = mAlarmHelper.getAlarm();
        //choose day return true
        if (Integer.parseInt(alarm.getDay().substring(calendar.get(Calendar.DAY_OF_WEEK) - 1, calendar.get(Calendar.DAY_OF_WEEK))) == 1)
            return true;
        else
            return false;
    }
    public boolean inRange(Date now) {
        Alarm alarm = mAlarmHelper.getAlarm();
        String stopin = alarm.getStopinterval();
        String startin = alarm.getStartinterval();
        int starthour = Integer.parseInt(alarm.getStarthr());
        String startmin = alarm.getStartmin();
        if (startin.equalsIgnoreCase("am")) {
            if (starthour == 12)
                starthour = 0;
        } else {
            if (starthour == 12)
                starthour = 12;
            else
                starthour += 12;
        }
        int stophour = Integer.parseInt(alarm.getStophr());
        String stopmin = alarm.getStopmin();
        if (stopin.equalsIgnoreCase("am")) {
            if (stophour == 12)
                stophour = 0;
        } else {
            if (stophour == 12)
                stophour = 12;
            else
                stophour += 12;
        }
        String starthr = "";
        String stophr = "";

        if(starthour<10){
            starthr = "0"+String.valueOf(starthour);
        }
        else{
            starthr = String.valueOf(starthour);
        }
        if(stophour<10){
            stophr = "0"+String.valueOf(stophour);
        }
        else{
            stophr = String.valueOf(stophour);
        }
        Date start = null;
        Date end = null;
        try {
            //All your parse Operations
            Log.i("inRange","error"+" "+starthr+"."+startmin+".00");
            start = new SimpleDateFormat("HH.mm.ss").parse(starthr+"."+startmin+".00");
            //end = new SimpleDateFormat("HH.mm.ss").parse(stophr+"."+stopmin+".00");
        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }
        try {
            //All your parse Operations
            Log.i("inRange","error"+" "+stophr+"."+stopmin+".00");
            // start = new SimpleDateFormat("HH.mm.ss").parse(starthr+"."+startmin+".00");
            end = new SimpleDateFormat("HH.mm.ss").parse(stophr+"."+stopmin+".00");
        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }
        Log.i("cross day",end.before(start)+"");
        if(checkstarttime()){
            return true;
        }
        if(end.before(start)){

            return now.after(start)|| now.before(end);
        }
        else{
            return now.after(start)&& now.before(end);
        }
    }
    public void setfrq(Context context,String massage){
        if(massage==null)
            Log.d("message","null");
        else
            Log.d("message",massage);
        if(massage==null||massage.equalsIgnoreCase("act")){
            Intent i = new Intent(context, actAlarm.class);
            Log.i("AlarmReceiver", "CanJump");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        }
        else{
            /*if (manager!= null) {
                manager.cancel(pendingIntent);
            }*/
            Intent alarmIntent = new Intent(context , AlarmReceiver.class);
            //Intent alarmIntent = new Intent(context , actAlarm.class);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Alarm alarm = mAlarmHelper.getAlarm();
            int frequency = Integer.parseInt(alarm.getFrq());
            //int interval = 60*1000*frequency;
            int interval = 60*1000/10;
            Bundle mes = new Bundle();
            mes.putString("key", "act");
            alarmIntent.putExtras(mes);
            //context.sendBroadcast(alarmIntent);
            pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
        }
    }
    public boolean checkstarttime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Alarm alarm = mAlarmHelper.getAlarm();
        String startin = alarm.getStartinterval();
        int starthour = Integer.parseInt(alarm.getStarthr());
        int startmin = Integer.parseInt(alarm.getStartmin());
        if (startin.equalsIgnoreCase("am")) {
            if (starthour == 12)
                starthour = 0;
        } else {
            if (starthour == 12)
                starthour = 12;
            else
                starthour += 12;
        }
        int currenthour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentmin = calendar.get(Calendar.MINUTE);
        if(currenthour==starthour&&currentmin==startmin){
           // Log.i("!null jump actalarm",data);
            return true;
        }
        else{
            return false;
        }
    }
    public Date timenow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Alarm alarm = mAlarmHelper.getAlarm();
        //int frequency = Integer.parseInt(alarm.getFrq());
        int currenthour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentmin = calendar.get(Calendar.MINUTE);
        /*if (frequency + currentmin > 59) {
            int tempmin = frequency + currentmin;
            currentmin = tempmin - (60 * (tempmin / 60));
            currenthour += tempmin / 60;
        }
        else{
            currentmin = frequency + currentmin;
        }*/
        String currenthr = "";
        String currentmn = "";
        if(currenthour<10){
            currenthr = "0"+String.valueOf(currenthour);
        }
        else{
            currenthr = String.valueOf(currenthour);
        }
        if(currentmin<10){
            currentmn = "0"+String.valueOf(currentmin);
        }
        else{
            currentmn = String.valueOf(currentmin);
        }
        Date current = null;
        try {
            //All your parse Operations
            Log.i("current time",currenthr+"."+currentmn+".00");
            current = new SimpleDateFormat("HH.mm.ss").parse(currenthr+"."+currentmn+".00");
        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            //e.printStackTrace();
            // Log.i("current time"," can not carete");
        }
        return current;
    }
    public void setnextstart(Context context,String message){
        if((message==null||message.equalsIgnoreCase("act"))&&checkday()){
            Intent i = new Intent(context, actAlarm.class);
            Log.i("AlarmReceiver", "CanJump");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        }
        else{
            if (manager!= null) {
                manager.cancel(pendingIntent);
            }
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            Alarm alarm = mAlarmHelper.getAlarm();
            //Log.i("Day", sdf.format(calendar.getTime()) + " " + calendar.get(Calendar.DAY_OF_WEEK) + " " + alarm.getDay() + " "
            // + calendar.get(Calendar.HOUR_OF_DAY) + " " + calendar.get(Calendar.MINUTE));
            String startin = alarm.getStartinterval();
            int starthour = Integer.parseInt(alarm.getStarthr());
            int startmin = Integer.parseInt(alarm.getStartmin());
            if (startin.equalsIgnoreCase("am")) {
                if (starthour == 12)
                    starthour = 0;
            } else {
                if (starthour == 12)
                    starthour = 12;
                else
                    starthour += 12;
            }
            calendar.set(Calendar.HOUR_OF_DAY, starthour);
            calendar.set(Calendar.MINUTE, startmin);
            calendar.set(Calendar.SECOND, 0);
            long startUpTime = calendar.getTimeInMillis();
            if (System.currentTimeMillis() > startUpTime) {
                startUpTime = startUpTime + 24 * 60 * 60 * 1000;
            }
            Bundle mes = new Bundle();
            mes.putString("key", "act");
            alarmIntent.putExtras(mes);
            //context.sendBroadcast(alarmIntent);
            pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            manager.setExact(AlarmManager.RTC_WAKEUP, startUpTime, pendingIntent);
            Log.i("start", "set time");
        }

    }
}