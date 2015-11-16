package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.Calendar;

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
    DBAlarmHelper mAlarmHelper;
    private HistoryHelper mhistoryHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cache.getInstance().putData("MainActivityContext", this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            firstName = bundle.getString("firstname");
            lastName = bundle.getString("lastname");
            id = bundle.getString("id");
        }
        profilepic = (CircleImageView) findViewById(R.id.profile_image);
        header = (TextView) findViewById(R.id.profile);
        user = (TextView) findViewById(R.id.username);
        if (bundle != null) {
            user.setText(firstName);
            passimg = new Bundle();
            passimg.putString("id", id);
        }
        Glide.with(this).load("https://graph.facebook.com/" + UserManage.getInstance(this).getCurrentFacebookId() + "/picture?type=large").into(profilepic);
        // Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        MainFragment fragobj = new MainFragment();
        if (bundle != null)
            fragobj.setArguments(passimg);
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
        Class fragmentClass;
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
                fragmentClass = GroupFragment.class;
                break;
            case R.id.nav_logout_fragment:
                fragmentClass = null;

                mAlarmHelper =  new DBAlarmHelper(this);
                if(UserManage.getInstance(this).getCurrentFacebookFirstName()==null) {
                    UserManage.getInstance(this).logoutUser(this);
                }
                else if(UserManage.getInstance(this).getCurrentFacebookFirstName()!=null) {
                    UserManage.getInstance(this).logoutUser(this);
                    LoginManager.getInstance().logOut();
                }
                mAlarmHelper.deleteSetAlarm("1");
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(this, Intro_Activity.class);
                startActivity(intent);
                break;
//            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
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
}