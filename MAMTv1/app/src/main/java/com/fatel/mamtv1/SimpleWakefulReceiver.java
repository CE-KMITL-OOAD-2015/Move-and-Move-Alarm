package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class SimpleWakefulReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private DBAlarmHelper  mAlarmHelper;
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        // This is the Intent to deliver to our service.
        //alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //Intent intentalarm = new Intent(context, SimpleWakefulReceiver.class);
        //alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DatabaseAlarm setalarm =   mAlarmHelper.getAlarm();
        if(setalarm.getStartinterval().equalsIgnoreCase("am")){
            if(Integer.parseInt(setalarm.getStarthr())!=12)
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(setalarm.getStarthr()));
            else
                calendar.set(Calendar.HOUR_OF_DAY, 0);
        }
        else{
            if(Integer.parseInt(setalarm.getStarthr())==12)
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(setalarm.getStarthr()));
            else
                calendar.set(Calendar.HOUR_OF_DAY, 12+Integer.parseInt(setalarm.getStarthr()));
        }
        calendar.set(Calendar.MINUTE, Integer.parseInt(setalarm.getStartmin()));*/




        //Intent service = new Intent(context, Activity.class);

        // Start the service, keeping the device awake while it is launching.

        Log.i("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
        Bundle extras = intent.getExtras();
        //if(extras == null){
        Intent i = new Intent(context,Activity.class);
        Log.i("SimpleWakefulReceiver", "CanJump");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        //}
        while(extras==null){
            Log.i("wait","activity");
        }
        Log.i("finish","activity is finished");
        Intent alarmIntent = new Intent(context , SimpleWakefulReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 6000;
        manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
    }
}