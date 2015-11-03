package com.fatel.mamtv1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kid14 on 11/4/2015.
 */
public class LoadImageTask extends AsyncTask<CircleImageView, Void, Bitmap> {

    URL url;
    Bitmap bitmap;
    CircleImageView im = null;
    public LoadImageTask(URL url)
    {
        this.url = url;
    }
    @Override
    protected Bitmap doInBackground(CircleImageView... im) {
        this.im = im[0];
        return download_Image(url.toString());
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        im.setImageBitmap(result);
    }
    private Bitmap download_Image(String url) {
        Bitmap bmp =null;
        try{
            URL ulrn = new URL(url);
            HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
                return bmp;
        }catch(Exception e){}
        return bmp;
    }
}