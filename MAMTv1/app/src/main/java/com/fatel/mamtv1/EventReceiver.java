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

/**
 * Created by Moobi on 18-Nov-15.
 */
public class EventReceiver extends BroadcastReceiver {
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String temp = extras.getString("event");
        if(temp==null){
            Log.i("tempevent", "temp null");
        }
        else{
            Log.i("tempevent",temp);
            if(temp==null||temp.equalsIgnoreCase("event")){
//                Intent i = new Intent(context, actAlarm.class);
//                Log.i("AlarmReceiver", "CanJump");
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//                Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
                if (manager != null) {
                    manager.cancel(pendingIntent);
                }
            }
            else {
                if (manager != null) {
                    manager.cancel(pendingIntent);
                }
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat working2 = new SimpleDateFormat("hh:mm");
                Date infordate = null;
                try {
                    infordate = working2.parse(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int hour = infordate.getHours();
                int min = infordate.getMinutes();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);
                long startUpTime = calendar.getTimeInMillis();
                if (System.currentTimeMillis() > startUpTime) {
                    startUpTime = startUpTime + 24 * 60 * 60 * 1000;
                }
                Bundle mes = new Bundle();
                mes.putString("key", "event");
                alarmIntent.putExtras(mes);
                //context.sendBroadcast(alarmIntent);
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                manager.setExact(AlarmManager.RTC_WAKEUP, startUpTime, pendingIntent);
                Log.i("start", "set time");
            }
        }
    }
}
