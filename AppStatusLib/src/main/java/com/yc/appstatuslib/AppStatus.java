package com.yc.appstatuslib;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.SparseArray;


import com.yc.appstatuslib.listener.AppStatusListener;

import java.util.ArrayList;
import java.util.List;

public class AppStatus {

    private static final String TAG = "AppStatus";
    private List<AppStatusListener> mAppStatusListener = new ArrayList();
    private AppStatus.AppStatusLifecycleCallbacks mAppCallbacks = new AppStatus.AppStatusLifecycleCallbacks();
    private Application mApplication;
    private int mActivityStartedCount;
    private int mActivityResumedCount;
    private ResourceManager mResourceManager;

    AppStatus(ResourceManager manager) {
        this.mResourceManager = manager;
    }

    void registerAppStatusListener(AppStatusListener listener) {
        if (listener != null) {
            synchronized(this.mAppStatusListener) {
                this.mAppStatusListener.add(listener);
            }
        }
    }

    boolean unregisterAppStatusListener(AppStatusListener listener) {
        if (listener == null) {
            return false;
        } else {
            synchronized(this.mAppStatusListener) {
                return this.mAppStatusListener.remove(listener);
            }
        }
    }

    int getAppStatus() {
        return this.mActivityResumedCount;
    }

    public void destory() {
        this.mAppStatusListener.clear();
        this.mApplication.unregisterActivityLifecycleCallbacks(this.mAppCallbacks);
    }

    void init(Application application) {
        this.mApplication = application;
        application.registerActivityLifecycleCallbacks(this.mAppCallbacks);
    }

    private void dispatchAppOnFrontOrBack(boolean front) {
        if (this.mAppStatusListener != null && this.mAppStatusListener.size() != 0) {
            Object[] listeners = this.mAppStatusListener.toArray();
            Object[] var3;
            int var4;
            int var5;
            Object listener;
            if (front) {
                var3 = listeners;
                var4 = listeners.length;

                for(var5 = 0; var5 < var4; ++var5) {
                    listener = var3[var5];
                    ((AppStatusListener)listener).appOnFrontOrBackChange(false);
                }
            } else {
                var3 = listeners;
                var4 = listeners.length;

                for(var5 = 0; var5 < var4; ++var5) {
                    listener = var3[var5];
                    ((AppStatusListener)listener).appOnFrontOrBackChange(true);
                }
            }

        }
    }

    class AppStatusLifecycleCallbacks implements ActivityLifecycleCallbacks {
        private SparseArray<Integer> mResumedActivities = new SparseArray();

        AppStatusLifecycleCallbacks() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            if (activity != null) {
                int hash = activity.hashCode();
                if (this.mResumedActivities.indexOfKey(hash) > 0) {
                    return;
                }

                this.mResumedActivities.append(hash, 0);
            }

            AppStatus.this.mActivityResumedCount++;
            if (AppStatus.this.mActivityResumedCount == 1) {
                AppStatus.this.dispatchAppOnFrontOrBack(true);
                AppStatus.this.mResourceManager.collection();
            }

        }

        public void onActivityStopped(Activity activity) {
            if (activity != null) {
                int hash = activity.hashCode();
                if (this.mResumedActivities.indexOfKey(hash) > 0) {
                    this.mResumedActivities.remove(hash);
                }
            }

            AppStatus.this.mActivityResumedCount--;
            if (AppStatus.this.mActivityResumedCount == 0) {
                AppStatus.this.dispatchAppOnFrontOrBack(false);
                AppStatus.this.mResourceManager.collection();
            }

        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }
    }
}
