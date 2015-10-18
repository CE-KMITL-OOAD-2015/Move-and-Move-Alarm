package com.fatel.mamtv1;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Monthon on 18/10/2558.
 */
public class MyService extends IntentService {
    public MyService(){
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Gets data from the incoming Intent
        for (int i=0; i<5; i++) {
            Log.i("SimpleWakefulReceiver", "Running service " + (i+1)
                    + "/5 @ " + SystemClock.elapsedRealtime());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
        Log.i("SimpleWakefulReceiver", "Completed service @ " + SystemClock.elapsedRealtime());
        SimpleWakefulReciver.completeWakefulIntent(intent);
    }

}
