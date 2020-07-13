package com.yc.ycandroidtool;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public final class CrashToolUtils {

    /**
     * 退出app操作
     */
    private static void exitApp(){
        //finishActivity();
        //需要杀掉原进程，否则崩溃的app处于黑屏,卡死状态
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private static void finishActivity() {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity!=null && !activity.isFinishing() && activity instanceof MainActivity){
            //可将activity 退到后台，注意不是finish()退出。
            //判断Activity是否是task根
            if (activity.isTaskRoot()){
                //参数为false——代表只有当前activity是task根，指应用启动的第一个activity时，才有效;
                activity.moveTaskToBack(false);
            } else {
                //参数为true——则忽略这个限制，任何activity都可以有效。
                //使用此方法，便不会执行Activity的onDestroy()方法
                activity.moveTaskToBack(true);
            }
            //使用moveTaskToBack是为了让app退出时，不闪屏，退出柔和一些
        }
        //注意这里是finish所有activity，然后杀死进程
        AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 开启一个新的服务，用来重启本APP【使用handler延迟】
     * 软件重启，不清临时数据。
     * 重启整个APP
     * @param context                       上下文
     * @param Delayed                       延迟多少毫秒
     */
    public static void reStartApp1(Context context, long Delayed){
        Intent intent = new Intent(context,KillSelfService.class);
        intent.putExtra("PackageName",context.getPackageName());
        intent.putExtra("Delayed",Delayed);
        context.startService(intent);
        LogUtils.w(CrashHandler.TAG, "reStartApp--- 用来重启本APP--1---");
        exitApp();
    }

    /**
     * 用来重启本APP[使用闹钟，整体重启，临时数据清空（推荐）]
     * 重启整个APP
     * @param context                       上下文
     * @param Delayed                       延迟多少毫秒
     */
    public static void reStartApp2(Context context , long Delayed){
        //暂时先跳转到启动页页面
        //todo 后期看能不能恢复任务栈，恢复到栈顶activity
        //Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        //intent.putExtra("REBOOT","reboot");
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, intent,PendingIntent.FLAG_ONE_SHOT);
        //退出程序
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + Delayed,restartIntent);
        LogUtils.w(CrashHandler.TAG, "reStartApp--- 用来重启本APP--2---");
        exitApp();
    }


}
