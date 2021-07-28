package com.yc.ycandroidtool;

import android.app.Application;

import com.yc.longevitylib.LongevityMonitor;
import com.yc.longevitylib.LongevityMonitorConfig;
import com.yc.mocklocationlib.gpsmock.ServiceHookManager;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashTestDemo;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.tool.ToolLogUtils;

import java.util.HashMap;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrash();
        NetworkTool.getInstance().init(this);
        //建议只在debug环境下显示，点击去网络拦截列表页面查看网络请求数据
        NetworkTool.getInstance().setFloat(this);
        init(this);

        //Hook WIFI GPS Telephony系统服务
        ServiceHookManager.getInstance().install(this);
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

    private void test(){
        CrashTestDemo.test();
    }


    /**
     * 初始化
     */
    public static void init(Application application) {
        LongevityMonitor.init(new LongevityMonitorConfig.Builder(application)
            // 业务埋点
            .setEventTrack(new LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack() {
                @Override
                public void onEvent(HashMap<String, String> hashMap) {

                }
            })
            // 保活监控 Apollo
            .setToggle(new LongevityMonitorConfig.ILongevityMonitorApolloToggle() {
                @Override
                public boolean isOpen() {
                    return true;
                }
            })
            // 日志输出
            .setLogger(new LongevityMonitorConfig.ILongevityMonitorLogger() {
                @Override
                public void log(String var1) {
                    ToolLogUtils.i("Longevity--"+var1);
                }
            })
            .build());
    }
}
