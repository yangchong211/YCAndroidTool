package com.yc.mocklocationlib.gpsmock.utils;

import android.util.Log;

public final class LogMockGps {

    public static boolean sShowLog = true;

    private LogMockGps() {
    }

    public static void log(String tag, String msg) {
        if (sShowLog) {
            Log.d(tag, msg);
        }

    }


    public static void e(String tag, String msg) {
        if (sShowLog) {
            Log.e(tag, msg);
        }

    }
}
