package com.yc.ycandroidtool;

import android.app.Application;

import com.yc.appstatuslib.ResourceManager;
import com.yc.appstatuslib.listener.AppStatusListener;
import com.yc.appstatuslib.listener.GpsListener;
import com.yc.appstatuslib.listener.NetworkListener;
import com.yc.appstatuslib.listener.ScreenListener;
import com.yc.appstatuslib.listener.WifiListener;
import com.yc.catonhelperlib.HandlerBlockTask;
import com.yc.longevitylib.LongevityMonitor;
import com.yc.longevitylib.LongevityMonitorConfig;
import com.yc.netlib.utils.ToolFileUtils;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.netlib.utils.NetworkTool;
import com.yc.toollib.tool.ToolLogUtils;

import java.io.File;
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
        HandlerBlockTask.getInstance().startWork();
        //WatchDog.getInstance().startWork();
        initAppStatusListener();
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


    private void initAppStatusListener() {
        String cachePath = ToolFileUtils.getCachePath(this);
        String path = cachePath + File.separator + "status";
        File file = new File(path);
        ResourceManager manager = new ResourceManager.Builder()
                .context(this)
                .interval(5)
                .file(file)
                .builder();
        manager.registerAppStatusListener(new AppStatusListener() {
            @Override
            public void onAppFont() {
                ToolLogUtils.i("app status AppStatusListener onAppFont");
            }

            @Override
            public void onAppBack() {
                ToolLogUtils.i("app status AppStatusListener onAppBack");
            }

            @Override
            public void onActivityStarted() {
                ToolLogUtils.i("app status AppStatusListener onActivityStarted");
            }
        });
        manager.registerGpsListener(new GpsListener() {
            @Override
            public void gpsOn() {
                ToolLogUtils.i("app status GpsListener gpsOn");
            }

            @Override
            public void gpsOff() {
                ToolLogUtils.i("app status GpsListener gpsOff");
            }
        });
        manager.registerNetworkListener(new NetworkListener() {
            @Override
            public void connect() {
                ToolLogUtils.i("app status NetworkListener connect");
            }

            @Override
            public void disconnect() {
                ToolLogUtils.i("app status NetworkListener disconnect");
            }
        });
        manager.registerScreenListener(new ScreenListener() {
            @Override
            public void screenOn() {
                ToolLogUtils.i("app status ScreenListener screenOn");
            }

            @Override
            public void screenOff() {
                ToolLogUtils.i("app status ScreenListener screenOff");
            }

            @Override
            public void userPresent() {
                ToolLogUtils.i("app status ScreenListener userPresent");
            }
        });
        manager.registerWifiListener(new WifiListener() {
            @Override
            public void wifiOn() {
                ToolLogUtils.i("app status WifiListener wifiOn");
            }

            @Override
            public void wifiOff() {
                ToolLogUtils.i("app status WifiListener wifiOff");
            }
        });
    }

}
