package com.fatel.mamtv1;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.widget.ImageView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;




public class Activity extends AppCompatActivity {

    TextView txtR;
    TextView txtA;
    TextView txtDes;
    ImageView imgView;
    AnimationDrawable frameAnimation;
    int count=0;
    //int[] imageId = new int[] {-1,-1,-1,-1};
    ArrayList<Image> img ;
    int exerciseImg;
    String exerciseDes;

    private static final String FORMAT = "%02d:%02d";
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Activity", "Can go");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        img = new ArrayList<>();
        txtR=(TextView) findViewById(R.id.rtime);
        txtA=(TextView) findViewById(R.id.atime);
        txtDes=(TextView) findViewById(R.id.des);
        imgView=(ImageView) findViewById(R.id.img);
        ActivityHandle activityHandle=new ActivityHandle();

        context=getApplicationContext();
        if(ImageCollection.size()==0){
            ImageCollection.initial(context);
        }

        Log.i("Activity","can go +1");
        img = ImageCollection.getImageById(activityHandle.getImageId());
        Log.i("Activity","can go +1"+img);
        Log.i("Activity","can go +2");
        exerciseImg=(img.get(count)).getImage();
        Log.i("Activity",""+(img.get(count)).getImage());
        Log.i("Activity","can go +3");
        exerciseDes=(img.get(count)).getDescription();
        Log.i("Activity",""+(img.get(count)).getDescription());
        Log.i("Activity","can go +4");
        txtDes.setText(exerciseDes);
        Log.i("Activity", "can go +5");
        imgView.setBackgroundResource(exerciseImg);
        Log.i("Activity", "can go +6");
        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) imgView.getBackground();
        Log.i("Activity","can go +7");
        // Start the animation (looped playback by default).
        frameAnimation.start();
        Log.i("Activity", "can go +8");

        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtR.setText("Remain Time   " + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                txtR.setText("Remain Time done!");
                frameAnimation.stop();
                count++;
                if(count<4) {

                    exerciseImg=(img.get(count)).getImage();
                    exerciseDes=(img.get(count)).getDescription();
                    txtDes.setText(exerciseDes);
                    imgView.setBackgroundResource(exerciseImg);
                    // Get the background, which has been compiled to an AnimationDrawable object.
                    frameAnimation = (AnimationDrawable) imgView.getBackground();
                    // Start the animation (looped playback by default).
                    frameAnimation.start();

                    start();
                }

            }
        }.start();



        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtA.setText("Activity Time   "+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                txtA.setText("Activity Time done!");

                //go to main
                Intent i1 = new Intent(Activity.this, PreShareActivity.class);
               // Bundle b1 = new Bundle();
                //b1.putExtra("key", "main");
                //i1.putExtra("key", "main");
                startActivity(i1);
                //Intent intent = new Intent(Activity.this, MainActivity.class);
                //startActivity(intent);

                //set frq
            }
        }.start();

    }
  /*  public void random(){
        for(int i=0;i<4;i++){
            boolean same=true;
            int x=0;
            while(same){
                same=false;
                x=(int)(Math.random() * 9);
                for(int j=0;j<i;j++) {
                    if (x == imageId[j]){
                        same=true;
                        break;
                    }
                }
            }
            imageId[i]=x;
        }

    }*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_, menu);
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
    }*/

    public void linkHome(View view)
    {
        Intent i1 = new Intent(Activity.this, MainActivity.class);
        // Bundle b1 = new Bundle();
        //b1.putExtra("key", "main");
        //i1.putExtra("key", "main");
        startActivity(i1);
        //sendBroadcast(i1);
        Intent i = new Intent(getBaseContext(), AlarmReceiver.class);

        Bundle b = new Bundle();
        b.putString("key", "first");
        i.putExtras(b);
        sendBroadcast(i);
        //AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //int interval = 60*1000*1;
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(Activity.this, 0, i, 0);
       // manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
    }
}
