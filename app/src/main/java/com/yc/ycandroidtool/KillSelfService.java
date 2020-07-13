package com.yc.ycandroidtool;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/5/6
 *     desc  : 重启开启app
 *     revise:
 * </pre>
 */
public class KillSelfService extends Service {


    private String packageName;
    private Handler handler;

    public KillSelfService() {
        handler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        long stopDelayed = intent.getLongExtra("Delayed", 2000);
        packageName = intent.getStringExtra("PackageName");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.w(CrashHandler.TAG, "KillSelfService---打开app---"+packageName);
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                startActivity(LaunchIntent);
                KillSelfService.this.stopSelf();
            }
        },stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
