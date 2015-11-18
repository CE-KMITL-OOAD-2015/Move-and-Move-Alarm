package com.fatel.mamtv1;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HttpConnector {
    private static HttpConnector instance;
    private RequestQueue requestQueue;
    private static Context context;
    private Map<String, Object> cache;
    public static String URL = "http://203.151.92.196:8080/";

    private HttpConnector(Context context) {
        this.context = context;
        cache = new HashMap<>();
    }

    public static synchronized HttpConnector getInstance(Context context) {
        if (instance == null) {
            instance = new HttpConnector(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
