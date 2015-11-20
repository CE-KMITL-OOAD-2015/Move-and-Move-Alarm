package com.fatel.mamtv1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JoinGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_group, menu);
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

    public void joinGroup(View view)
    {
        EditText groupCode = (EditText) findViewById(R.id.edit_message);

        if(groupCode.getText().toString().length() < 4) {
            makeToast("Group code must be equals 4 digits.");
            return;
        }

        final int groupID = Integer.parseInt(groupCode.getText().toString());

        String url = HttpConnector.URL + "group/addMember";
        StringRequest findGroupRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley addMember", response);
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
                            UserManage.getInstance(JoinGroupActivity.this).getCurrentUser().setIdGroup(converter.toInt(groupData.get("id")));
                            cache.putData("groupData", groupData);
                            JoinGroupActivity.this.requestEvent();
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server. Please check the Internet setting.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Converter converter = Converter.getInstance();
                Map<String, String> map = new HashMap<String, String>(); //create map to keep variables
                HashMap<String, Object> JSON = new HashMap<>();
                HashMap<String, Object> group = new HashMap<>();
                group.put("id", groupID);
                JSON.put("member", UserManage.getInstance(JoinGroupActivity.this).getCurrentUser().getGeneralValues());
                JSON.put("group", converter.HashMapToJSON(group));

                map.put("JSON", converter.HashMapToJSON(JSON));

                return map;
            }
        };

        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(findGroupRequest);
    }

    public void makeToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void requestEvent()
    {
        String url = HttpConnector.URL + "event/getEvent";
        StringRequest eventRequest = new StringRequest(Request.Method.GET, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volle event", response);
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> eventData = converter.JSONToHashMap("" + data.get("event"));
                            cache.putData("eventData", eventData);

                            Intent intent3 = new Intent(JoinGroupActivity.this, GroupMainActivity.class);
                            startActivity(intent3);
                            finish();
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server. Please check the Internet setting.");
            }
        });

        HttpConnector.getInstance(this).addToRequestQueue(eventRequest);
    }
}
