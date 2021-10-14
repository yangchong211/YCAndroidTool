package com.yc.appstatuslib;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Build.VERSION;

import com.yc.appstatuslib.info.CollectionInfo;
import com.yc.appstatuslib.log.CsvFormatLog;
import com.yc.appstatuslib.log.FormatStrategy;

import java.io.File;

class ResourceCollect {
    private Handler mHandler;
    private ResourceManager mResourceManager;
    private HandlerThread mHandlerThread;
    private FormatStrategy mFormatStrategy;
    private int mInterval;
    private ResourceManager.TraceLog mTraceLog;
    private ResourceManager.OrderStatus mOrderStatus;

    ResourceCollect(ResourceCollect.Builder builder) {
        this.mResourceManager = builder.manager;
        this.mFormatStrategy = builder.formatStrategy;
        this.mTraceLog = builder.traceLog;
        this.mInterval = builder.interval;
        this.mOrderStatus = builder.status;
    }

    void init() {
        this.mHandlerThread = new HandlerThread("resource-collect-thread");
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                ResourceCollect.this.collection();
            }
        };
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(), (long)this.mInterval);
    }

    private void collection() {
        this.mHandler.removeCallbacksAndMessages((Object)null);
        CollectionInfo collectionInfo = CollectionInfo.builder(this.mResourceManager.getBatteryInfo(), this.mOrderStatus, this.mResourceManager.getAppStatus());
        this.mFormatStrategy.log(collectionInfo);
        if (this.mTraceLog != null) {
            this.mTraceLog.log(collectionInfo);
        }

        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(), (long)this.mInterval);
    }

    void sendCollectionMsg() {
        this.mHandler.sendEmptyMessage(0);
    }

    void destroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object)null);
        }

        if (this.mHandlerThread != null) {
            if (VERSION.SDK_INT >= 18) {
                this.mHandlerThread.quitSafely();
            } else {
                this.mHandlerThread.quit();
            }
        }

    }

    static class Builder {
        private static final int ONE_MINUTE = 60000;
        int interval;
        ResourceManager manager;
        FormatStrategy formatStrategy;
        File file;
        ResourceManager.TraceLog traceLog;
        ResourceManager.OrderStatus status;

        Builder() {
        }

        ResourceCollect.Builder interval(int interval) {
            this.interval = interval;
            return this;
        }

        ResourceCollect.Builder file(File file) {
            this.file = file;
            return this;
        }

        ResourceCollect.Builder manager(ResourceManager manager) {
            this.manager = manager;
            return this;
        }

        ResourceCollect.Builder formatStrategy(FormatStrategy strategy) {
            this.formatStrategy = strategy;
            return this;
        }

        ResourceCollect.Builder traceLog(ResourceManager.TraceLog trace) {
            this.traceLog = trace;
            return this;
        }

        ResourceCollect.Builder orderStatus(ResourceManager.OrderStatus status) {
            this.status = status;
            return this;
        }

        ResourceCollect builder() {
            if (this.interval == 0) {
                this.interval = 60000;
            }

            if (this.formatStrategy == null) {
                this.formatStrategy = new CsvFormatLog(this.file);
            }

            return new ResourceCollect(this);
        }
    }
}

