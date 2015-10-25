package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView header;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private DBAlarmHelper mAlarmHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarmHelper = new DBAlarmHelper(this);
       // Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
        if(mAlarmHelper.checkdata()==1){
            Log.i("Set Mainactivity","Canset");
            start();
        }
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.container, new MainFragment());
        tx.commit();

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        header = (TextView) findViewById(R.id.profile);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;

                Class fragmentClass;
                fragmentClass = ProfileFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();//getActivity()
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                tx.addToBackStack(null);
                tx.replace(R.id.container, fragment).commit();

                // Highlight the selected item, update the title, and close the drawer
                setTitle("Profile");
                mDrawerLayout.closeDrawers();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_home_fragment:
                fragmentClass = MainFragment.class;
                break;
            case R.id.nav_alarm_fragment:
                fragmentClass = AlarmFragment.class;
                break;
//            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
//                break;
            default:
                fragmentClass = MainFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();//getActivity()
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        tx.addToBackStack(null);
        tx.replace(R.id.container, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        if(menuItem.getTitle().equals("Home"))
            setTitle("Move Alarm");
        else
            setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    public void start(){
        /*manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        DatabaseAlarm alarm = mAlarmHelper.getAlarm();
        Log.i("Day",sdf.format(calendar.getTime())+" "+calendar.get(Calendar.DAY_OF_WEEK)+" "+alarm.getDay()+" "
                +calendar.get(Calendar.HOUR_OF_DAY)+" "+calendar.get(Calendar.MINUTE));
        String startin = alarm.getStartinterval();
        int starthour = Integer.parseInt(alarm.getStarthr());
        int startmin = Integer.parseInt(alarm.getStartmin());
        if(startin.equalsIgnoreCase("am")){
            if(starthour==12)
                starthour = 0;
        }
        else{
            if(starthour==12)
                starthour=12;
            else
                starthour+=12;
        }
        Log.i("main","set calendar "+calendar.get(Calendar.HOUR_OF_DAY)+" start "+starthour+" "
                +calendar.get(Calendar.MINUTE)+" "+startmin);
        calendar.set(Calendar.HOUR_OF_DAY, starthour);
        calendar.set(Calendar.MINUTE, startmin);
        calendar.set(Calendar.SECOND, 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/
        Bundle extras = getIntent().getExtras();
       // String temp = extras.getString("key");
        if (extras != null) {
            //String value = extras.getStringExtra("key");
        }
        if(extras==null){
            Log.i("extras","extras main == null");
            Intent alarmIntent = new Intent(MainActivity.this , AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            DatabaseAlarm alarm = mAlarmHelper.getAlarm();
            int frequency = Integer.parseInt(alarm.getFrq());
            //int interval = 60*1000*frequency;
            int interval = 60*1000*1;
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
        }
        else{
            Log.i("extras",extras.getString("key"));
            if(!extras.getString("key").equalsIgnoreCase("recount")){
                Intent i = new Intent(getBaseContext(), AlarmReceiver.class);
                Bundle b = new Bundle();
                b.putString("key", "recount");
                i.putExtras(b);
                sendBroadcast(i);
            }
            else if(extras.getString("key").equalsIgnoreCase("main")){
                Log.i("act to main ",extras.getString("key"));
            }
        }
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * Integer.parseInt(alarm.getFrq()), pendingIntent);
    }


}