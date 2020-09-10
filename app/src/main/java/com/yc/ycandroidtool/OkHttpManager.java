package com.yc.ycandroidtool;


import com.yc.toollib.network.stetho.NetworkListener;
import com.yc.toollib.network.stetho.NetworkInterceptor;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpManager {

    private static final String TAG = "OkHttpManager";
    private static OkHttpManager sInstance;

    public static OkHttpManager getInstance() {
        if (sInstance == null) {
            sInstance = new OkHttpManager();
        }
        return sInstance;
    }

    public void get(String url, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .eventListenerFactory(NetworkListener.get())
                .addNetworkInterceptor(new NetworkInterceptor())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }
}
