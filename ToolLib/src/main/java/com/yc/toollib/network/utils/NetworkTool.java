package com.yc.toollib.network.utils;

import android.app.Application;

import com.yc.toollib.crash.CrashHandler;

public class NetworkTool {

    private static NetworkTool INSTANCE;
    private Application application;

    /**
     * 获取NetworkTool实例 ,单例模式
     */
    public static NetworkTool getInstance() {
        if (INSTANCE == null) {
            synchronized (NetworkTool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetworkTool();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Application application){
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }
}
