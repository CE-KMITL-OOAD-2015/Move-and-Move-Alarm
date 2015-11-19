package com.fatel.mamtv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView header;
    private TextView user;
    CircleImageView profilepic;
    public String id;
    String tempid;
    DBAlarmHelper mAlarmHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(UserManage.getInstance(this).getCurrentIdGroup() != 0)
            requestGroupInfo();
        Cache.getInstance().putData("MainActivityContext", this);
        profilepic = (CircleImageView) findViewById(R.id.profile_image);

        header = (TextView) findViewById(R.id.profile);
        user = (TextView) findViewById(R.id.username);
        if((UserManage.getInstance(this).getCurrentUsername()+"").equals("null"))
            user.setText(UserManage.getInstance(this).getCurrentFacebookFirstName());
        else
            user.setText(UserManage.getInstance(this).getCurrentUsername());

        tempid = UserManage.getInstance(this).getCurrentFacebookID();
        if(!tempid.equals("0.0")) {
            if(!tempid.equals("0")) {
                //tempid = tempid.substring(0, 1) + tempid.substring(2, 17);
                Glide.with(this).load("https://graph.facebook.com/" + tempid + "/picture?type=large").into(profilepic);
            }
        }

        if(UserManage.getInstance(this).getCurrentUser().getIdGroup() != 0) {
            requestEvent();
        }

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        MainFragment fragobj = new MainFragment();
        tx.replace(R.id.container, fragobj);
        tx.commit();
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = ProfileFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();//getActivity()
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                tx.addToBackStack(null);
                tx.replace(R.id.container, fragment).commit();

                // Highlight the selected item, update the title, and close the drawer
                setTitle("Profile");
                mDrawerLayout.closeDrawers();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //history
        History history = History.findHistory(UserManage.getInstance(this).getCurrentIdUser(),this);
        if(history==null){
            history = new History(UserManage.getInstance(this).getCurrentIdUser());
            history.save(this);
            Cache.getInstance().putData("userHistory", history);
            requestUserProgress();
        }
        else{
            Cache.getInstance().putData("userHistory", history);
            requestSendUserProgress();
        }
//
        //historygroup

        Historygroup historygroup = Historygroup.findHistorygroup(UserManage.getInstance(this).getCurrentIdGroup(),this);
        if(historygroup==null&&UserManage.getInstance(this).getCurrentIdGroup()!=0){
            historygroup = new Historygroup(UserManage.getInstance(this).getCurrentIdGroup());
            history.save(this);
            Cache.getInstance().putData("groupHistory", historygroup);
            requestGroupProgress();
        }
        else if(UserManage.getInstance(this).getCurrentIdGroup()!=0){
            Cache.getInstance().putData("groupHistory", historygroup);
            requestSendGroupProgress();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home_fragment:
                fragmentClass = MainFragment.class;
                break;
            case R.id.nav_alarm_fragment:
                fragmentClass = AlarmFragment.class;
                break;
            case R.id.nav_posture_fragment:
                fragmentClass = ChoosePostureFragment.class;
                break;
            case R.id.nav_progress_fragment :
                fragmentClass = null;
                mDrawerLayout.closeDrawers();
                Intent intent4 = new Intent(this, ProgressActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_lboard_fragment:
                fragmentClass = null;
                mDrawerLayout.closeDrawers();
                Intent intent2 = new Intent(this, ScoreboardActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_help_fragment:
                fragmentClass = HelpFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_group_fragment:
                User currentUser = UserManage.getInstance(this).getCurrentUser();
                if(currentUser.getIdGroup() == 0)
                    fragmentClass = GroupFragment.class;
                else{//มีกลุ่ม
                    requestGroupInfo(GroupMainActivity.class);
                }
                break;

            case R.id.nav_logout_fragment:
                fragmentClass = null;
                mAlarmHelper =  new DBAlarmHelper(this);


                if(!tempid.equals("0.0")){
                    FacebookSdk.sdkInitialize(this.getApplicationContext());
                    LoginManager.getInstance().logOut();
                }

                UserManage.getInstance(this).logoutUser(this);


                mAlarmHelper.deleteSetAlarm("1");
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(this, Intro_Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_set_fragment:
                fragmentClass = SetFragment.class;
                break;
            default:
                fragmentClass = MainFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragmentClass != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();//getActivity()
            FragmentTransaction tx = fragmentManager.beginTransaction();
            tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            tx.addToBackStack(null);
            tx.replace(R.id.container, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer

            if (menuItem.getTitle().equals("Home"))
                setTitle("Move Alarm");
            else
                setTitle(menuItem.getTitle());
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    public void linkPosture1(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 0);
        startActivity(intent);
    }

    public void linkPosture2(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 1);
        startActivity(intent);
    }

    public void linkPosture3(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 2);
        startActivity(intent);
    }

    public void linkPosture4(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 3);
        startActivity(intent);
    }

    public void linkPosture5(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 4);
        startActivity(intent);
    }

    public void linkPosture6(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 5);
        startActivity(intent);
    }

    public void linkPosture7(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 6);
        startActivity(intent);
    }

    public void linkPosture8(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 7);
        startActivity(intent);
    }

    public void linkPosture9(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 8);
        startActivity(intent);
    }

    public void linkPosture10(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 9);
        startActivity(intent);
    }

    public void linkPosture11(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 10);
        startActivity(intent);
    }

    public void linkPosture12(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 11);
        startActivity(intent);
    }
    public void linkPosture13(View view) {
        Intent intent = new Intent(this, PostureActivity.class);
        intent.putExtra("value", 12);
        startActivity(intent);
    }


    public void linkCreateGroup(View view)
    {

        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);
    }

    public void linkJoinGroup(View view)
    {
        Intent intent = new Intent(this, JoinGroupActivity.class);

        startActivity(intent);

    }
    public void linktoreset(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        mAlarmHelper =  new DBAlarmHelper(MainActivity.this);
                        mAlarmHelper.deleteSetAlarm("1");
                        mDrawerLayout.closeDrawers();
                        makeToast("Delete set time");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Alarm");
        builder.setMessage("Do you want to reset alarm?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void makeToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }



    public void requestGroupInfo(Class nextActivity)
    {
        final Class nxtActivity = nextActivity;
        String url = "http://203.151.92.196:8080/group/findByID";
        StringRequest findGroupRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
                            cache.putData("groupData", groupData);
                            requestEvent(nxtActivity);
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>(); //create map to keep variables
                map.put("id", "" + UserManage.getInstance(MainActivity.this).getCurrentUser().getIdGroup());

                return map;
            }
        };

        HttpConnector.getInstance(this).addToRequestQueue(findGroupRequest);
    }

    public void requestGroupInfo()
    {
        String url = "http://203.151.92.196:8080/group/findByID";
        StringRequest findGroupRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
                            cache.putData("groupData", groupData);
                            makeToast("syncing completed.");
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>(); //create map to keep variables
                map.put("id", "" + UserManage.getInstance(MainActivity.this).getCurrentUser().getIdGroup());

                return map;
            }
        };

        HttpConnector.getInstance(this).addToRequestQueue(findGroupRequest);
    }

    public void requestEvent()
    {
        String url = HttpConnector.URL + "event/getEvent";
        StringRequest eventRequest = new StringRequest(Request.Method.GET, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> eventData = converter.JSONToHashMap("" + data.get("event"));
                            cache.putData("eventData", eventData);
                            DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
                            Date date = null;
                            try {
                                date = dateFormat.parse(converter.toString(eventData.get("time")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String time = ""+date.getHours()+"."+date.getMinutes();
                            Intent intent = new Intent(getBaseContext(), EventReceiver.class);
                            Bundle b = new Bundle();
                            b.putString("event", time);
                            intent.putExtras(b);
                            sendBroadcast(intent);

                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        });

        HttpConnector.getInstance(this).addToRequestQueue(eventRequest);
    }

    public void requestEvent(Class nextActivity)
    {
        final Class nxtActivity = nextActivity;
        String url = HttpConnector.URL + "event/getEvent";
        StringRequest eventRequest = new StringRequest(Request.Method.GET, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley 5", response);
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            Log.i("event" , "" + data.get("event"));
                            HashMap<String, Object> eventData = converter.JSONToHashMap("" + data.get("event"));
                            cache.putData("eventData", eventData);
                            DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
                            Date date = null;
                            try {
                                date = dateFormat.parse(converter.toString(eventData.get("time")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String time = ""+date.getHours()+"."+date.getMinutes();
                            Intent intent = new Intent(getBaseContext(), EventReceiver.class);
                            Bundle b = new Bundle();
                            b.putString("event", time);
                            intent.putExtras(b);
                            sendBroadcast(intent);

                            Intent intent3 = new Intent(MainActivity.this, nxtActivity);
                            startActivity(intent3);

                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        });

        HttpConnector.getInstance(this).addToRequestQueue(eventRequest);
    }

    public void editname(View view){
        final EditText name = new EditText(this);
        final EditText surname = new EditText(this);
        name.setText("");
        surname.setText("");
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i("name",""+isEmpty(name));
                        Log.i("surname", "" + isEmpty(surname));
                        if(!isEmpty(name)){
                            //do
                            Log.i("name",""+name.getText().toString());
                            UserManage.getInstance(MainActivity.this).getCurrentUser().setFirstName(name.getText().toString());
                        }
                        if(!isEmpty(surname)) {
                            //do
                            Log.i("surname",""+surname.getText().toString());
                            UserManage.getInstance(MainActivity.this).getCurrentUser().setLastName(surname.getText().toString());
                        }
                        UserManage.getInstance(MainActivity.this).updateUser(MainActivity.this);
                        Fragment fragment = null;
                        Class fragmentClass;
                        fragmentClass = ProfileFragment.class;
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragmentManager = getSupportFragmentManager();//getActivity()
                        FragmentTransaction tx = fragmentManager.beginTransaction();
                        tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        tx.addToBackStack(null);
                        tx.replace(R.id.container, fragment).commit();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        name.setTextColor(Color.WHITE);
        surname.setTextColor(Color.WHITE);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        name.setHint(Html.fromHtml("<font color='#B0B0B0'>Name</font>"));
        layout.addView(name);
        surname.setHint(Html.fromHtml("<font color='#B0B0B0'>Surname</font>"));
        layout.addView(surname);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Edit");
        builder.setView(layout);
        builder.setMessage("Edit information").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    public void requestUserProgress()
    {
        String url = HttpConnector.URL + "userProgress/getByUser";
        StringRequest progressRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley 8", response);
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> progress = converter.JSONToHashMap(converter.toString(data.get("progress")));
                            HashMap<String, Object> userData = converter.JSONToHashMap(converter.toString(progress.get("user")));

                            History history = (History) Cache.getInstance().getData("userHistory");
                            history.setCancelActivity(converter.toInt(progress.get("cancelActivity")));
                            history.setNumberOfAccept(converter.toInt(progress.get("numberOfAccept")));
                            history.setIdUser(converter.toInt(userData.get("id")));
                            history.save(MainActivity.this);
                            makeToast("syncing completed.");
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>(); //create map to keep variables
                HashMap<String, Object> JSON = new HashMap<>();
                JSON.put("user", UserManage.getInstance(MainActivity.this).getCurrentUser().getGeneralValues());
                map.put("JSON", Converter.getInstance().HashMapToJSON(JSON));
                return map;
            }
        };

        HttpConnector.getInstance(this).addToRequestQueue(progressRequest);
    }

    public void requestSendUserProgress()
    {
        String url = HttpConnector.URL + "userProgress/save";
        StringRequest progressRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            makeToast("syncing completed.");
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>(); //create map to keep variables
                HashMap<String, Object> JSON = new HashMap<>();
                HashMap<String, Object> progress;

                Log.i("volley", "user request");

                final History history = (History) Cache.getInstance().getData("userHistory");

                progress = history.getGeneralValues();
                JSON.put("user", UserManage.getInstance(MainActivity.this).getCurrentUser().getGeneralValues());
                JSON.put("activityProgress", progress);
                map.put("JSON", Converter.getInstance().HashMapToJSON(JSON));
                return map;
            }
        };

        HttpConnector.getInstance(this).addToRequestQueue(progressRequest);
    }

    public void requestGroupProgress()
    {
        String url = HttpConnector.URL + "groupProgress/getByGroup";
        StringRequest progressRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley 9", response);
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> progress = converter.JSONToHashMap("" + data.get("progress"));
                            HashMap<String, Object> groupData = converter.JSONToHashMap("" + progress.get("group"));
                            Historygroup history = (Historygroup) Cache.getInstance().getData("groupHistory");
                            history.setCancelEvent(converter.toInt(progress.get("cancelActivity")));
                            history.setNumberOfAccept(converter.toInt(progress.get("numberOfAccept")));
                            history.save(MainActivity.this);
                            makeToast("syncing completed.");
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                User currentUser = UserManage.getInstance(MainActivity.this).getCurrentUser();
                Map<String, String> map = new HashMap<>(); //create map to keep variables
                HashMap<String, Object> JSON = new HashMap<>();
                HashMap<String, Object> group = new HashMap<>();
                group.put("id", currentUser.getIdGroup());
                JSON.put("group", group);
                map.put("JSON", Converter.getInstance().HashMapToJSON(JSON));
                return map;
            }
        };

        HttpConnector.getInstance(this).addToRequestQueue(progressRequest);
    }

    public void requestSendGroupProgress()
    {
        String url = HttpConnector.URL + "groupProgress/save";
        StringRequest progressRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
                        Log.i("volley", "in sending group request");
                        Converter converter = Converter.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            makeToast("syncing completed.");
                        }
                        else {
                            makeToast(converter.toString(data.get("description")));
                        }
                    }
                }, new Response.ErrorListener() { //create error listener to trace an error if download process fail
            @Override
            public void onErrorResponse(VolleyError volleyError) { //when error listener is activated
                Log.i("volley", volleyError.toString());
                makeToast("Cannot connect to server or internal server error.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>(); //create map to keep variables
                HashMap<String, Object> JSON = new HashMap<>();
                HashMap<String, Object> group = new HashMap<>();
                HashMap<String, Object> progress;

                Log.i("volley", "group request");

                group.put("id", UserManage.getInstance(MainActivity.this).getCurrentIdGroup());

                final Historygroup history = (Historygroup) Cache.getInstance().getData("groupHistory");

                progress = history.getGeneralValues();
                JSON.put("group", group);
                JSON.put("activityProgress", progress);
                Log.i("progress", "" + progress);
                map.put("JSON", Converter.getInstance().HashMapToJSON(JSON));
                return map;
            }
        };

        HttpConnector.getInstance(this).addToRequestQueue(progressRequest);
    }
}