package com.yc.ycandroidtool;

import android.app.Application;
import android.util.Log;

import com.yc.toollib.crash.CrashHandler;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }

}
