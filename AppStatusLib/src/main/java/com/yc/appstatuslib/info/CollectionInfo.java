package com.yc.appstatuslib.info;


import android.content.res.Resources;
import android.os.Build;

import com.yc.appstatuslib.ResourceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CollectionInfo {

    private static final SimpleDateFormat sDateFormat;
    private static final String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
    public BatteryInfo batteryInfo = new BatteryInfo();
    public CpuInfo cpuInfo = new CpuInfo();
    public boolean isOnline;
    public boolean hasOrder;
    public String currentTime;
    public int appStatus;

    static {
        Locale sysLocale = getSysLocale();
        sDateFormat = new SimpleDateFormat(pattern, sysLocale);
    }

    public CollectionInfo() {

    }

    public static CollectionInfo builder(BatteryInfo batteryInfo,
                                         ResourceManager.OrderStatus status,
                                         int appStatus) {
        CollectionInfo collectionInfo = new CollectionInfo();
        collectionInfo.batteryInfo = batteryInfo;
        collectionInfo.cpuInfo = CpuInfo.builder();
        collectionInfo.appStatus = appStatus;
        collectionInfo.currentTime = sDateFormat.format(new Date());
        if (status != null) {
            collectionInfo.isOnline = status.isOnline();
            collectionInfo.hasOrder = status.hasOrder();
        }

        return collectionInfo;
    }

    public String toString() {
        return "CollectionInfo{batteryInfo=" + this.batteryInfo + ", cpuInfo=" + this.cpuInfo + ", isOnline=" + this.isOnline + ", hasOrder=" + this.hasOrder + ", currentTime='" + this.currentTime + '\'' + ", appStatus=" + this.appStatus + '}';
    }

    public static Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        if (locale == null) {
            locale = new Locale("en-US");
        }
        return locale;
    }

}
