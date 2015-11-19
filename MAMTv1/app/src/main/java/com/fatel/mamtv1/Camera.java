package com.fatel.mamtv1;
import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.Signature;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.ActionBarActivity;
import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

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
        import com.facebook.share.ShareApi;
        import com.facebook.share.Sharer;
        import com.facebook.share.model.ShareLinkContent;
        import com.facebook.share.model.SharePhoto;
        import com.facebook.share.model.SharePhotoContent;

        import java.io.File;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Date;
        import java.util.Locale;

public class Camera extends  ActionBarActivity {

    CallbackManager callbackManager;
    ProfileTracker profileTracker;

    private TextView userName;
    private ProfilePictureView profilePicture;
    //private Button postLinkButton;
    //private Button postPictureButton;

    //add for activity_share facebook
    private final static int PICK_IMAGE = 1;
    private String imageFilePath;  //ชื่อพาธของไฟล์รูป
    private  static final String PERMISSION = "publish_actions";
    private Button postPictureButton;

    //ชนิดของการโพสต์ที่รอดำเนินการ
    private enum PendingAction{
        NONE,
        POST_LINK,
        POST_PICTURE
    }

    //การโพสต์ที่รอดำเนินการซึ่งจะดำเนินการหลังจากได้รับสิทธิ์ publish_actions แล้ว
    private PendingAction pendingAction = PendingAction.NONE;

    //method เริ่มต้นกระบวนการโพสต์
    private void performPublish(PendingAction action){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null){
            pendingAction = action;
            handlePendingAction();
        }
    }


    //add for sync with camera
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final String IMAGE_DIRECTORY_NAME = "Move Alarm Camera"; //ชื่อ folder ที่เก็บรูป

    private Uri fileUri;
    private Button capturePictureButton;
    private ImageView imgPreview;

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

                        //add for activity_share facebook
                        handlePendingAction();
                    }


                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                    }
                });

        setContentView(R.layout.activity_pre_share);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                //updateUI();
            }
        };

        //add for sync with camera
        capturePictureButton = (Button)findViewById(R.id.btn_capturePic);

        /**
         * Capture image button click event
         * */
        capturePictureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });


        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }


    }


    //add for activity_share facebook
    private void showPickPictureDialog(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"เลือกรูปภาพ"),PICK_IMAGE);
    }


    //add for sync with camera
    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    //add for sync with camera
    /**
     * Capturing Camera Posture will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }



    //add for sync with camera
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }



    //add for sync with camera
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUI();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //updateUI();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //add for activity_share facebook
        if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{
                                android.provider.MediaStore.Images.ImageColumns.DATA
                        },
                        null, null, null
                );
                cursor.moveToFirst();

                imageFilePath = cursor.getString(0);
                showConfirmPostPictureDialog();


                cursor.close();
            }
        }


        //add for sync with camera
        /**
         * Receiving activity result method will be called after closing the camera
         * */
        // if the result is capturing Posture
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view

                //First Activity to get a Uri the URI which you get from onActivityResult
                Intent intent = new Intent(getBaseContext(),ShareActivity.class);
                intent.putExtra("uri", fileUri);
                startActivity(intent);





            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Posture capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    //add for sync with camera
    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }








    //add for activity_share facebook
    private void showConfirmPostPictureDialog(){
        Bitmap picture = BitmapFactory.decodeFile(imageFilePath);

        final ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(picture);

        new AlertDialog.Builder(this)
                .setTitle("โพสต์รูปภาพนี้ลง Facebook?")
                .setView(imageView)
                .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        performPublish(PendingAction.POST_PICTURE);
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }




    //add for activity_share facebook
    private  void handlePendingAction(){
        PendingAction oldPendingAction = pendingAction;
        pendingAction = PendingAction.NONE;

        switch (oldPendingAction){
            case NONE:
                break;
            case POST_LINK:
                postLink();
                break;
            case POST_PICTURE:
                postPicture();
                break;
        }
    }


    //add for activity_share facebook(picture)
    private void postPicture(){
        Profile profile = Profile.getCurrentProfile();

        Bitmap picture = BitmapFactory.decodeFile(imageFilePath);
        SharePhoto pictureToShare = new SharePhoto.Builder()
                .setBitmap(picture)
                .build();

        ArrayList<SharePhoto> pictureList = new ArrayList<>();
        pictureList.add(pictureToShare);

        SharePhotoContent content = new SharePhotoContent.Builder()
                .setPhotos(pictureList)
                .build();

        if(profile != null && hasPublishPermission()){
            ShareApi.share(content, shareCallback);
        }else{
            pendingAction = pendingAction.POST_PICTURE;
            LoginManager.getInstance().logInWithPublishPermissions(
                    this, Arrays.asList(PERMISSION));
        }
    }

    //add for activity_share facebook(link)
    private void postLink(){
        Profile profile = Profile.getCurrentProfile();
        //รายละเอียดของลิ้งค์ที่จะโพสต์ลงเฟส
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("3bugs.com")
                .setContentDescription("บทเรียน บลาๆๆ")
                .setContentUrl(Uri.parse("http://www.3bugs.com/"))
                .setImageUrl(Uri.parse("http://www.3bugs.com/logo.png"))
                .build();

        if(profile != null && hasPublishPermission()){
            ShareApi.share(content,shareCallback);
        }else{
            pendingAction = pendingAction.POST_LINK;
            LoginManager.getInstance().logInWithPublishPermissions(
                    this, Arrays.asList(PERMISSION));
        }
    }


    //add for activity_share facebook
    private boolean hasPublishPermission(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null &&
                accessToken.getPermissions().contains("publish_actions");
    }


    //add for activity_share facebook
    private FacebookCallback<Sharer.Result> shareCallback =
            new FacebookCallback<Sharer.Result>(){
                @Override
                public void onCancel(){

                }

                @Override
                public void onError(FacebookException error){
                    String title = "เกิดข้อผิดพลาดในการโพสต์";
                    String msg = error.getMessage();
                    showResult(title,msg);
                }

                @Override
                public void onSuccess(Sharer.Result result){
                    if(result.getPostId() != null){
                        String title = "โพสต์ลง Facebook สำเร็จ";
                        String id = result.getPostId();
                        String msg = String.format("Post ID: %s",id);
                        showResult(title,msg);
                    }
                }

                private  void showResult(String title,String alertMessage){
                    new AlertDialog.Builder(Camera.this)
                            .setTitle(title)
                            .setMessage(alertMessage)
                            .setPositiveButton("OK",null)
                            .show();
                }
            };


}
