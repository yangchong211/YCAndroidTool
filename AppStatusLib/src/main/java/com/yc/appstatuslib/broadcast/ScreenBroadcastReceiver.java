package com.yc.appstatuslib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yc.appstatuslib.ResourceManager;

public class ScreenBroadcastReceiver extends BroadcastReceiver {

    private ResourceManager mManager;

    public ScreenBroadcastReceiver(ResourceManager resourceManager) {
        this.mManager = resourceManager;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.mManager != null) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                this.mManager.dispatcherScreenOn();
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                this.mManager.dispatcherScreenOff();
            } else if ("android.intent.action.USER_PRESENT".equals(action)) {
                this.mManager.dispatcherUserPresent();
            }

        }
    }
}
