package com.fatel.mamtv1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends android.support.v4.app.Fragment {
    private Spinner mStartHr;
    private Spinner mStartMin;
    private Spinner mStartAP;
    private Spinner mFinishHr;
    private Spinner mFinishMin;
    private Spinner mFinishAP;
    private CheckBox mChkboxMon;
    private CheckBox mChkboxTue;
    private CheckBox mChkboxWed;
    private CheckBox mChkboxThu;
    private CheckBox mChkboxFri;
    private CheckBox mChkboxSat;
    private CheckBox mChkboxSun;
    private String mdays="";
    private Spinner mFreq;


    private DBAlarmHelper mAlarmHelper;
    private int ID = -1;

    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        mStartHr = createSpinner(12, R.id.start_hr,true,view);
        mStartMin = createSpinner(60, R.id.start_min,false,view);
        mFinishHr = createSpinner(12, R.id.fin_hr, true, view);
        mFinishMin = createSpinner(60, R.id.fin_min, false, view);
        mStartAP = createSpinnerAmPm(R.id.start_AP, view);
        mFinishAP = createSpinnerAmPm(R.id.fin_AP, view);

        mChkboxSun = (CheckBox)view.findViewById(R.id.chkboxSun);
        mChkboxMon = (CheckBox)view.findViewById(R.id.chkboxMon);
        mChkboxTue = (CheckBox)view.findViewById(R.id.chkboxTue);
        mChkboxWed = (CheckBox)view.findViewById(R.id.chkboxWed);
        mChkboxThu = (CheckBox)view.findViewById(R.id.chkboxThu);
        mChkboxFri = (CheckBox)view.findViewById(R.id.chkboxFri);
        mChkboxSat = (CheckBox)view.findViewById(R.id.chkboxSat);

        mFreq = createSpinnerFrq(R.id.frq_min, view);
        mAlarmHelper = new DBAlarmHelper(getActivity());

        Button bt = (Button) view.findViewById(R.id.buttonSet);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mChkboxSun.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }
                if(mChkboxMon.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }
                if(mChkboxTue.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }
                if(mChkboxWed.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }
                if(mChkboxThu.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }
                if(mChkboxFri.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }
                if(mChkboxSat.isChecked()){
                    mdays += "1";
                }
                else{
                    mdays += "0";
                }

                //keep data
                    DatabaseAlarm alarm = new DatabaseAlarm();
                    alarm.setStarthr(mStartHr.getSelectedItem().toString());
                    alarm.setStartmin(mStartMin.getSelectedItem().toString());
                    alarm.setStophr(mFinishHr.getSelectedItem().toString());
                    alarm.setStopmin(mFinishMin.getSelectedItem().toString());
                    alarm.setStartinterval(mStartAP.getSelectedItem().toString());
                    alarm.setStopinterval(mFinishAP.getSelectedItem().toString());
                    alarm.setDay(mdays.toString());
                    alarm.setFrq(mFreq.getSelectedItem().toString());
                    if (ID == -1) {
                        mAlarmHelper.addAlarm(alarm);
                    } else {
                        alarm.setId(ID);
                        //mHelper.updateFriend(friend);
                    }
                    //finish();
                FragmentTransaction tx = getFragmentManager().beginTransaction();
                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                tx.replace(R.id.container, new MainFragment());
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        return view;
    }

    public Spinner createSpinner(int num,int id,boolean isHr,View view)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[num];
        ArrayAdapter<String> adapter;
        if(isHr) {
            for (int i = 1; i <= num; i++) {
                if(i<10)
                    numm[i - 1] = "0" + i;
                else
                    numm[i - 1] = "" + i;
            }
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(11);
        }
        else {
            for (int i = 0; i < num; i++) {
                if(i<10)
                    numm[i] = "0" + i;
                else
                    numm[i] = "" + i;
            }
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        return spinner;
    }

    public Spinner createSpinnerAmPm(int id,View view)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[]{"AM","PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    public Spinner createSpinnerFrq(int id,View view)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[]{"15","30","45","60","75","90"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }




}
