package com.yc.toollib.crash.tools;

import android.util.Log;

import com.yc.toollib.crash.core.Recovery;



public class RecoveryLog {

    private static final String TAG = "Recovery";

    public static void e(String message) {
        if (Recovery.getInstance().isDebug())
            Log.e(TAG, message);
    }
}
