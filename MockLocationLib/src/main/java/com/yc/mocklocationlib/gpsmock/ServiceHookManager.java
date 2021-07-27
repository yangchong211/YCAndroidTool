package com.yc.mocklocationlib.gpsmock;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServiceHookManager {

    private static final String CLASS_SERVICE_MANAGER = "android.os.ServiceManager";
    private static final String METHOD_GET_SERVICE = "getService";
    private static final String FIELD_S_CACHE = "sCache";
    private boolean isHookSuccess;
    private List<BaseServiceHooker> mHookers;

    public static ServiceHookManager getInstance() {
        return ServiceHookManager.Holder.INSTANCE;
    }

    private ServiceHookManager() {
        this.mHookers = new ArrayList();
        this.init();
    }

    private void init() {
        this.mHookers.add(new WifiHooker());
        this.mHookers.add(new TelephonyHooker());
    }

    @SuppressLint({"PrivateApi"})
    public void install(Context context) {
        try {
            Class serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getDeclaredMethod("getService", String.class);
            Iterator var4 = this.mHookers.iterator();

            while(var4.hasNext()) {
                BaseServiceHooker hooker = (BaseServiceHooker)var4.next();
                IBinder binder = (IBinder)getService.invoke((Object)null, hooker.getServiceName());
                if (binder == null) {
                    return;
                }

                ClassLoader classLoader = binder.getClass().getClassLoader();
                Class[] interfaces = new Class[]{IBinder.class};
                BinderHookHandler handler = new BinderHookHandler(binder, hooker);
                hooker.setBinder(binder);
                IBinder proxy = (IBinder)Proxy.newProxyInstance(classLoader, interfaces, handler);
                hooker.replaceBinder(context, proxy);
                Field sCache = serviceManager.getDeclaredField("sCache");
                sCache.setAccessible(true);
                Map<String, IBinder> cache = (Map)sCache.get((Object)null);
                cache.put(hooker.getServiceName(), proxy);
                sCache.setAccessible(false);
            }

            this.isHookSuccess = true;
        } catch (ClassNotFoundException var13) {
            var13.printStackTrace();
        } catch (NoSuchMethodException var14) {
            var14.printStackTrace();
        } catch (IllegalAccessException var15) {
            var15.printStackTrace();
        } catch (InvocationTargetException var16) {
            var16.printStackTrace();
        } catch (NoSuchFieldException var17) {
            var17.printStackTrace();
        }

    }

    public boolean isHookSuccess() {
        return this.isHookSuccess;
    }

    private static class Holder {
        private static ServiceHookManager INSTANCE = new ServiceHookManager();

        private Holder() {
        }
    }
}

