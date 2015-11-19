package com.fatel.mamtv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login_Activity extends AppCompatActivity {
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private DBAlarmHelper mAlarmHelper;
    public static Login_Activity instance;

    CallbackManager callbackManager;
    ProfileTracker profileTracker;

    @Override
    public void finish()
    {
        super.finish();
        instance = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fatel.mamtv1",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        linkWithFB();
                    }

                    @Override
                    public void onCancel() {
                        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                        Profile profile = Profile.getCurrentProfile();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                        Profile profile = Profile.getCurrentProfile();
                    }
                });

        setContentView(R.layout.login_layout);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                linkWithFB();
            }
        };
        mAlarmHelper = new DBAlarmHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_, menu);
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

    public void linkMain(View view)
    {
        EditText username;
        EditText password;

        username = (EditText)findViewById(R.id.enter_username);
        password = (EditText)findViewById(R.id.enter_password);
        Cache.getInstance().putData("loginContext", this);


        if(username.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(password.getText().toString().equals(""))
        {
            Toast toast = Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if((username.getText().toString().length()<6) || (password.getText().toString().length()<6))
        {
            Toast toast = Toast.makeText(this, "Please enter Username and Password at least 6 characters", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            UserManage.getInstance(this).loginUser(username.getText().toString(), password.getText().toString(), this);
       }

    }

    public void linkWithFB()
    {
        boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();

        Cache.getInstance().putData("loginFBContext", this);
        if (loggedIn && (profile != null)) {

            UserManage.getInstance(this).loginFBUser(profile.getId(), profile.getFirstName(), this);
            UserManage.getInstance(this).setFacebookLastName(profile.getLastName(), this);

            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            Toast.makeText(this, "Hello "+profile.getFirstName(), Toast.LENGTH_SHORT).show();
        }
    }

}
