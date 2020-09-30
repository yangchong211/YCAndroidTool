package com.yc.toollib.crash;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2018/5/6
 *     desc  : 进程工具类
 *     revise:
 * </pre>
 */
public final class ProcessUtils {

    private static String currentProcessName;

    /**
     * 获取当前进程名
     * 我们优先通过 Application.getProcessName() 方法获取进程名。
     * 如果获取失败，我们再反射ActivityThread.currentProcessName()获取进程名
     * 如果失败，我们才通过常规方法ActivityManager来获取进程名
     * @return                      当前进程名
     */
    @Nullable
    public static String getCurrentProcessName(@NonNull Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //1)通过Application的API获取当前进程名
        currentProcessName = getCurrentProcessNameByApplication();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //2)通过反射ActivityThread获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityThread();
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        //3)通过ActivityManager获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityManager(context);
        return currentProcessName;
    }

    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    public static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //Application.getProcessName()方法直接返回当前进程名。
            //这个方法只有在android9【也就是aip28】之后的系统才能调用
            return Application.getProcessName();
        }
        return null;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    public static String getCurrentProcessNameByActivityThread() {
        String processName = null;
        try {
            //ActivityThread.currentProcessName()方法居然是public static的
            @SuppressLint("PrivateApi")
            final Method declaredMethod = Class.forName("android.app.ActivityThread",
                    false, Application.class.getClassLoader())
                    .getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null, new Object[0]);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processName;
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     * 1。ActivityManager.getRunningAppProcesses() 方法需要跨进程通信，效率不高。
     * 需要 和 系统进程的 ActivityManagerService 通信。必然会导致该方法调用耗时。
     * 2。拿到RunningAppProcessInfo的列表之后，还需要遍历一遍找到与当前进程的信息。
     * 3。ActivityManager.getRunningAppProcesses() 有可能调用失败，返回null，也可能 AIDL 调用失败。调用失败是极低的概率。
     */
    public static String getCurrentProcessNameByActivityManager(@NonNull Context context) {
        if (context == null) {
            return null;
        }
        //指的是Process的id。每个进程都有一个独立的id，可以通过pid来区分不同的进程。
        int pid = android.os.Process.myPid( );
        ActivityManager am = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            //获取当前正在运行的进程
            List<ActivityManager.RunningAppProcessInfo> runningAppList = am.getRunningAppProcesses();
            if (runningAppList != null) {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningAppList) {
                    //相应的RunningServiceInfo的pid
                    if (processInfo.pid == pid) {
                        return processInfo.processName;
                    }
                }
            }
        }
        return null;
    }

}
