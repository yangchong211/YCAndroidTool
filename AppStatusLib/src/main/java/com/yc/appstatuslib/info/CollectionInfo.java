package com.yc.appstatuslib.info;


import com.yc.appstatuslib.ResourceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CollectionInfo {
    private static SimpleDateFormat sDateFormat;
    public BatteryInfo batteryInfo = new BatteryInfo();
    public CpuInfo cpuInfo = new CpuInfo();
    public boolean isOnline;
    public boolean hasOrder;
    public String currentTime;
    public int appStatus;

    public CollectionInfo() {
    }

    public static CollectionInfo builder(BatteryInfo batteryInfo, ResourceManager.OrderStatus status, int appStatus) {
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

    static {
        sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
    }
}
