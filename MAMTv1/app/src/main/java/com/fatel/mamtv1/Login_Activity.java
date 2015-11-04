package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login_Activity extends AppCompatActivity {
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private DBAlarmHelper mAlarmHelper;
    UserManage mUserManage;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fatel.mamtv1",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Log.i("loggedin", "go");
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        Log.i("loggedin", "callback");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        linkWithFB();
                    }

                    @Override
                    public void onCancel() {
                        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                        Profile profile = Profile.getCurrentProfile();
                        Log.i("loggedin", loggedIn + "");
                        if (loggedIn && (profile != null)) {
                            //Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            // startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(FacebookException e) {
                        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                        Profile profile = Profile.getCurrentProfile();
                        Log.i("loggedin", loggedIn + "");
                        if (loggedIn && (profile != null)) {
                            // Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            // startActivity(intent);
                        }
                    }
                });

        setContentView(R.layout.login_layout);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                linkWithFB();
            }
        };
        mAlarmHelper = new DBAlarmHelper(this);
        mUserManage = new UserManage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_, menu);
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

    public void linkMain(View view)
    {
        EditText username;
        EditText password;

        username = (EditText)findViewById(R.id.enter_username);
        password = (EditText)findViewById(R.id.enter_password);

        boolean isSuccess = mUserManage.checkUser(username.getText().toString(), password.getText().toString());

        if(username.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(password.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (isSuccess/*ifSuccess ใช้เช็คว่า username กับ password ตรงกับฐานข้อมูลรึเปล่า*/) {
            mUserManage.loginUser(username.getText().toString(),password.getText().toString(),this);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            // ดูว่ามีการตั้งค่าเวลาหรือเปล่า
            if(mAlarmHelper.checkdata()==1){
                start();
            }
        }
        else{
            Toast toast = Toast.makeText(this, "Username or Password incorrect.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void start(){
        Intent i = new Intent(getBaseContext(), AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putString("key", "set");
        i.putExtras(b);
        sendBroadcast(i);
        /*manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

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
       // Bundle extras = getIntent().getExtras();
        // String temp = extras.getString("key");
       // if (extras != null) {
            //String value = extras.getStringExtra("key");
       // }
        /*if(extras==null){
            Log.i("extras","extras main == null");
            Intent alarmIntent = new Intent(Login_Activity.this , AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(Login_Activity.this, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Alarm alarm = mAlarmHelper.getAlarm();
            int frequency = Integer.parseInt(alarm.getFrq());
            //int interval = 60*1000*frequency;
            int interval = 60*1000*1;
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
        }*/
        /*else{
            Log.i("extras",extras.getString("key"));
            if(!extras.getString("key").equalsIgnoreCase("recount")){

            }
            else if(extras.getString("key").equalsIgnoreCase("main")){
                Log.i("act to main ",extras.getString("key"));
            }
        }*/
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * Integer.parseInt(alarm.getFrq()), pendingIntent);
    }

    public void linkWithFB()
    {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();
        //Log.i("loggedin", loginResult + " go UI");
        if (loggedIn && (profile != null)) {
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
