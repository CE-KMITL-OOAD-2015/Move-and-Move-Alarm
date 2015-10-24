package com.example.socialapp;

/**
 * Created by Monthon on 21/10/2558.
 */
        import android.support.v7.app.ActionBarActivity;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.Signature;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.util.Base64;
        import android.util.Log;
        import android.widget.Button;
        import android.widget.TextView;
        import com.facebook.AccessToken;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.Profile;
        import com.facebook.ProfileTracker;
        import com.facebook.appevents.AppEventsLogger;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.ProfilePictureView;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
public class Account extends ActionBarActivity {

    CallbackManager callbackManager;
    ProfileTracker profileTracker;

    private TextView userName;
    private ProfilePictureView profilePicture;
    //private Button postLinkButton;
    //private Button postPictureButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.socialapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
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
                        updateUI();
                    }

                    @Override
                    public void onCancel() {
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        updateUI();
                    }
                });

        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.uesr_name);
        profilePicture = (ProfilePictureView)
                findViewById(R.id.profile_picture);
        //postLinkButton = (Button)
        //      findViewById(R.id.post_link_button);
        //postPictureButton = (Button)
        // findViewById(R.id.post_picture_button);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                updateUI();
            }
        };
    }

    private void updateUI(){
        boolean loggedIn = AccessToken.getCurrentAccessToken()!= null;
        Profile profile = Profile.getCurrentProfile();

        if(loggedIn && (profile != null)){
            profilePicture.setProfileId(profile.getId());
            Log.i("iduser", profile.getId());
            userName.setText(profile.getName());
            Log.i("Nameuser", profile.getName());
            Log.i("Firstnameuser", profile.getFirstName());
            //postLinkButton.setEnabled(true);
            //postPictureButton.setEnabled(true);
        }
        else {
            profilePicture.setProfileId(null);
            userName.setText(null);
            // postLinkButton.setEnabled(false);
            // postPictureButton.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        updateUI();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
