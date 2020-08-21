package com.yc.toollib.crash;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.NoSuchElementException;
import java.util.Stack;


/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/06/4
 *     desc  : activity栈管理器
 *     revise:
 * </pre>
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public Stack<Activity> getAllActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() throws NoSuchElementException {
        if (activityStack == null || (activityStack!=null && activityStack.size()<=0) ){
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity ,不包含指定的activity
     */
    public void finishAllActivity(Activity currentActivity) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && currentActivity != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
        activityStack.add(currentActivity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     */
    public <T extends Activity> T getActivity(Class<T> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return (T) activity;
                }
            }
        return null;
    }

    /**
     * 结束除了最底部一个以外的所有Activity
     */
    public void finishUpActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size - 1; i++) {
            activityStack.pop().finish();
        }
    }


    /**
     * 判断 Activity 是否存在栈中
     *
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean isActivityExistsInStack(@NonNull final Class<?> clz) {
        if(activityStack!=null && activityStack.size()>0){
            for (Activity aActivity : activityStack) {
                if (aActivity.getClass().equals(clz)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 判断mainactivity是否处于栈顶
     * @return  true在栈顶false不在栈顶
     */
    public boolean isActivityTop(Context mContext,String activityName){
        if(TextUtils.isEmpty(activityName)){
            return false;
        }
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if(manager.getRunningTasks(1)==null || (manager.getRunningTasks(1)!=null && manager.getRunningTasks(1).size()<=0)){
            return false;
        }
        if((manager.getRunningTasks(1).get(0)==null || (manager.getRunningTasks(1).get(0)!=null && manager.getRunningTasks(1).get(0).topActivity==null))){
            return false;
        }
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        if(TextUtils.isEmpty(name)){
            return false;
        }
        return name.equals(activityName);
    }
}