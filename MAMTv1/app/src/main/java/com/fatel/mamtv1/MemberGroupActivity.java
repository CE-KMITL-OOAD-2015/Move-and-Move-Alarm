package com.fatel.mamtv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberGroupActivity extends AppCompatActivity {
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_group);
        Converter converter = Converter.getInstance();
        ArrayList<TextView> users = new ArrayList<>();
        users.add((TextView) findViewById(R.id.textView22));
        users.add((TextView)findViewById(R.id.textView32));
        users.add((TextView)findViewById(R.id.textView42));
        users.add((TextView)findViewById(R.id.textView52));
        users.add((TextView)findViewById(R.id.textView62));
        users.add((TextView)findViewById(R.id.textView72));
        users.add((TextView)findViewById(R.id.textView82));
        users.add((TextView)findViewById(R.id.textView92));
        users.add((TextView)findViewById(R.id.textView102));
        users.add((TextView)findViewById(R.id.textView112));

        HashMap<String, Object> groupData = (HashMap<String, Object>) Cache.getInstance().getData("groupData");
        ArrayList<HashMap<String, Object>> memberData = Converter.getInstance().toHashMapArrayList(groupData.get("members"));

        Log.i("Member", "listing member");
        int size = memberData.size();
        for(int i = 0; i < size; i++) {
            HashMap<String, Object> data = memberData.get(i);
            String userName = converter.toString(data.get("userName"));
            String name = (userName == null || userName.equals("")) ? converter.toString(data.get("facebookFirstName")) : userName;
            users.get(i).setText(name);
        }
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
