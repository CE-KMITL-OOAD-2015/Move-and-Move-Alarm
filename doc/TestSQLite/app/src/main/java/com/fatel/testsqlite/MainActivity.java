package com.fatel.testsqlite;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import com.fatel.testsqlite.DBHelper;
import com.fatel.testsqlite.Alarm;

public class MainActivity extends AppCompatActivity {

    DBHelper mHelper;
    List<String> alarm;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new DBHelper(this);
        alarm = mHelper.getAlarm();
        listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, alarm);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent addFriend = new Intent(this, AddAlarmActivity.class);

            startActivity(addFriend);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
