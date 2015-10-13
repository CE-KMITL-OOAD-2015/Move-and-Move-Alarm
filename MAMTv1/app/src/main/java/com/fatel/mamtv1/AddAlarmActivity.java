package com.fatel.mamtv1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Monthon on 14/10/2558.
 */
public class AddAlarmActivity extends Activity {
    private Spinner mStartHr;
    private Spinner mStartMin;
    private Spinner mStartAP;
    private Spinner mFinishHr;
    private Spinner mFinishMin;
    private Spinner mFinishAP;

    private Spinner mFreq;
    //private Button mBtn;

    private DBAlarmHelper mAlarmHelper;
    private int ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alarm);

        mStartHr = (Spinner) findViewById(R.id.start_hr);
        mStartMin = (Spinner) findViewById(R.id.start_min);
        mStartAP = (Spinner) findViewById(R.id.start_AP);
        mFinishHr = (Spinner) findViewById(R.id.fin_hr);
        mFinishMin = (Spinner) findViewById(R.id.fin_min);
        mFinishAP = (Spinner) findViewById(R.id.fin_AP);

        mFinishAP = (Spinner) findViewById(R.id.frq_min);
        //mBtn = (Button)findViewById(R.id.;)
    }

}

