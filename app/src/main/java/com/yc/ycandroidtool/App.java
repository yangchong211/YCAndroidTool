package com.yc.ycandroidtool;

import android.app.Application;
import android.content.Context;

import com.yc.catonhelperlib.canary.BlockCanary;
import com.yc.catonhelperlib.watch.HandlerBlockTask;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.netlib.utils.NetworkTool;
import com.yc.ycandroidtool.canary.AppContext;

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
        NetworkTool.getInstance().init(this);
        //建议只在debug环境下显示，点击去网络拦截列表页面查看网络请求数据
        NetworkTool.getInstance().setFloat(this);
        HandlerBlockTask.getInstance().startWork();
        //WatchDog.getInstance().startWork();
        sContext = this;
        BlockCanary.install(this, new AppContext()).start();
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

}
