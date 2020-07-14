package com.yc.ycandroidtool;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.yc.toollib.crash.callback.RecoveryCallback;
import com.yc.toollib.crash.core.Recovery;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);

//        init();
    }

    private void init() {
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .recoverEnabled(true)
                .callback(new MyCrashCallback())
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .skip(TestActivity.class)
                .init(this);
    }

    private class MyCrashCallback implements RecoveryCallback{

        @Override
        public void stackTrace(String exceptionMessage) {
            Log.e("yangchong---", "exceptionMessage:" + exceptionMessage);
        }

        @Override
        public void cause(String cause) {
            Log.e("yangchong---", "cause:" + cause);
        }

        @Override
        public void exception(String exceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
            Log.e("yangchong---", "exceptionClassName:" + exceptionType);
            Log.e("yangchong---", "throwClassName:" + throwClassName);
            Log.e("yangchong---", "throwMethodName:" + throwMethodName);
            Log.e("yangchong---", "throwLineNumber:" + throwLineNumber);
        }

        @Override
        public void throwable(Throwable throwable) {

        }
    }

}
