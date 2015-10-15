package com.fatel.mamtv1;


import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
import android.widget.ImageView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;

import static com.fatel.mamtv1.Constants.DESCRIPTION;
import static com.fatel.mamtv1.Constants.IMAGE;
import static com.fatel.mamtv1.Constants.TABLE_NAME;
import static com.fatel.mamtv1.Constants._ID;


public class Activity extends AppCompatActivity {

    TextView txtR;
    TextView txtA;
    TextView txtDes;
    ImageView img;
    AnimationDrawable frameAnimation;
    int count=0;
    int[] imageId = new int[] {-1,-1,-1,-1};
    //int[] images = new int[] {R.drawable.ex1, R.drawable.ex2, R.drawable.ex3, R.drawable.ex4 ,R.drawable.ex5,R.drawable.ex6,R.drawable.ex7,R.drawable.ex8,R.drawable.ex9};
    int imgS;
    int a =0;
    String description;
    String des;
    int readImg=0;

    private static final String FORMAT = "%02d:%02d";

    private static String[] COLUMNS = { _ID, IMAGE, DESCRIPTION};
    private static String ORDER_BY = IMAGE + " DESC";

    private ActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        helper = new ActivityHelper(this);


        txtR=(TextView) findViewById(R.id.rtime);
        txtA=(TextView) findViewById(R.id.atime);
        txtDes=(TextView) findViewById(R.id.des);
        img=(ImageView) findViewById(R.id.img);

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

        insertImage();


        try {
            //อ่านข้อมูล
            SQLiteDatabase db2 = helper.getReadableDatabase();
            Cursor cursor = db2.query(TABLE_NAME, COLUMNS, null, null, null, null,null);
            //Cursor cursor =  db2.rawQuery("select * from " + TABLE_NAME + " where " + _ID + "=" + 0  , null);
            while(cursor.moveToNext()) {
                int id = (int)(cursor.getLong(0));
                readImg = cursor.getInt(1);
                des = cursor.getString(2);
                if(id==imageId[a]) {
                    a++;
                    break;
                }
            }

        }
        finally {
            helper.close();
        }
        description=des;
        imgS= readImg;
        txtDes.setText(description);



        // img.setImageResource(images[imageId[count]]);
        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        img.setBackgroundResource(imgS);

        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) img.getBackground();

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

                    try {
                        //อ่านข้อมูล
                        SQLiteDatabase db2 = helper.getReadableDatabase();
                        Cursor cursor = db2.query(TABLE_NAME, COLUMNS, null, null, null, null,null);
                        //Cursor cursor =  db2.rawQuery("select * from " + TABLE_NAME + " where " + _ID + "=" + 0  , null);
                        while(cursor.moveToNext()) {
                            int id = (int)(cursor.getLong(0));
                            readImg = cursor.getInt(1);
                            des = cursor.getString(2);
                            if(id==imageId[a]) {
                                a++;
                                break;
                            }
                        }

                    }
                    finally {
                        helper.close();
                    }
                    description=des;
                    imgS= readImg;
                    txtDes.setText(description);
                    img.setBackgroundResource(imgS);

                    // Get the background, which has been compiled to an AnimationDrawable object.
                    frameAnimation = (AnimationDrawable) img.getBackground();

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
            }
        }.start();


    }
    public void insertImage(){
        this.deleteDatabase("images.db");
        try {
            //เพิ่มข้อมูลลงฐานข้อมูล
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(IMAGE, R.drawable.ex1);
            values.put(DESCRIPTION, "หมุนคอไปทางขวาจนสุด แล้วหมุนกลับ");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex2);
            values.put(DESCRIPTION, "ยกแขนสลับซ้ายขวา");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex3);
            values.put(DESCRIPTION, "หันไปทางขวาจนสุด แล้วหันไปทางซ้าย");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex4);
            values.put(DESCRIPTION, "กดคอลง  แล้วดันคอขึ้น");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex5);
            values.put(DESCRIPTION, "กางขา ย่อเข่าลงสลับซ้ายขวา");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex6);
            values.put(DESCRIPTION, "หมุนข้อเท้า สลับซ้ายขวา");
            db.insertOrThrow(TABLE_NAME, null, values);
            values.put(IMAGE, R.drawable.ex7);
            values.put(DESCRIPTION, "ยกขาขึ้นด้านหลัง สลับซ้ายขวา");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex8);
            values.put(DESCRIPTION, "ยกแขนขึ้นดันข้อศอกอีกข้าง สลับซ้ายขวา");
            db.insertOrThrow(TABLE_NAME, null, values);

            values.put(IMAGE, R.drawable.ex9);
            values.put(DESCRIPTION, "ยืดแขน สลับซ้ายขวา");
            db.insertOrThrow(TABLE_NAME, null, values);




        }
        finally {
            helper.close();
        }
    }
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
}
