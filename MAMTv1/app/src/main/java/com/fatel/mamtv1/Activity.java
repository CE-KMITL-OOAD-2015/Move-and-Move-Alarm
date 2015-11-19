package com.fatel.mamtv1;


import android.content.Context;

import android.content.Intent;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;



public class Activity extends AppCompatActivity {

    TextView txtR;
    TextView txtA;
    TextView txtDes;
    ImageView imgView;
    AnimationDrawable frameAnimation;
    int count=0;
    ArrayList<Posture> img ;
    int exerciseImg;
    String exerciseDes;

    private static final String FORMAT = "%02d:%02d";
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        img = activityHandle.getRandomPosture(this);

        exerciseImg=(img.get(count)).getImage();

        exerciseDes=(img.get(count)).getDescription();

        txtDes.setText(exerciseDes);

        imgView.setBackgroundResource(exerciseImg);

        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) imgView.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();
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
                Intent i1 = new Intent(Activity.this, Camera.class);
                startActivity(i1);
                requesAddscore();
            }
        }.start();

    }

    public void linkHome(View view)
    {
        //history
        History history = History.findHistory(UserManage.getInstance(this).getCurrentIdUser(),this);
        history.subaccept(1);
        history.addcancel(1);
        history.save(this);

        Intent i1 = new Intent(Activity.this, MainActivity.class);

        startActivity(i1);
        //sendBroadcast(i1);
        Intent i = new Intent(getBaseContext(), AlarmReceiver.class);

        Bundle b = new Bundle();
        b.putString("key", "first");
        i.putExtras(b);
        sendBroadcast(i);

    }

    public void requesAddscore()
    {
        final Converter converter = Converter.getInstance();
        String url = HttpConnector.URL + "user/increaseScore";
        StringRequest eventRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated


                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            makeToast("Sync process completed.");
                            UserManage.getInstance(Activity.this).addScore(1, Activity.this);
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated

                makeToast("Cannot connect to server. Please check the Internet setting.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>(); //create map to keep variables
                HashMap<String, Object> JSON = new HashMap<>();
                HashMap<String, Object> userData = UserManage.getInstance(Activity.this).getCurrentUser().getGeneralValues();
                int point = 1;

                JSON.put("score", point);
                JSON.put("user", userData);
                JSON.put("description", "Done activity! Get 1 point.");

                map.put("JSON", converter.HashMapToJSON(JSON));

                return map;
            }
        };

        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(eventRequest);
    }

    public void makeToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
