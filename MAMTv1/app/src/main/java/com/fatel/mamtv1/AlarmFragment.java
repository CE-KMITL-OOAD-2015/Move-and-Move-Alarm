package com.fatel.mamtv1;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
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
<<<<<<< HEAD
import android.widget.Toast;
=======
import android.widget.TextView;
>>>>>>> feature/Clock


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

    private PendingIntent pendingIntent;
    private AlarmManager manager;
    public AlarmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        mAlarmHelper = new DBAlarmHelper(getActivity());
Log.i("xx",mAlarmHelper.checkdata()+"");
        mStartHr = createSpinner(12, R.id.start_hr,true,view,mAlarmHelper,true);
        mStartMin = createSpinner(60, R.id.start_min,false,view,mAlarmHelper,true);
        mFinishHr = createSpinner(12, R.id.fin_hr, true, view,mAlarmHelper,false);
        mFinishMin = createSpinner(60, R.id.fin_min, false, view, mAlarmHelper, false);
        mStartAP = createSpinnerAmPm(R.id.start_AP, view,mAlarmHelper,true);
        mFinishAP = createSpinnerAmPm(R.id.fin_AP, view,mAlarmHelper,false);
        mChkboxSun = (CheckBox)view.findViewById(R.id.chkboxSun);
        mChkboxMon = (CheckBox)view.findViewById(R.id.chkboxMon);
        mChkboxTue = (CheckBox)view.findViewById(R.id.chkboxTue);
        mChkboxWed = (CheckBox)view.findViewById(R.id.chkboxWed);
        mChkboxThu = (CheckBox)view.findViewById(R.id.chkboxThu);
        mChkboxFri = (CheckBox)view.findViewById(R.id.chkboxFri);
        mChkboxSat = (CheckBox)view.findViewById(R.id.chkboxSat);
        mFreq = createSpinnerFrq(R.id.frq_min, view,mAlarmHelper);

        //check checkbox tick
        if(mAlarmHelper.checkdata()==1) {
            Alarm alarm = mAlarmHelper.getAlarm();
            String day = alarm.getDay();
            if(day.substring(0,1).equals("1"))
                mChkboxSun.setChecked(true);
            if(day.substring(1,2).equals("1"))
                mChkboxMon.setChecked(true);
            if(day.substring(2,3).equals("1"))
                mChkboxTue.setChecked(true);
            if(day.substring(3,4).equals("1"))
                mChkboxWed.setChecked(true);
            if(day.substring(4,5).equals("1"))
                mChkboxThu.setChecked(true);
            if(day.substring(5,6).equals("1"))
                mChkboxFri.setChecked(true);
            if(day.substring(6,7).equals("1"))
                mChkboxSat.setChecked(true);
        }

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
                    Alarm alarm = new Alarm();
                    alarm.setId(1);
                    alarm.setStarthr(mStartHr.getSelectedItem().toString());
                    alarm.setStartmin(mStartMin.getSelectedItem().toString());
                    alarm.setStophr(mFinishHr.getSelectedItem().toString());
                    alarm.setStopmin(mFinishMin.getSelectedItem().toString());
                    alarm.setStartinterval(mStartAP.getSelectedItem().toString());
                    alarm.setStopinterval(mFinishAP.getSelectedItem().toString());
                    alarm.setDay(mdays.toString());
                    alarm.setFrq(mFreq.getSelectedItem().toString());
                    if (ID == -1 && (mAlarmHelper.checkdata()==0)) {
                        mAlarmHelper.addAlarm(alarm);
                    } else {
                        mAlarmHelper.UpdateAlarm(alarm);
                    }
                Intent mServiceIntent = new Intent(getActivity(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(),0,mServiceIntent,0);
                start();
//                FragmentTransaction tx = getFragmentManager().beginTransaction();
//                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                tx.replace(R.id.container, new MainFragment());
//                tx.addToBackStack(null);
                Toast.makeText(getActivity(), "SetAlarm Successful", Toast.LENGTH_SHORT).show();
//                tx.commit();
            }
        });
        return view;
    }

    public Spinner createSpinner(int num,int id,boolean isHr,View view,DBAlarmHelper mAlarmHelper,boolean isStart)
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
            if(mAlarmHelper.checkdata()==0)
                spinner.setSelection(11);
            else
            {
                Alarm alarm = mAlarmHelper.getAlarm();
                if(isStart) {
                    String hr = alarm.getStarthr();
                    spinner.setSelection(Integer.parseInt(hr) - 1);
                }
                else
                {
                    String hr = alarm.getStophr();
                    spinner.setSelection(Integer.parseInt(hr)-1);
                }
            }
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
            if(mAlarmHelper.checkdata()==1)
            {
                Alarm alarm = mAlarmHelper.getAlarm();
                if(isStart) {
                    String min = alarm.getStartmin();
                    spinner.setSelection(Integer.parseInt(min));
                }
                else
                {
                    String min = alarm.getStopmin();
                    spinner.setSelection(Integer.parseInt(min));
                }
            }
        }
        return spinner;
    }

    public Spinner createSpinnerAmPm(int id,View view,DBAlarmHelper mAlarmHelper,boolean isStart)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[]{"AM","PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(mAlarmHelper.checkdata()==1)
        {
            Alarm alarm = mAlarmHelper.getAlarm();
            if(isStart) {
                String ap = alarm.getStartinterval();
                if(ap.equals("AM"))
                    spinner.setSelection(0);
                else
                    spinner.setSelection(1);
            }
            else
            {
                String ap = alarm.getStopinterval();
                if(ap.equals("AM"))
                    spinner.setSelection(0);
                else
                    spinner.setSelection(1);
            }
        }
        return spinner;
    }

    public Spinner createSpinnerFrq(int id,View view,DBAlarmHelper mAlarmHelper)
    {
        Spinner spinner = (Spinner)view.findViewById(id);
        String[] numm = new String[]{"15","30","45","60","75","90"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numm);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(mAlarmHelper.checkdata()==1)
        {
            Alarm alarm = mAlarmHelper.getAlarm();
            String frq = alarm.getFrq();
            int frqint = -1;
            if(frq.equals("15"))
                frqint = 0;
            else if(frq.equals("30"))
                frqint = 1;
            else if(frq.equals("45"))
                frqint = 2;
            else if(frq.equals("60"))
                frqint = 3;
            else if(frq.equals("75"))
                frqint = 4;
            else if(frq.equals("90"))
                frqint = 5;
            spinner.setSelection(frqint);
        }
        return spinner;
    }
    public void start() {
        /*manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
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
        Log.i("fragment",calendar.get(Calendar.HOUR_OF_DAY)+" start "+starthour+" "
                +calendar.get(Calendar.MINUTE)+" "+startmin);
        calendar.set(Calendar.HOUR_OF_DAY, starthour);
        calendar.set(Calendar.MINUTE, startmin);
        calendar.set(Calendar.SECOND, 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);*/
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putString("key", "set");
        i.putExtras(b);
        getActivity().sendBroadcast(i);
    }
    /*public void cancel(){
            if (manager!= null&&checkcanceltime()) {
                manager.cancel(pendingIntent);
            }
            if (checkcanceltime()==false) {
                start();
            }
            else{
                manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                int interval = 60*1000*10;
                manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
            }
    }
    public boolean checkcanceltime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String stopin = alarm.getStopinterval();
        int stophour = Integer.parseInt(alarm.getStophr());
        int stopmin = Integer.parseInt(alarm.getStopmin());
        int frequency = Integer.parseInt(alarm.getFrq());
        if(stopin.equalsIgnoreCase("am")){
            if(stophour==12)
                stophour = 0;
        }
        else{
            if(stophour==12)
                stophour=12;
            else
                stophour+=12;
        }
        if(frequency+stopmin>59){
            int tempmin = frequency+stopmin;
            stopmin = tempmin-(60*(tempmin/60));
            stophour += tempmin/60;
        }
        int currenthour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentmin = calendar.get(Calendar.MINUTE);
        if(frequency+currentmin>59){
            int tempmin = frequency+currentmin;
            currentmin = tempmin-(60*(tempmin/60));
            currenthour += tempmin/60;
        }
        if(Integer.parseInt(alarm.getDay().substring(calendar.get(Calendar.DAY_OF_WEEK)-1,calendar.get(Calendar.DAY_OF_WEEK)))==0){
            return true;
        }
        else if(currenthour<=stophour&&currentmin<=stopmin){
            return true;
        }
        else{
            return false;
        }
    }*/

}
