package com.yc.ycandroidtool;

import android.app.Application;

import com.yc.appstatuslib.AppStatusManager;
import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.listener.BaseStatusListener;
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
        AppStatusManager manager = new AppStatusManager.Builder()
                .context(this)
                .interval(5)
                .file(file)
                .builder();
        manager.registerAppStatusListener(new BaseStatusListener() {
            @Override
            public void wifiStatusChange(boolean isWifiOn) {
                super.wifiStatusChange(isWifiOn);
                if (isWifiOn){
                    ToolLogUtils.i("app status Wifi 打开");
                } else {
                    ToolLogUtils.i("app status Wifi 关闭");
                }
            }

            @Override
            public void gpsStatusChange(boolean isGpsOn) {
                super.gpsStatusChange(isGpsOn);
                if (isGpsOn){
                    ToolLogUtils.i("app status Gps 打开");
                } else {
                    ToolLogUtils.i("app status Gps 关闭");
                }
            }

            @Override
            public void networkStatusChange(boolean isConnect) {
                super.networkStatusChange(isConnect);
                if (isConnect){
                    ToolLogUtils.i("app status Network 打开");
                } else {
                    ToolLogUtils.i("app status Network 关闭");
                }
            }

            @Override
            public void screenStatusChange(boolean isScreenOn) {
                super.screenStatusChange(isScreenOn);
                if (isScreenOn){
                    ToolLogUtils.i("app status Screen 打开");
                } else {
                    ToolLogUtils.i("app status Screen 关闭");
                }
            }

            @Override
            public void screenUserPresent() {
                super.screenUserPresent();
                ToolLogUtils.i("app status Screen 使用了");
            }

            @Override
            public void appOnFrontOrBackChange(boolean isBack) {
                super.appOnFrontOrBackChange(isBack);
                if (isBack){
                    ToolLogUtils.i("app status app 推到后台");
                } else {
                    ToolLogUtils.i("app status app 回到前台");
                }
            }

            @Override
            public void bluetoothStatusChange(boolean isBluetoothOn) {
                super.bluetoothStatusChange(isBluetoothOn);
                if (isBluetoothOn){
                    ToolLogUtils.i("app status 蓝牙 打开");
                } else {
                    ToolLogUtils.i("app status 蓝牙 关闭");
                }
            }

            @Override
            public void batteryStatusChange(BatteryInfo batteryInfo) {
                super.batteryStatusChange(batteryInfo);
                ToolLogUtils.i("app status 电量 " + batteryInfo.toStringInfo());
            }
        });
    }

}
