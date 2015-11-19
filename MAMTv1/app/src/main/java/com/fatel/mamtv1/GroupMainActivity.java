package com.fatel.mamtv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GroupMainActivity extends AppCompatActivity {

    TextView adminName;
    TextView groupName;
    TextView amountMember;
    TextView groupCode;
    TextView groupScore;
    TextView getEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);
        groupCode = (TextView)findViewById(R.id.textView22);
        adminName = (TextView)findViewById(R.id.textView42);
        groupName = (TextView)findViewById(R.id.textView32);
        amountMember = (TextView)findViewById(R.id.amount);
        groupScore = (TextView)findViewById(R.id.score);
        getEvent = (TextView)findViewById(R.id.getEvent);

        Converter converter = Converter.getInstance();

        try {
            HashMap<String, Object> groupData = (HashMap<String, Object>) Cache.getInstance().getData("groupData");
            HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(groupData.get("admin")));
            HashMap<String, Object> eventData = (HashMap<String, Object>) Cache.getInstance().getData("eventData");

            DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
            Date date = dateFormat.parse(converter.toString(eventData.get("time")));
            dateFormat = new SimpleDateFormat("hh : mm  aa");

            String groupID = "" + converter.toInt(groupData.get("id"));
            String userName = converter.toString(userData.get("userName"));
            String nameOfAdmin = (userName == null) ? converter.toString(userData.get("facebookFirstName")) : userName;
            int addedDigit = 4 - groupID.length();
            String code = String.format("%0" + addedDigit + "d%s", 0, groupID);
            groupCode.setText(code);
            groupName.setText(converter.toString(groupData.get("name")));

            adminName.setText(nameOfAdmin);
            amountMember.setText("" + converter.toInt(groupData.get("amountMember")));
            groupScore.setText("" + converter.toInt(groupData.get("score")));
            getEvent.setText(dateFormat.format(date));
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_main, menu);
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

    public void linkMember(View view)
    {
        Intent intent = new Intent(this, MemberGroupActivity.class);
        startActivity(intent);
    }
}
