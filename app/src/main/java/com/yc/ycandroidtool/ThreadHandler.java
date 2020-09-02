package com.yc.ycandroidtool;

import android.app.Application;

import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashHelper;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toollib.tool.ToolLogUtils;

@Deprecated
public class ThreadHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private boolean isInit = false;
    /**
     * CrashHandler实例
     */
    private static ThreadHandler INSTANCE;

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static ThreadHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadHandler();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     * 该方法来实现对运行时线程进行异常处理
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mDefaultHandler != null) {
            //收集完信息后，交给系统自己处理崩溃
            //uncaughtException (Thread t, Throwable e) 是一个抽象方法
            //当给定的线程因为发生了未捕获的异常而导致终止时将通过该方法将线程对象和异常对象传递进来。
            mDefaultHandler.uncaughtException(t, e);
        } else {
            //否则自己处理
        }
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     * @param ctx
     */
    public void init(Application ctx) {
        if (isInit){
            return;
        }
        //获取系统默认的UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        //设置一个处理者当一个线程突然因为一个未捕获的异常而终止时将自动被调用。
        //未捕获的异常处理的控制第一个被当前线程处理，如果该线程没有捕获并处理该异常，其将被线程的ThreadGroup对象处理，最后被默认的未捕获异常处理器处理。
        Thread.setDefaultUncaughtExceptionHandler(this);
        isInit = true;
    }
}
