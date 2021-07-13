package com.yc.longevitylib;


import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 保活
 *     revise:
 * </pre>
 */
public class LongevityMonitor {

    private static final long INIT_DELAY_TIME_OFFSET = 5000L;
    private static final long WATCH_DOG_TIMER_INTERVAL = 15000L;
    private static final long WATCH_DOG_PERIOD_THRESHOLD = 60000L;
    private static final long WATCH_DOG_INTERVAL_UP_LIMIT = 43200000L;
    private static final long TIMESTAMP_NO_VALUE = 0L;
    private static final int PID_NO_VALUE = 0;
    private static Application sApplication;
    private static LongevityMonitorConfig.ILongevityMonitorApolloToggle sToggle;
    private static LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack sEventTrack;
    public static LongevityMonitorConfig.ILongevityMonitorLogger sLogger;
    private static final Handler sHandler = new Handler(Looper.getMainLooper());
    private static SharedPreferences sSP;
    private static long sLastLiveTimeStamp;
    private static int sLastPid;
    private static int sLastScreenState;
    public static int sCurrentScreenState = 1;
    private static Bundle sSavedInstanceState;
    private static final Runnable sWatchDogRunnable = new Runnable() {
        public void run() {
            if (LongevityMonitor.sToggle.isOpen()) {
                long currentTimestamp = System.currentTimeMillis();
                int currentPid = LongevityMonitor.getCurrentPid();
                long periodMillis = currentTimestamp - LongevityMonitor.sLastLiveTimeStamp;
                if (periodMillis > WATCH_DOG_PERIOD_THRESHOLD && periodMillis < WATCH_DOG_INTERVAL_UP_LIMIT) {
                    if (currentPid == LongevityMonitor.sLastPid) {
                        LongevityMonitor.report("longevity_monitor_event_sleep", "longevity_monitor_event_sleep_type_sleep", periodMillis, currentTimestamp, LongevityMonitor.sLastLiveTimeStamp);
                    } else if (LongevityMonitor.sSavedInstanceState != null) {
                        LongevityMonitor.report("longevity_monitor_event_system_kill", "", periodMillis, currentTimestamp, LongevityMonitor.sLastLiveTimeStamp);
                    } else if (LongevityMonitor.sLastScreenState == 2) {
                        LongevityMonitor.report("longevity_monitor_event_sleep", "longevity_monitor_event_sleep_type_kill", periodMillis, currentTimestamp, LongevityMonitor.sLastLiveTimeStamp);
                    } else {
                        LongevityMonitor.sLogger.log("longevity_monitor_event_user_kill " + periodMillis);
                    }
                }

                if (periodMillis > WATCH_DOG_INTERVAL_UP_LIMIT) {
                    LongevityMonitor.sLogger.log("interval exceed limit " + periodMillis);
                }

                Editor editor = LongevityMonitor.sSP.edit();
                editor.putLong("ts", currentTimestamp);
                editor.putInt("pid", currentPid);
                editor.putInt("screen_state", LongevityMonitor.sCurrentScreenState);
                editor.apply();
                LongevityMonitor.sLastLiveTimeStamp = currentTimestamp;
                LongevityMonitor.sLastPid = currentPid;
                LongevityMonitor.sLastScreenState = LongevityMonitor.sCurrentScreenState;
                LongevityMonitor.sHandler.postDelayed(this, WATCH_DOG_TIMER_INTERVAL);
                LongevityMonitor.sLogger.log("postDelayed-----sWatchDogRunnable");
            } else {
                LongevityMonitor.sLogger.log("boolean isOpen() set false");
            }
        }
    };

    public LongevityMonitor() {
    }

    public static void init(LongevityMonitorConfig config) {
        sApplication = config.getApplication();
        sToggle = config.getToggle();
        sEventTrack = config.getEventTrack();
        sLogger = config.getLogger();
        LongevityScreenReceiver.register(sApplication);
    }

    public static void onActivityCreate(Bundle savedInstanceState) {
        if (sToggle.isOpen()) {
            sLogger.log("savedInstanceState is null ?\t" + (savedInstanceState == null));
            sSavedInstanceState = savedInstanceState;
            sSP = sApplication.getSharedPreferences("longevity_monitor", 0);
            sLastLiveTimeStamp = sSP.getLong("ts", TIMESTAMP_NO_VALUE);
            sLastPid = sSP.getInt("pid", PID_NO_VALUE);
            sLastScreenState = sSP.getInt("screen_state", 3);
            if (sLastLiveTimeStamp == TIMESTAMP_NO_VALUE || sLastPid == PID_NO_VALUE) {
                sLastLiveTimeStamp = System.currentTimeMillis();
                sLastPid = getCurrentPid();
            }

            restartHandler();
        }
    }

    private static void restartHandler() {
        sHandler.removeCallbacks(sWatchDogRunnable);
        sHandler.postDelayed(sWatchDogRunnable, INIT_DELAY_TIME_OFFSET);
        sLogger.log("restartHandler-----"+INIT_DELAY_TIME_OFFSET);
    }

    private static int getCurrentPid() {
        return Process.myPid();
    }

    private static void report(String eventName, String eventType, long periodMillis
            , long currentTimestamp, long lastTimestamp) {
        HashMap<String, String> params = new HashMap();
        params.put("event", eventName);
        params.put("type", TextUtils.isEmpty(eventType) ? "" : eventType);
        params.put("period", String.valueOf(periodMillis));
        params.put("ts", String.valueOf(currentTimestamp));
        params.put("ts_last", String.valueOf(lastTimestamp));
        sEventTrack.onEvent(params);
        sLogger.log(params.toString());
    }
}
