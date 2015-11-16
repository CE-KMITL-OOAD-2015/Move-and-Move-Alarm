package com.fatel.mamtv1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupActivity extends AppCompatActivity {
    EditText gName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        gName = (EditText)findViewById(R.id.edit_message);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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

    public void linkGroup(View view)
    {
        if(gName.length()<6)
        {
            Toast.makeText(this, "Please enter Group name at least 6 characters", Toast.LENGTH_SHORT).show();
        }
        else{
            //ตรงนี้
            String url = "http://203.151.92.196:8080/group/createGroup";

            StringRequest createGroupRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                    new Response.Listener<String>() { //create new listener to traces the data
                        @Override
                        public void onResponse(String response) { //when listener is activated
                            Log.i("volley", response);
                            Converter converter = Converter.getInstance();
                            HashMap<String, Object> data = converter.JSONToHashMap(response);
                            if((boolean) data.get("status")) {
                                HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
                                String name = converter.toString(groupData.get("name"));
                                int id = converter.toInt(groupData.get("id"));
                                int score = converter.toInt(groupData.get("score"));
                            }
                            else {
                                makeToast(converter.toString(data.get("description")));
                            }
                        }
                    }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
                @Override
                public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                    Log.i("volley", volleyError.toString());
                }
            }) { //define POST parameters
                @Override
                protected Map<String, String> getParams() {
                    User user = UserManage.getInstance(CreateGroupActivity.this).getCurrentUser();
                    HashMap<String, Object> JSON = new HashMap<>();
                    HashMap<String, Object> userData = new HashMap<>();
                    HashMap<String, Object> groupData = new HashMap<>();
                    userData.put("id", user.getIdUser());
                    userData.put("firstName",user.getFirstName());
                    userData.put("lastName",user.getLastName());
                    userData.put("userName",user.getUserName());
                    userData.put("age",user.getAge());
                    userData.put("score",user.getScore());
                    userData.put("profileImage",user.getProfileImage());
                    userData.put("facebookID",user.getFacebookID());
                    userData.put("facebookFirstName",user.getFacebookFirstName());
                    userData.put("facebookLastName",user.getFacebookLastName());
                    userData.put("gender",user.getGender());
                    userData.put("email",user.getEmail());

                    groupData.put("user", user);
                    groupData.put("name", gName.getText().toString());

                    Map<String, String> map = new HashMap<String, String>(); //create map to keep variables

                    return map;
                }
            };

            HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(createGroupRequest);
        }
    }
    public void makeToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
