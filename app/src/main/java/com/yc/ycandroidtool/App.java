package com.yc.ycandroidtool;

import android.app.Application;
import android.util.Log;

import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashToolUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this, new CrashListener() {
            @Override
            public void againStartApp() {
                CrashToolUtils.reStartApp2(App.this,500, MainActivity.class);
            }

            @Override
            public void recordException(Throwable ex) {

            }
        });
    }

}
