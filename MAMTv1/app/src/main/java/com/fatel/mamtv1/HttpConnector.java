package com.fatel.mamtv1;

        import android.app.Activity;
        import android.app.Application;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.VolleyLog;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import java.net.NetworkInterface;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Objects;

/**
 * Created by oat90 on 3/11/2558.
 */
public class HttpConnector{

    private Context context;
    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static HttpConnector sInstance;


    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized HttpConnector getInstance() {
        return sInstance;
    }
    public HttpConnector(Context context){
        this.context = context;
        sInstance = getInstance();
        mRequestQueue = getRequestQueue(context);
    }
    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue(Context context) {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue(context).add(req);
    }

    /**
     * Adds the specified request, to the global queue using the Default TAG.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue(context).add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public void getrequest(){

// add the request object to the queue to be executed

        //HttpConnector.getInstance().addToRequestQueue(req);
        final String URL = "https://www.google.co.th/?gws_rd=cr,ssl&ei=bxw7Vo6KJuPMmwW4hrzoBg";
        StringRequest req = new StringRequest(Request.Method.GET,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Log.i("success", dateFormat.format(calendar.getTime()));
                VolleyLog.v("Response:%n %s", response);
                Log.i("success",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.i("error","error");
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Log.i("start",dateFormat.format(calendar.getTime()));
        mRequestQueue.add(req);
    }

}

