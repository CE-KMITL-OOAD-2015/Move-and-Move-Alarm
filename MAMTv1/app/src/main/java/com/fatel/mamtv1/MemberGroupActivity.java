package com.fatel.mamtv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MemberGroupActivity extends AppCompatActivity {
    TextView user1;
    TextView user2;
    TextView user3;
    TextView user4;
    TextView user5;
    TextView user6;
    TextView user7;
    TextView user8;
    TextView user9;
    TextView user10;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_group);
        user1 = (TextView)findViewById(R.id.textView22);
        user2 = (TextView)findViewById(R.id.textView32);
        user3 = (TextView)findViewById(R.id.textView42);
        user4 = (TextView)findViewById(R.id.textView52);
        user5 = (TextView)findViewById(R.id.textView62);
        user6 = (TextView)findViewById(R.id.textView72);
        user7 = (TextView)findViewById(R.id.textView82);
        user8 = (TextView)findViewById(R.id.textView92);
        user9 = (TextView)findViewById(R.id.textView102);
        user10 = (TextView)findViewById(R.id.textView112);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_member_group, menu);
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
