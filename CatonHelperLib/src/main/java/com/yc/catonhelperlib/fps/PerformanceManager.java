package com.yc.catonhelperlib.fps;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerformanceManager {

    private Handler mMainHandler;
    private String fpsFileName;
    private SimpleDateFormat simpleDateFormat;
    private int mLastFrameRate;
    private int mLastSkippedFrames;
    private Context mContext;
    private FrameRateRunnable mRateRunnable;


    public static PerformanceManager getInstance() {
        return PerformanceManager.Holder.INSTANCE;
    }


    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }


    private class FrameRateRunnable implements Runnable, Choreographer.FrameCallback {
        private int totalFramesPerSecond;

        private FrameRateRunnable() {
        }

        public void run() {
            PerformanceManager.this.mLastFrameRate = this.totalFramesPerSecond;
            if (PerformanceManager.this.mLastFrameRate > 60) {
                PerformanceManager.this.mLastFrameRate = 60;
            }

            PerformanceManager.this.mLastSkippedFrames = 60 - PerformanceManager.this.mLastFrameRate;
            this.totalFramesPerSecond = 0;
            PerformanceManager.this.mMainHandler.postDelayed(this, 1000L);
        }

        public void doFrame(long frameTimeNanos) {
            ++this.totalFramesPerSecond;
            Choreographer.getInstance().postFrameCallback(this);
            PerformanceManager.this.writeFpsDataIntoFile();
        }
    }


    private PerformanceManager() {
        this.fpsFileName = "fps.txt";
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.mLastFrameRate = 60;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mRateRunnable = new FrameRateRunnable();
    }

    public void startMonitorFrameInfo() {
        this.mMainHandler.postDelayed(this.mRateRunnable, 1000L);
        Choreographer.getInstance().postFrameCallback(this.mRateRunnable);
    }

    public void stopMonitorFrameInfo() {
        Choreographer.getInstance().removeFrameCallback(this.mRateRunnable);
        this.mMainHandler.removeCallbacks(this.mRateRunnable);
    }

    public void destroy() {
        this.stopMonitorFrameInfo();
    }

    private void writeFpsDataIntoFile() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mLastFrameRate);
        stringBuilder.append(" ");
        stringBuilder.append(this.simpleDateFormat.format(
                new Date(System.currentTimeMillis())));
        FileManager.writeTxtToFile(stringBuilder.toString(),
                this.getFilePath(this.mContext), this.fpsFileName);
    }

    public String getFpsFilePath() {
        //
        return this.getFilePath(this.mContext) + this.fpsFileName;
    }


    private String getFilePath(Context context) {
        return context.getCacheDir() + File.separator + "yc/";
    }

    public int getLastSkippedFrames() {
        return this.mLastSkippedFrames;
    }

    private static class Holder {
        private static PerformanceManager INSTANCE = new PerformanceManager();

        private Holder() {
        }
    }

}
