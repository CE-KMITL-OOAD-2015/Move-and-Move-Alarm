package com.fatel.testsqlite;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAlarmActivity extends AppCompatActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mTel;
    private EditText mEmail;
    private EditText mDescription;
    private Button mButtonOK;

    private DBHelper mHelper;

    private int ID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_alarm);

        mFirstName = (EditText) findViewById(R.id.add_first_name);
        mLastName = (EditText) findViewById(R.id.add_last_name);
        mTel = (EditText) findViewById(R.id.add_tel);
        mEmail = (EditText) findViewById(R.id.add_email);
        mDescription = (EditText) findViewById(R.id.add_description);
        mButtonOK = (Button) findViewById(R.id.button_submit);

        mHelper = new DBHelper(this);

        mButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(AddAlarmActivity.this);
                builder.setTitle(getString(R.string.add_data_title));
                builder.setMessage(getString(R.string.add_data_message));

                builder.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Alarm alarm = new Alarm();
                                alarm.setStartHr(mFirstName.getText().toString());
                                alarm.setStartMin(mLastName.getText().toString());
                                alarm.setEndHr(mTel.getText().toString());
                                alarm.setEndMin(mEmail.getText().toString());
                                alarm.setFrq(mDescription.getText().toString());
                                alarm.setDay(mDescription.getText().toString());

                                if (ID == -1) {
                                    mHelper.addAlarm(alarm);
                                } else {
                                    alarm.setId(ID);
                                    //mHelper.updateFriend(friend);
                                }
                                finish();
                            }
                        });

                builder.setNegativeButton(getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_alarm, menu);
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
        finish();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        return super.onOptionsItemSelected(item);
    }
}
