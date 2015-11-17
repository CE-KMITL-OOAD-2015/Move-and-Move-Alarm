package com.fatel.mamtv1;


import android.app.AlarmManager;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends android.support.v4.app.Fragment {
    private DBAlarmHelper mAlarmHelper;
    CircleImageView propic;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setDay(view);
        mAlarmHelper = new DBAlarmHelper(getActivity());
        setTextAlarm(view,mAlarmHelper);
        propic = (CircleImageView)view.findViewById(R.id.profile_image_f);
        Log.i("xx", propic.toString());
        propic.setVisibility(View.VISIBLE);
        Log.i("xx", "do?");
        Glide.with(this).load("https://graph.facebook.com/" + UserManage.getInstance((Context)Cache.getInstance().getData("MainActivityContext")).getCurrentFacebookId() + "/picture?type=large").into(propic);

        return view;
    }
    public void setDay(View view){
        TextView day = (TextView) view.findViewById(R.id.daycurrent);
        String days = getCurrentDay();
        day.setText(days);
        //day.setVisibility(View.VISIBLE);
        Log.i("Day",days);
    }
    public String getCurrentDay(){
        Calendar now = Calendar.getInstance();
        String[] strDays = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday", "Saturday"};
        //Log.i("Day",now.get(Calendar.DAY_OF_WEEK)+"");
        return strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
    }
    public void setTextAlarm(View view,DBAlarmHelper alarmHelper){
        TextView hrstart = (TextView)view.findViewById(R.id.starthr);
        TextView minstart = (TextView)view.findViewById(R.id.startmin);
        TextView hrstop = (TextView)view.findViewById(R.id.hrstop);
        TextView minstop = (TextView)view.findViewById(R.id.minstop);
        TextView intervalstart = (TextView)view.findViewById(R.id.intervalstart);
        TextView intervalstop = (TextView)view.findViewById(R.id.intervalstop);
        if(alarmHelper.checkdata()==1){
            Alarm alarm = new Alarm();
            alarm = alarmHelper.getAlarm();
            hrstart.setText(alarm.getStarthr());
            minstart.setText(alarm.getStartmin());
            hrstop.setText(alarm.getStophr());
            minstop.setText(alarm.getStopmin());
            intervalstart.setText(alarm.getStartinterval());
            intervalstop.setText(alarm.getStopinterval());
        }
        else{
            hrstart.setText("--");
            minstart.setText("--");
            hrstop.setText("--");
            minstop.setText("--");
            intervalstart.setText("");
            intervalstop.setText("");
        }
    }
}
