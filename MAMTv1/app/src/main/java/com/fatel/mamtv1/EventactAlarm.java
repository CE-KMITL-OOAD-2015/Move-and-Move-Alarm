package com.fatel.mamtv1;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class EventactAlarm extends AppCompatActivity {

    private Vibrator v;
    private MediaPlayer m;
    private Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventact_alarm);
        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        long[] pattern = {0, 500, 1000};
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        m = MediaPlayer.create(this,notification);
        // m.reset();
        //m = MediaPlayer.create(this,notification);
        m.setLooping(true);
        m.start();
        v.vibrate(pattern, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void linkActivity(View view){
        //history
        HistorygroupHelper mhistorygroupHelper = new HistorygroupHelper(this);
        Historygroup historygroup = mhistorygroupHelper.getHistoryGroup(UserManage.getInstance(this).getCurrentIdGroup());
        if(historygroup!=null){
            historygroup.addaccept(1);
            historygroup.save(this);
        }
        else{
            Log.i("Usergroup","can't addaccept");
        }
        Log.i("historyacc", UserManage.getInstance(this).getCurrentIdGroup() + "");
        Log.i("historyacc",historygroup.getNumberOfAccept()+"");
        Log.i("historyacc",historygroup.gettotal()+"");
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
        v.cancel();
        m.reset();
    }

    public void linkHome(View view){
        //history
        HistorygroupHelper mhistorygrouphelper = new HistorygroupHelper(this);
        Historygroup historygroup = mhistorygrouphelper.getHistoryGroup(UserManage.getInstance(this).getCurrentIdGroup());
        if(historygroup!=null){
            historygroup.addcancel(1);
            historygroup.save(this);
        }
        else{
            Log.i("Usergroup","can't cancel");
        }
        Log.i("historycancel", UserManage.getInstance(this).getCurrentIdGroup()+ "");
        Log.i("historycancel", historygroup.getCancelEvent() + "");
        Log.i("historycancel", historygroup.gettotal() + "");
        Intent i1 = new Intent(EventactAlarm.this, MainActivity.class);
        // Bundle b1 = new Bundle();
        //b1.putExtra("key", "main");
        //i1.putExtra("key", "main");
        startActivity(i1);
        //sendBroadcast(i1);
        v.cancel();
        m.reset();
        //AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 60*1000*1;
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(actAlarm.this, 0, i, 0);
        //manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
    }

}
