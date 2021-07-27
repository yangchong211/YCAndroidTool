package com.yc.mocklocationlib.gpsmock;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

import com.yc.mocklocationlib.gpsmock.bean.LatLng;
import com.yc.mocklocationlib.gpsmock.point.GcjPointer;
import com.yc.mocklocationlib.gpsmock.point.WgsPointer;
import com.yc.mocklocationlib.gpsmock.utils.GpsMockConfig;
import com.yc.mocklocationlib.gpsmock.utils.LogMockGps;

import java.util.List;

public final class MockGpsManager {

    private static final String TAG = "MockGpsManager";
    private static float sBearing = 0.0F;
    private static float sSpeed = 3.0F;
    private static MockGpsManager sInstance;
    int triedInit;
    private LocationManager mLocationManager;
    private Location mLocation = new Location("gps");
    private WgsPointer mLastPoint;
    private MockGpsManager.OnMessage mOnMessage;
    private boolean mIsAvailable;
    private Handler mHandlerUI;
    private Handler mHandlerWorker;
    private Context mContext;

    private final Runnable laterInitRunnable = new Runnable() {
        public void run() {
            mHandlerWorker.removeCallbacks(laterInitRunnable);
            try {
                List<String> providers = mLocationManager.getProviders(true);
                LogMockGps.log("MockGpsManager", "当前可用的 providers 有:" + providers);
                mLocationManager.addTestProvider("gps", false, false, false, false, true, true, true, 0, 5);
                mLocationManager.setTestProviderEnabled("gps", true);
                mIsAvailable = true;
            } catch (Exception var6) {
                String msg = "请到开发者选项中打开模拟定位\n初始化失败 tried " + triedInit;
                onMessage(msg + var6.getMessage());
                if (triedInit < 3) {
                    mHandlerUI.postDelayed(laterInitRunnable, (long)(triedInit * 2000));
                    LogMockGps.log("MockGpsManager", msg);
                }
                disableTestProvider();
                LogMockGps.e("MockGpsManager", msg);
            } finally {
                ++triedInit;
            }

        }
    };
    private Runnable mockGpsRunnable = new Runnable() {
        public void run() {
            mHandlerWorker.removeCallbacks(mockGpsRunnable);
            double latitude = GpsMockManager.getInstance().getLatitude();
            double longitude = GpsMockManager.getInstance().getLongitude();
            teleportWgs(latitude,longitude);
            if (GpsMockManager.getInstance().isMocking()) {
                mHandlerWorker.postDelayed(this, 3000L);
            }

        }
    };

    private MockGpsManager(@NonNull Context context) {
        LogMockGps.log("MockGpsManager", "模拟路测工具初始化");
        HandlerThread handlerThread = new HandlerThread("MockGpsManager Thread");
        handlerThread.start();
        this.mHandlerWorker = new Handler(handlerThread.getLooper());
        this.mHandlerUI = new Handler(Looper.getMainLooper());
        this.mContext = context.getApplicationContext();
        //获取LocationManager对象
        this.mLocationManager = (LocationManager)
                mContext.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        this.removeTestProvider();
        this.mHandlerUI.post(this.laterInitRunnable);
    }

    public static float getBearing() {
        return sBearing;
    }

    public static void setBearing(float bearing) {
        LogMockGps.log("MockGpsManager", "⚠️setBearing() called with: bearing = [" + bearing + "]");
        sBearing = bearing;
    }

    public static float getSpeed() {
        return sSpeed;
    }

    public static void setSpeed(float speed) {
        LogMockGps.log("MockGpsManager", "⚠️setSpeed() called with: speed = [" + speed + "]");
        sSpeed = speed;
    }

    public static String getSpeedBearingStr() {
        return "sSpeed = [" + sSpeed + "], bearing = [" + sBearing + "]";
    }

    public static MockGpsManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MockGpsManager(context);
        }

        return sInstance;
    }

    public void disableTestProvider() {
        try {
            if (this.mLocationManager == null) {
                this.mLocationManager = (LocationManager)this.mContext.getApplicationContext().getSystemService("location");
            }

            this.removeTestProvider();
            this.mLocationManager.setTestProviderEnabled("gps", false);
        } catch (Exception var2) {
        }

    }

    public boolean changeMockLoc(@NonNull Context context, double latitude, double longitude) {
        if (longitude <= 180.0D && longitude >= -180.0D) {
            if (latitude <= 90.0D && latitude >= -90.0D) {
                GpsMockManager.getInstance().mockLocation(latitude, longitude);
                GpsMockConfig.saveMockLocation(context, new LatLng(latitude, longitude));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void start() {
        if (GpsMockManager.getInstance().isMocking()) {
            //发送消息
            this.mHandlerWorker.post(this.mockGpsRunnable);
        }

    }

    public void stop() {
        this.mHandlerWorker.removeCallbacks(this.mockGpsRunnable);
    }

    @SuppressLint({"DefaultLocale"})
    public void teleportGcj(double lat, double lng) {
        GcjPointer pointer = new GcjPointer(lat, lng);
        WgsPointer pointerWgs = pointer.toWgsPointer();
        LogMockGps.log("MockGpsManager", String.format("MockGpsManager 请求模拟定位 火星坐标 lat=%g, lng=%g", lat, lng));
        this.teleportWgs(pointerWgs.getLatitude(), pointerWgs.getLongitude());
    }

    public void teleportWgs(double lat, double lng) {
        if (!this.mIsAvailable) {
            String msg = "第一、设置我们手机允许模拟定位【系统设置】=》开发者选项=》打开允许模拟位置\n第二、进入位置服务切换成只是用gps确定位置";
            this.onMessage(msg);
            LogMockGps.log("MockGpsManager", "⚠️teleportWgs() called with mIsAvailable: lat = [" + lat + "], lng = [" + lng + "], sSpeed = [" + sSpeed + "], bearing = [" + sBearing + "]");
        }

        try {
            WgsPointer pointerWgs = new WgsPointer(lat, lng);
            this.mLastPoint = pointerWgs;
            this.mLocation.setLatitude(pointerWgs.getLatitude());
            this.mLocation.setLongitude(pointerWgs.getLongitude());
            this.mLocation.setAltitude(0.0D);
            this.mLocation.setAccuracy(5.0F);
            this.mLocation.setBearing(sBearing);
            this.mLocation.setSpeed(sSpeed);
            this.mLocation.setTime(System.currentTimeMillis());
            if (VERSION.SDK_INT >= 17) {
                this.mLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }

            LogMockGps.log("MockGpsManager", "⚠️teleportWgs() 中 GPS Provider 可用, " +
                    "设置坐标: lat = [" + lat + "], lng = [" + lng + "], sSpeed = ["
                    + sSpeed + "], bearing = [" + sBearing + "]");
            this.mLocationManager.setTestProviderLocation("gps", this.mLocation);
        } catch (SecurityException var8) {
            String msg = "请到开发者选项中打开模拟定位";
            this.onMessage(msg);
        } catch (Exception var9) {
            LogMockGps.log("MockGpsManager", "Provider \"gps\" unknown. 拦截下来了.");

            try {
                this.removeTestProvider();
                this.mLocationManager.addTestProvider("gps", false, false, false, false, true, true, true, 0, 5);
                this.mLocationManager.setTestProviderEnabled("gps", true);
                this.teleportWgs(this.mLastPoint.getLatitude(), this.mLastPoint.getLongitude());
            } catch (Exception var7) {
                var7.printStackTrace();
                this.disableTestProvider();
            }
        }

    }

    public void setListener(MockGpsManager.OnMessage onMessage) {
        this.mOnMessage = onMessage;
    }

    public void stopMock() {
        this.disableTestProvider();
    }

    private void removeTestProvider() {
        try {
            LogMockGps.log("MockGpsManager", "removeTestProvider();" +
                    "Removing Test providers");
            this.mLocationManager.removeTestProvider("gps");
        } catch (Exception var2) {
            LogMockGps.log("MockGpsManager", "⚠️Got exception in removing test  " +
                    "provider :" + var2.toString());
        }

    }

    private void onMessage(String msg) {
        if (this.mOnMessage != null) {
            this.mOnMessage.onMessage(msg);
        }

    }

    interface OnMessage {
        void onMessage(String var1);
    }
}

