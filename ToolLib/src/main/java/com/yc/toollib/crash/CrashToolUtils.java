package com.yc.toollib.crash;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yc.toollib.tool.ToolAppManager;
import com.yc.toollib.tool.ToolLogUtils;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 工具类
 *     revise: demo
 * </pre>
 */
public final class CrashToolUtils {

    /**
     * 后期需求，添加崩溃重启后，恢复activity任务栈操作和数据
     * 1.如何保存任务栈
     * 2.activity重启后数据恢复[自动恢复Activity Stack和数据]
     * 3.崩溃信息的保存显示，以及是否添加过期清除
     * 4.开闭原则，支持拓展性，后期上报数据到自己服务器【待定】
     * 5.是清空缓存处理还是重启app
     * 6.
     */

    /**
     * 退出app操作
     */
    private static void exitApp(){
        finishActivity();
        killCurrentProcess(true);
    }

    /**
     * 杀死进程操作，默认为异常退出
     * System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序
     * System.exit(1)一般放在catch块中，当捕获到异常，需要停止程序。这个status=1是用来表示这个程序是非正常退出。
     * @param isThrow                           是否是异常退出
     */
    public static void killCurrentProcess(boolean isThrow) {
        //需要杀掉原进程，否则崩溃的app处于黑屏,卡死状态
        android.os.Process.killProcess(android.os.Process.myPid());
        if (isThrow){
            System.exit(10);
        } else {
            System.exit(0);
        }
    }

    private static void finishActivity() {
        Activity activity = ToolAppManager.getAppManager().currentActivity();
        if (activity!=null && !activity.isFinishing()){
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
    }

    private static void finishAllActivity(){
        ToolAppManager.getAppManager().finishAllActivity();
    }

    /**
     * 开启一个新的服务，用来重启本APP【使用handler延迟】
     * 软件重启，不清临时数据。
     * 重启整个APP
     * @param context                       上下文
     * @param Delayed                       延迟多少毫秒
     */
    public static void reStartApp1(Context context, long Delayed){
        //finishActivity();
        Intent intent = new Intent(context, KillSelfService.class);
        intent.putExtra("PackageName",context.getPackageName());
        intent.putExtra("Delayed",Delayed);
        context.startService(intent);
        ToolLogUtils.w(CrashHandler.TAG, "reStartApp--- 用来重启本APP--1---");
        //exitApp();
        killCurrentProcess(true);
    }

    /**
     * 用来重启本APP[使用闹钟，整体重启，临时数据清空（推荐）]
     * 重启整个APP
     * @param context                       上下文
     * @param Delayed                       延迟多少毫秒
     */
    public static void reStartApp2(Context context , long Delayed , Class clazz){
        //finishActivity();
        //Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intent = new Intent(context.getApplicationContext(), clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);*/
        if (intent.getComponent() != null) {
            //如果类名已经设置，我们强制它模拟启动器启动。
            //如果我们不这样做，如果你从错误活动重启，然后按home，
            //然后从启动器启动活动，主活动将在backstack上出现两次。
            //这很可能不会有任何有害的影响，因为如果你设置了Intent组件，
            //if将始终启动，而不考虑此处指定的操作。
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        PendingIntent restartIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, intent,PendingIntent.FLAG_ONE_SHOT);
        //退出程序
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + Delayed,restartIntent);
        ToolLogUtils.w(CrashHandler.TAG, "reStartApp--- 用来重启本APP--2---"+clazz);
        //exitApp();
        killCurrentProcess(true);
    }

    public static void reStartApp3(Context context) {
        String packageName = context.getPackageName();
        Activity activity = ToolAppManager.getAppManager().currentActivity();
        Class<? extends Activity> clazz = guessRestartActivityClass(activity);
        ToolLogUtils.w(CrashHandler.TAG, "reStartApp--- 用来重启本APP--3-"+packageName + "--"+clazz);
        Intent intent = new Intent(activity, clazz);
        restartApplicationWithIntent(activity, intent);
    }

    /**
     * 日志列表页面
     *
     * @param context                       上下文
     */
    public static void startCrashListActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), CrashListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    /**
     * 测试异常类
     *
     * @param context                       上下文
     */
    public static void startCrashTestActivity(Context context) {
        Intent intent = new Intent(context, CrashTestActivity.class);
        context.startActivity(intent);
    }


    private static void restartApplicationWithIntent(@NonNull Activity activity, @NonNull Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
            //如果类名已经设置，我们强制它模拟启动器启动。
            //如果我们不这样做，如果你从错误活动重启，然后按home，
            //然后从启动器启动活动，主活动将在backstack上出现两次。
            //这很可能不会有任何有害的影响，因为如果你设置了Intent组件，
            //if将始终启动，而不考虑此处指定的操作。
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        activity.startActivity(intent);
        activity.finish();
        killCurrentProcess(true);
    }


    @Nullable
    private static Class<? extends Activity> guessRestartActivityClass(@NonNull Context context) {
        Class<? extends Activity> resolvedActivityClass;
        resolvedActivityClass = getRestartActivityClassWithIntentFilter(context);
        if (resolvedActivityClass == null) {
            resolvedActivityClass = getLauncherActivity(context);
        }
        return resolvedActivityClass;
    }


    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getRestartActivityClassWithIntentFilter(@NonNull Context context) {
        Intent searchedIntent = new Intent().setPackage(context.getPackageName());
        //检索可以为给定意图执行的所有活动
        List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentActivities(searchedIntent,
                PackageManager.GET_RESOLVED_FILTER);
        if (resolveInfo.size() > 0) {
            ResolveInfo info = resolveInfo.get(0);
            try {
                return (Class<? extends Activity>) Class.forName(info.activityInfo.name);
            } catch (ClassNotFoundException e) {
                ToolLogUtils.e(CrashHandler.TAG+e.getMessage());
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getLauncherActivity(@NonNull Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null && intent.getComponent() != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                ToolLogUtils.e(CrashHandler.TAG+e.getLocalizedMessage());
            }
        }
        return null;
    }

}
