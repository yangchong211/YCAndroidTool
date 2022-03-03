package com.yc.ycandroidtool;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yc.anrtoollib.watch.ANRError;
import com.yc.anrtoollib.watch.ANRInterceptor;
import com.yc.anrtoollib.watch.ANRListener;
import com.yc.anrtoollib.watch.ANRWatchDog;
import com.yc.catonhelperlib.canary.BlockCanary;
import com.yc.catonhelperlib.watch.HandlerBlockTask;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.netlib.utils.NetworkTool;
import com.yc.ycandroidtool.canary.AppContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class App extends Application {

    private static Context sContext;
    public ANRWatchDog anrWatchDog = new ANRWatchDog(2000);
    public int mDuration = 4;
    public final ANRListener silentListener = new ANRListener() {
        @Override
        public void onAppNotResponding(@NonNull ANRError error) {
            Log.e("ANR-Watchdog-Demo", "", error);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
        NetworkTool.getInstance().init(this);
        //建议只在debug环境下显示，点击去网络拦截列表页面查看网络请求数据
//        NetworkTool.getInstance().setFloat(this);
        //HandlerBloc·kTask.getInstance().startWork();
        //WatchDog.getInstance().startWork();
        sContext = this;
        //BlockCanary.install(this, new AppContext()).start();
        //initAnrTool();
    }

    public static Context getAppContext() {
        return sContext;
    }

    private void initCrash() {
        //ThreadHandler.getInstance().init(this);
        CrashHandler.getInstance().init(this, new CrashListener() {
            /**
             * 重启app
             */
            @Override
            public void againStartApp() {
                System.out.println("崩溃重启----------againStartApp------");
                CrashToolUtils.reStartApp1(App.this,2000);
                //CrashToolUtils.reStartApp2(App.this,2000, MainActivity.class);
                //CrashToolUtils.reStartApp3(App.this);
            }

            /**
             * 自定义上传crash，支持开发者上传自己捕获的crash数据
             * @param ex                        ex
             */
            @Override
            public void recordException(Throwable ex) {
                System.out.println("崩溃重启----------recordException------");
                //自定义上传crash，支持开发者上传自己捕获的crash数据
                //StatService.recordException(getApplication(), ex);
            }
        });
    }

    private void initAnrTool() {
        anrWatchDog.setANRListener(new ANRListener() {
                    @Override
                    public void onAppNotResponding(@NonNull ANRError error) {
                        Log.e("ANR-Watchdog-Demo", "Detected Application Not Responding!");
                        try {
                            new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(error);
                        }
                        catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        Log.i("ANR-Watchdog-Demo", "Error was successfully serialized");

                        throw error;
                    }
                })
                .setANRInterceptor(new ANRInterceptor() {
                    @Override
                    public long intercept(long duration) {
                        long ret = mDuration * 1000 - duration;
                        if (ret > 0)
                            Log.w("ANR-Watchdog-Demo", "Intercepted ANR that is too short (" + duration + " ms), postponing for " + ret + " ms.");
                        return ret;
                    }
                })
        ;

        anrWatchDog.start();
    }

}
