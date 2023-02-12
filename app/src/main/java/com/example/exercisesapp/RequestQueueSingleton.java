package com.example.exercisesapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {

    // attributes
    private static RequestQueueSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    /**
     * constructor for request queue
     * @param context
     */
    private RequestQueueSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * create instance of request queue
     * @param context
     * @return
     */
    public static synchronized RequestQueueSingleton getInstance(Context context) {

        // Check if there is already an instance
        if (instance == null) {
            instance = new RequestQueueSingleton(context);
        }
        return instance;
    }

    /**
     * get request queue instance
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * add request to request queue
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

