package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
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
            Log.i("tempevent",temp);
            if(temp==null||temp.equalsIgnoreCase("event")){
                Log.i("Event","run notification");
                boolean check = true;
                if(!(Cache.getInstance().getData("switch")+"").equals("null")){
                    check = (boolean)Cache.getInstance().getData("switch");
                }
                Intent activityIntent = new Intent(context,EventactAlarm.class);
                if(check){
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,activityIntent,PendingIntent.FLAG_ONE_SHOT);
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    Notification notification = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.duck_yellow)
                            .setContentTitle("Move Alarm Notification")  //title
                            .setContentText("Get Event")    //detail
                                    //.setPriority(Notification.PRIORITY_HIGH)
                            .setTicker("Get Event")
                            .setSound(defaultSoundUri)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();

                    NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1000, notification);

                    PowerManager.WakeLock screenOn = ((PowerManager)context.getSystemService(context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"example" );
                    screenOn.acquire();
                }
                else{
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(activityIntent);
                }
                if (manager != null) {
                    manager.cancel(pendingIntent);
                }
            }
            else {
                Log.i("Event","set time");
                if (manager != null) {
                    manager.cancel(pendingIntent);
                }
                Log.i("Event","set time1");
                Intent alarmIntent = new Intent(context, EventReceiver.class);
                manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                //test
                calendar.setTimeInMillis(System.currentTimeMillis());
                //
                Log.i("Event","set time2");
                Log.i("Event",temp);
                DateFormat dateFormat = new SimpleDateFormat("HH.mm");
                Date infordate = null;
                try {
                    infordate = dateFormat.parse(temp.toString());
                } catch (ParseException e) {
                   // e.printStackTrace();
                }
                Log.i("Event","set time4");
                Log.i("Event",infordate+"");
                int hour = infordate.getHours();
                int min = infordate.getMinutes();
                Log.i("Event","set time5");
                //test
//                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
//                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
//                calendar.set(Calendar.SECOND, 0);
                //
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);
                long startUpTime = calendar.getTimeInMillis();
                if (System.currentTimeMillis() > startUpTime) {
                    Log.i("Event","set time6");
                    Log.i("Event",""+calendar.get(Calendar.HOUR_OF_DAY)+" "+(calendar.get(Calendar.MINUTE)+1));
                    startUpTime = startUpTime + 24 * 60 * 60 * 1000;
                }
                Bundle mes = new Bundle();
                mes.putString("event", "event");
                alarmIntent.putExtras(mes);
                //context.sendBroadcast(alarmIntent);
                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                manager.setExact(AlarmManager.RTC_WAKEUP, startUpTime, pendingIntent);
                Log.i("start", ""+hour+" "+min);
            }
    }
}
