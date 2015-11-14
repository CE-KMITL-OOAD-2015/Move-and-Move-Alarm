package com.fatel.mamtv1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PostureActivity extends AppCompatActivity {
    int count=0;
    TextView txtDes;
    ImageView imgView;
    AnimationDrawable frameAnimation;
    Button previous;
    Button home;
    Button next;
    //int[] imageId = new int[] {-1,-1,-1,-1};
    ArrayList<Image> img ;
    int exerciseImg;
    String exerciseDes;
    private static final String FORMAT = "%02d:%02d";
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            count = bundle.getInt("value");
        }
        setContentView(R.layout.activity_posture);
        img = new ArrayList<>();
        txtDes=(TextView) findViewById(R.id.txt);
        imgView=(ImageView) findViewById(R.id.img);
        home = (Button) findViewById(R.id.homebtn);
        next = (Button) findViewById(R.id.nextbtn);
        previous = (Button) findViewById(R.id.previousbtn);
        context=getApplicationContext();
        if(ImageCollection.size()==0){
            ImageCollection.initial(context);
        }
        int[] imageId = new int[] {0,1,2,3,4,5,6,7,8};
        Log.i("Activity", "can go +1");
        img = ImageCollection.getImageById(imageId);
        Log.i("Activity","can go +1"+img);
        Log.i("Activity","can go +2");
        exerciseImg=(img.get(count)).getImage();
        Log.i("Activity",""+(img.get(count)).getImage());
        Log.i("Activity","can go +3");
        exerciseDes=(img.get(count)).getDescription();
        Log.i("Activity", "" + (img.get(count)).getDescription());
        Log.i("Activity","can go +4");
        txtDes.setText(exerciseDes);
        Log.i("Activity", "can go +5");
        imgView.setBackgroundResource(exerciseImg);
        Log.i("Activity", "can go +6");
        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) imgView.getBackground();
        Log.i("Activity", "can go +7");
        // Start the animation (looped playback by default).
        frameAnimation.start();
        Log.i("Activity", "can go +8");

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                count--;
                if (count >= 0) {
                    frameAnimation.stop();
                    exerciseImg = (img.get(count)).getImage();
                    exerciseDes = (img.get(count)).getDescription();
                    txtDes.setText(exerciseDes);
                    imgView.setBackgroundResource(exerciseImg);
                    // Get the background, which has been compiled to an AnimationDrawable object.
                    frameAnimation = (AnimationDrawable) imgView.getBackground();
                    // Start the animation (looped playback by default).
                    frameAnimation.start();
                } else if (count == -1) {
                    count = 0;
                    previous.setEnabled(false);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous.setEnabled(true);
                count++;
                if (count < 9) {
                    frameAnimation.stop();
                    exerciseImg = (img.get(count)).getImage();
                    exerciseDes = (img.get(count)).getDescription();
                    txtDes.setText(exerciseDes);
                    imgView.setBackgroundResource(exerciseImg);
                    // Get the background, which has been compiled to an AnimationDrawable object.
                    frameAnimation = (AnimationDrawable) imgView.getBackground();
                    // Start the animation (looped playback by default).
                    frameAnimation.start();
                }
                else if(count == 9)
                {
                    count = 8;
                    next.setEnabled(false);
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_posture, menu);
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
}
