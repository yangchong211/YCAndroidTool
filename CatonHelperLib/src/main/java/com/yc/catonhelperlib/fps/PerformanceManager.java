package com.yc.catonhelperlib.fps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerformanceManager {

    private final Handler mMainHandler;
    private final String fpsFileName;
    private final SimpleDateFormat simpleDateFormat;
    private int mLastFrameRate;
    private int mLastSkippedFrames;
    private Context mContext;
    private final FrameRateRunnable mRateRunnable;
    private static final String TAG = "PerformanceManager";

    public static PerformanceManager getInstance() {
        return PerformanceManager.Holder.INSTANCE;
    }


    public void init(Context context) {
        mContext = context.getApplicationContext();
    }


    private class FrameRateRunnable implements Runnable, Choreographer.FrameCallback {
        
        private int totalFramesPerSecond;

        private FrameRateRunnable() {
            
        }

        public void run() {
            mLastFrameRate = totalFramesPerSecond;
            if (mLastFrameRate > 60) {
                mLastFrameRate = 60;
            }
            mLastSkippedFrames = 60 - mLastFrameRate;
            totalFramesPerSecond = 0;
            mMainHandler.postDelayed(this, 1000L);
            Log.i(TAG,"fps runnable run");
        }

        public void doFrame(long frameTimeNanos) {
            ++totalFramesPerSecond;
            Choreographer.getInstance().postFrameCallback(this);
            writeFpsDataIntoFile();
        }
    }


    @SuppressLint("SimpleDateFormat")
    private PerformanceManager() {
        fpsFileName = "fps.txt";
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mLastFrameRate = 60;
        mMainHandler = new Handler(Looper.getMainLooper());
        mRateRunnable = new FrameRateRunnable();
    }

    public void startMonitorFrameInfo() {
        mMainHandler.postDelayed(mRateRunnable, 1000L);
        Choreographer.getInstance().postFrameCallback(mRateRunnable);
    }

    public void stopMonitorFrameInfo() {
        Choreographer.getInstance().removeFrameCallback(mRateRunnable);
        mMainHandler.removeCallbacks(mRateRunnable);
    }

    public void destroy() {
        stopMonitorFrameInfo();
    }

    private void writeFpsDataIntoFile() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mLastFrameRate);
        stringBuilder.append(" ");
        stringBuilder.append(simpleDateFormat.format(
                new Date(System.currentTimeMillis())));
        String string = stringBuilder.toString();
        String filePath = getFilePath(mContext);
        Log.i(TAG,"fps data is : "+string);
        //Samsung SM-A5160 Android 11, API 30
        Log.i(TAG,"fps data file path : "+filePath);
        ///data/user/0/com.com.yc.ycandroidtool/cache/yc/
        FileManager.writeTxtToFile(string, getFilePath(mContext), fpsFileName);
    }

    public String getFpsFilePath() {
        //获取file
        return getFilePath(mContext) + fpsFileName;
    }


    private String getFilePath(Context context) {
        return context.getCacheDir() + File.separator + "yc/";
    }

    public int getLastSkippedFrames() {
        return mLastSkippedFrames;
    }

    private static class Holder {
        
        private static final PerformanceManager INSTANCE = new PerformanceManager();

        private Holder() {
        }
    }

}
