package com.yc.appstatuslib;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.yc.appstatuslib.broadcast.BatteryBroadcastReceiver;
import com.yc.appstatuslib.broadcast.GpsBrodacastReceiver;
import com.yc.appstatuslib.broadcast.NetWorkBroadcastReceiver;
import com.yc.appstatuslib.broadcast.ScreenBroadcastReceiver;
import com.yc.appstatuslib.broadcast.WifiBroadcastReceiver;
import com.yc.appstatuslib.info.BatteryInfo;
import com.yc.appstatuslib.info.CollectionInfo;
import com.yc.appstatuslib.inter.AppStatusListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {

    private List<AppStatusListener> mAppStatusListener;
    private ResourceCollect mResourceCollect;
    private BatteryBroadcastReceiver mBatteryReceiver;
    private GpsBrodacastReceiver mGpsReceiver;
    private NetWorkBroadcastReceiver mNetWorkReceiver;
    private ScreenBroadcastReceiver mScreenReceiver;
    private WifiBroadcastReceiver mWifiBroadcastReceiver;
    private AppStatus mAppStatus;
    private Context mContext;

    private ResourceManager(ResourceManager.Builder builder) {
        this.mAppStatusListener = new ArrayList<>();
        this.mBatteryReceiver = new BatteryBroadcastReceiver(this);
        this.mGpsReceiver = new GpsBrodacastReceiver(this);
        this.mNetWorkReceiver = new NetWorkBroadcastReceiver(this);
        this.mScreenReceiver = new ScreenBroadcastReceiver(this);
        this.mWifiBroadcastReceiver = new WifiBroadcastReceiver(this);
        this.mAppStatus = new AppStatus(this);
        this.mContext = builder.context;
        this.mResourceCollect = (new ResourceCollect.Builder()).manager(this)
                .traceLog(builder.traceLog)
                .file(builder.file)
                .interval(builder.interval)
                .orderStatus(builder.status)
                .builder();
        this.init();
    }

    private void init() {
        if (this.mResourceCollect != null) {
            this.mResourceCollect.destroy();
        }

        this.initBatteryReceiver(this.mContext);
        this.initGpsReceiver(this.mContext);
        this.initNetworkReceiver(this.mContext);
        this.iniScreenReceiver(this.mContext);
        this.iniWifiReceiver(this.mContext);
        this.mResourceCollect.init();
        this.mAppStatus.init((Application)this.mContext.getApplicationContext());
    }

    private void initBatteryReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        context.registerReceiver(this.mBatteryReceiver, filter);
    }

    private void initGpsReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.location.PROVIDERS_CHANGED");
        context.registerReceiver(this.mGpsReceiver, filter);
    }

    private void initNetworkReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(this.mNetWorkReceiver, filter);
    }

    private void iniScreenReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(this.mScreenReceiver, filter);
    }

    private void iniWifiReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        context.registerReceiver(this.mWifiBroadcastReceiver, filter);
    }

    public void destroy() {
        this.mContext.unregisterReceiver(this.mBatteryReceiver);
        this.mContext.unregisterReceiver(this.mGpsReceiver);
        this.mContext.unregisterReceiver(this.mNetWorkReceiver);
        this.mContext.unregisterReceiver(this.mScreenReceiver);
        this.mAppStatusListener.clear();
        if (this.mResourceCollect != null) {
            this.mResourceCollect.destroy();
        }

    }

    public void collection() {
        if (this.mResourceCollect != null) {
            this.mResourceCollect.sendCollectionMsg();
        }

    }

    public BatteryInfo getBatteryInfo() {
        return this.mBatteryReceiver.getBatteryInfo();
    }


    public void dispatcherGpsOff() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).gpsStatusChange(false);
        }

    }

    public void dispatcherGpsOn() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;
        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).gpsStatusChange(true);
        }

    }

    public void dispatcherScreenOff() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).screenStatusChange(false);
        }

    }

    public void dispatcherScreenOn() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).screenStatusChange(true);
        }

    }

    public void dispatcherUserPresent() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).screenUserPresent();
        }

    }


    public void registerAppStatusListener(AppStatusListener listener) {
        if (this.mAppStatusListener != null) {
            this.mAppStatusListener.add(listener);
            this.mAppStatus.registerAppStatusListener(listener);
        }
    }

    public boolean unregisterAppStatusListener(AppStatusListener listener) {
        this.mAppStatus.unregisterAppStatusListener(listener);
        return mAppStatusListener != null && this.mAppStatusListener.remove(listener);
    }

    public void dispatcherNetworkConnection() {
        if (this.mAppStatusListener != null && this.mAppStatusListener.size() != 0) {
            Object[] listeners = this.mAppStatusListener.toArray();
            Object[] var2 = listeners;
            int var3 = listeners.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object listener = var2[var4];
                ((AppStatusListener)listener).networkStatusChange(true);
            }

        }
    }

    public void dispatcherNetworkDisConnection() {
        if (this.mAppStatusListener != null && this.mAppStatusListener.size() != 0) {
            Object[] listeners = this.mAppStatusListener.toArray();
            Object[] var2 = listeners;
            int var3 = listeners.length;
            for(int var4 = 0; var4 < var3; ++var4) {
                Object listener = var2[var4];
                ((AppStatusListener)listener).networkStatusChange(false);
            }
        }
    }

    public void dispatcherWifiOff() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).wifiStatusChange(false);
        }

    }

    public void dispatcherWifiOn() {
        Object[] listeners = this.mAppStatusListener.toArray();
        Object[] var2 = listeners;
        int var3 = listeners.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object listener = var2[var4];
            ((AppStatusListener)listener).wifiStatusChange(true);
        }

    }

    public int getAppStatus() {
        return this.mAppStatus.getAppStatus();
    }

    public interface TraceLog {
        void log(CollectionInfo var1);
    }

    public interface OrderStatus {
        boolean isOnline();

        boolean hasOrder();
    }

    public interface DimScreenSaver {
        boolean isScreenNeverDim();

        int getScreenMinLightness();

        int getDimDelay();
    }

    public static class Builder {
        /**
         * 时间间隔
         */
        private int interval;
        /**
         * 上下文
         */
        private Context context;
        /**
         * file文件
         */
        private File file;
        private ResourceManager.TraceLog traceLog;
        private ResourceManager.OrderStatus status;

        public Builder() {
        }

        public ResourceManager.Builder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public ResourceManager.Builder file(File file) {
            this.file = file;
            return this;
        }

        public ResourceManager.Builder context(Context context) {
            this.context = context;
            return this;
        }

        public ResourceManager.Builder traceLog(ResourceManager.TraceLog trace) {
            this.traceLog = trace;
            return this;
        }

        public ResourceManager.Builder orderStatus(ResourceManager.OrderStatus status) {
            this.status = status;
            return this;
        }

        public ResourceManager builder() {
            if (this.context == null) {
                throw new NullPointerException("context is null");
            } else if (this.file == null) {
                throw new NullPointerException("file is null");
            } else {
                if (this.interval == 0) {
                    this.interval = 6000;
                }

                return new ResourceManager(this);
            }
        }
    }
}