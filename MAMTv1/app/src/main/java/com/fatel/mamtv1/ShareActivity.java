package com.fatel.mamtv1;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.ArrayList;
import java.util.Arrays;

public class ShareActivity extends AppCompatActivity {


    private ImageView imgPreview;
        private Button postPicBtn;
        Uri Imguri = null;
        private  static final String PERMISSION = "publish_actions";


        //ชนิดของการโพสต์ที่รอดำเนินการ
        private enum PendingAction{
            NONE,
            //POST_LINK,
            POST_PICTURE
        }

        //การโพสต์ที่รอดำเนินการซึ่งจะดำเนินการหลังจากได้รับสิทธิ์ publish_actions แล้ว
        private PendingAction pendingAction = PendingAction.NONE;

        //method เริ่มต้นกระบวนการโพสต์

        private void performPublish(PendingAction action){
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            Log.i("log in", "" + accessToken); // new add

            if(accessToken != null){
                pendingAction = action;
                handlePendingAction();
            }
        }



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_share);
            Log.i("URI", "can pass");
            //Second Activity get a Uri path
            Bundle b = getIntent().getExtras();
            Log.i("URI", "can pass1");
            if (b != null) {
                //String uri_Str= b.getString("uri_Str");
                //Imguri = Uri.parse(uri_Str);
                Log.i("URI", "can pass1.1");
                Imguri = getIntent().getParcelableExtra("uri");
                Log.i("URI", "can pass2");
            }
            Log.i("URI", "" + Imguri);


            imgPreview = (ImageView) findViewById(R.id.imgNxtPreview);
            previewCapturedImage(Imguri);


            //add for editText บ่ได้ใช้ คิดว่าจะตัด caption ทิ้ง
            EditText myCaption = (EditText)findViewById(R.id.caption);
            String myCap = myCaption.getText().toString();


            //add for activity_share facebook
            postPicBtn = (Button) findViewById(R.id.btn_share);  //btn_postPic    btn_share

            postPicBtn.setOnClickListener(new View.OnClickListener() {
                //@override
                public void onClick (View v){
                    //showPickPictureDialog();
                    performPublish(PendingAction.POST_PICTURE);
                }
            });


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_share, menu);
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


        /**
         * Display image from a path to ImageView
         */
        private void previewCapturedImage(Uri img) {
            try {
                // hide video preview
                //videoPreview.setVisibility(View.GONE);

                imgPreview.setVisibility(View.VISIBLE);

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 2;

                final Bitmap bitmap = BitmapFactory.decodeFile(img.getPath(),
                        options);
                Log.i("path",""+img.getPath());

                imgPreview.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }



        //add for activity_share facebook(picture)
        private void postPicture(){
            Profile profile = Profile.getCurrentProfile();
            if(profile==null)
                Log.i("profile","null");
            else
                Log.i("profile","!null");
            if(Imguri==null)
                Log.i("uri","null");
            else
                Log.i("uri",""+Imguri);
            Bitmap picture = BitmapFactory.decodeFile(Imguri.getPath());
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


        //add for activity_share facebook
        private boolean hasPublishPermission(){
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            return accessToken != null &&
                    accessToken.getPermissions().contains("publish_actions");
        }



        //add for activity_share facebook
        private  void handlePendingAction(){
            PendingAction oldPendingAction = pendingAction;
            pendingAction = PendingAction.NONE;

            switch (oldPendingAction){
                case NONE:
                    break;
                //case POST_LINK:
                //    postLink();
                //    break;
                case POST_PICTURE:
                    postPicture();
                    break;
            }
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
                        new AlertDialog.Builder(ShareActivity.this)
                                .setTitle(title)
                                .setMessage(alertMessage)
                                .setPositiveButton("OK",null)
                                .show();
                    }
                };



    }
