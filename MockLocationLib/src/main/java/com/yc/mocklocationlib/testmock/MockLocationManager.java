package com.yc.mocklocationlib.testmock;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模拟地址管理类
 */
public class MockLocationManager {

    /**
     * 位置管理器
     */
    private LocationManager locationManager = null;

    /**
     * 是否成功addTestProvider，默认为true，软件启动时为防止意外退出导致未重置，重置一次
     * Android 6.0系统以下，可以通过Setting.Secure.ALLOW_MOCK_LOCATION获取是否【允许模拟位置】，
     * 当【允许模拟位置】开启时，可addTestProvider；
     * Android 6.0系统及以上，弃用Setting.Secure.ALLOW_MOCK_LOCATION变量，没有【允许模拟位置】选项，
     * 增加【选择模拟位置信息应用】，此时需要选择当前应用，才可以addTestProvider，
     * 但未找到获取当前选择应用的方法，因此通过addTestProvider是否成功来判断是否可用模拟位置。
     */
    private boolean hasAddTestProvider = true;

    /**
     * 模拟位置的提供者
     */
    private List<String> mockProviders = null;

    /**
     * 纬度
     */
    public double latitude;

    /**
     * 经度
     */
    public double longitude;
    private boolean isSuccess;

    public LocationManager getLocationManager() {
        //定位对象
        return locationManager;
    }

    public List<String> getMockProviders() {
        return mockProviders;
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100){
                //如果失败，则重试，直到成功为止
                if (!isSuccess){
                    mockLocation();
                    handler.sendEmptyMessageAtTime(100,1000);
                }
            }
        }
    };

    /**
     * 初始化服务
     * @param context                           上下文
     */
    public void initService(Context context) {
        mockProviders = new ArrayList<>();
        mockProviders.add(LocationManager.GPS_PROVIDER);
        mockProviders.add(LocationManager.NETWORK_PROVIDER);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 防止程序意外终止，没有停止模拟GPS
        stopMockLocation();
    }

    /**
     * 模拟位置是否启用
     * 若启用，则addTestProvider
     */
    public boolean getUseMockPosition(Context context) {
        // Android 6.0以下，通过Setting.Secure.ALLOW_MOCK_LOCATION判断
        // Android 6.0及以上，需要【选择模拟位置信息应用】，未找到方法，因此通过addTestProvider是否可用判断
        int secure = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION, 0);
        //判断是否开启了系统模拟位置
        boolean canMockPosition = ( secure != 0) ||
                Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
        if (canMockPosition && !hasAddTestProvider) {
            try {
                forMockProviders();
                // 模拟位置可用
                hasAddTestProvider = true;
                canMockPosition = true;
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
        if (!canMockPosition) {
            stopMockLocation();
        }
        return canMockPosition;
    }

    private void forMockProviders() {
        for (String providerStr : mockProviders) {
            LocationProvider provider = locationManager.getProvider(providerStr);
            if (provider != null) {
                locationManager.addTestProvider(
                        provider.getName()
                        , provider.requiresNetwork()
                        , provider.requiresSatellite()
                        , provider.requiresCell()
                        , provider.hasMonetaryCost()
                        , provider.supportsAltitude()
                        , provider.supportsSpeed()
                        , provider.supportsBearing()
                        , provider.getPowerRequirement()
                        , provider.getAccuracy());
            } else {
                if (providerStr.equals(LocationManager.GPS_PROVIDER)) {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true,
                            false, false,
                            true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                } else if (providerStr.equals(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.addTestProvider(
                            providerStr
                            , true, false,
                            true, false,
                            false, false, false
                            , Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , false, false,
                            false, false,
                            true, true, true
                            , Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
                }
            }
            locationManager.setTestProviderEnabled(providerStr, true);
            locationManager.setTestProviderStatus(providerStr,
                    LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        }
    }

    /**
     * 取消位置模拟，以免启用模拟数据后无法还原使用系统位置
     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
     */
    public void stopMockLocation() {
        handler.removeCallbacksAndMessages(null);
        if (hasAddTestProvider) {
            for (String provider : mockProviders) {
                try {
                    locationManager.removeTestProvider(provider);
                } catch (Exception ex) {
                    // 此处不需要输出日志，若未成功addTestProvider，则必然会出错
                    // 这里是对于非正常情况的预防措施
                }
            }
            hasAddTestProvider = false;
        }
    }

    private void mockLocation() {
        try {
            // 模拟位置（addTestProvider成功的前提下）
            for (String providerStr : mockProviders) {
                Location mockLocation = new Location(providerStr);
                mockLocation.setLatitude(latitude);   // 维度（度）
                mockLocation.setLongitude(longitude);  // 经度（度）
                mockLocation.setAccuracy(0.1f);   // 精度（米）
                mockLocation.setTime(new Date().getTime());   // 本地时间
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                }
                locationManager.setTestProviderLocation(providerStr, mockLocation);
            }
            isSuccess = true;
        } catch (Exception e) {
            // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
            stopMockLocation();
            isSuccess = false;
        }
    }


    public void startMockLocation() {
        handler.sendEmptyMessageAtTime(100,1000);
    }

    public void setLocationData(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }
}