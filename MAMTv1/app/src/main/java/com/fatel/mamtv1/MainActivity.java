package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView header;
    private TextView user;
    CircleImageView profilepic;
    String firstName;
    String lastName;
    public String id;
    Bundle passimg;
    String tempid;
    DBAlarmHelper mAlarmHelper;
    private HistoryHelper mhistoryHelper;
    private HistorygroupHelper mhistorygroupHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cache.getInstance().putData("MainActivityContext", this);
        Cache.getInstance().putData("MainActivityActivity",this);
        profilepic = (CircleImageView) findViewById(R.id.profile_image);

        header = (TextView) findViewById(R.id.profile);
        user = (TextView) findViewById(R.id.username);
        if((UserManage.getInstance(this).getCurrentUsername()+"").equals("null"))
            user.setText(UserManage.getInstance(this).getCurrentFacebookFirstName());
        else
            user.setText(UserManage.getInstance(this).getCurrentUsername());

        Log.i("checkid", UserManage.getInstance(this).getCurrentFacebookId() + "");
        Log.i("checkuser", UserManage.getInstance(this).getCurrentUsername() + "");
        Log.i("checkemail", UserManage.getInstance(this).getCurrentEmail() + "");
        Log.i("checkemail", UserManage.getInstance(this).getCurrentFacebookFirstName() + "");
        Log.i("checkid", UserManage.getInstance(this).getCurrentFacebookLastName() + "");
        Log.i("checkid", UserManage.getInstance(this).getCurrentAge() + "");
        Log.i("checkid", UserManage.getInstance(this).getCurrentGender() + "");
        Log.i("score", UserManage.getInstance(this).getCurrentScore() + "");
        tempid = UserManage.getInstance(this).getCurrentFacebookID();
        Log.i("checkname", tempid+"");
        if(!tempid.equals("0.0")) {
            if(!tempid.equals("0")) {
                tempid = tempid.substring(0, 1) + tempid.substring(2, 17);
                Glide.with(this).load("https://graph.facebook.com/" + tempid + "/picture?type=large").into(profilepic);
            }
        }
        // Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        if(UserManage.getInstance(this).getCurrentUser().getIdGroup() != 0) {
            requestEvent();
            Log.i("request", "event");
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
        mhistoryHelper = new HistoryHelper(this);
        History history = mhistoryHelper.getHistoryUser(UserManage.getInstance(this).getCurrentIdUser());
        if(history==null){
            history = new History(UserManage.getInstance(this).getCurrentIdUser());
            history.save(this);
        }

        //historygroup
        mhistorygroupHelper = new HistorygroupHelper(this);
        Historygroup historygroup = mhistorygroupHelper.getHistoryGroup(UserManage.getInstance(this).getCurrentIdGroup());
        if(historygroup==null&&UserManage.getInstance(this).getCurrentIdGroup()!=0){
            historygroup = new Historygroup(UserManage.getInstance(this).getCurrentIdGroup());
            historygroup.save(this);
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
                    requestGroupInfo();
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


                /*
                if(UserManage.getInstance(this).mauser == 1) {
                    UserManage.getInstance(this).logoutUser(this);
                    UserManage.getInstance(this).mauser = 0;
                }
                else if(UserManage.getInstance(this).mauser == 2) {
                    LoginManager.getInstance().logOut();
                    UserManage.getInstance(this).logoutUser(this);
                    UserManage.getInstance(this).mauser = 0;
                }*/

                mAlarmHelper.deleteSetAlarm("1");
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(this, Intro_Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_set_fragment:
                fragmentClass = SetFragment.class;
                break;
//            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
//                break;
//                break;
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
            //menuItem.setChecked(true);
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
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
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



    public void requestGroupInfo()
    {
        String url = "http://203.151.92.196:8080/group/findByID";
        StringRequest findGroupRequest = new StringRequest(Request.Method.POST, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
                        Converter converter = Converter.getInstance();
                        Cache cache = Cache.getInstance();
                        HashMap<String, Object> data = converter.JSONToHashMap(response);
                        if((boolean) data.get("status")) {
                            HashMap<String, Object> groupData = converter.JSONToHashMap(converter.toString(data.get("group")));
                            cache.putData("groupData", groupData);
                            Intent intent3 = new Intent(MainActivity.this, GroupMainActivity.class);
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
                makeToast("Cannot connect to server. Please check the Internet setting.");
            }
        }) { //define POST parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>(); //create map to keep variables
                map.put("id", "" + UserManage.getInstance(MainActivity.this).getCurrentUser().getIdGroup());

                return map;
            }
        };

        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(findGroupRequest);
    }

    public void requestEvent()
    {
        String url = "http://203.151.92.196:8080/event/getEventFixedTime";
        StringRequest eventRequest = new StringRequest(Request.Method.GET, url, //create new string request with POST method
                new Response.Listener<String>() { //create new listener to traces the data
                    @Override
                    public void onResponse(String response) { //when listener is activated
                        Log.i("volley", response);
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

        HttpConnector.getInstance((Context) Cache.getInstance().getData("MainActivityContext")).addToRequestQueue(eventRequest);
    }
}