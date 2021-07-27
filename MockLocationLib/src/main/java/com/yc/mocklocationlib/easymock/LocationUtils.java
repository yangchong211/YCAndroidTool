package com.yc.mocklocationlib.easymock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class LocationUtils {

    private volatile static LocationUtils uniqueInstance;
    private LocationManager locationManager;
    private final Context mContext;
    private static ArrayList<AddressCallback> addressCallbacks = new ArrayList<>();
    private AddressCallback addressCallback;
    private static Location location;
    //是否加载过
    private boolean isInit = false;


    public AddressCallback getAddressCallback() {
        return addressCallback;
    }

    public void setAddressCallback(AddressCallback addressCallback) {
        this.addressCallback = addressCallback;
        if(isInit){
            showLocation();
        }else {
            isInit = true;
        }
    }

    private LocationUtils(Context context) {
        mContext = context;
        getLocation();
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtils(context);
                }
            }
        }
        return uniqueInstance;
    }

    /**
     * 添加回调事件
     * @param addressCallback
     */
    private void addAddressCallback(AddressCallback addressCallback){
        addressCallbacks.add(addressCallback);
        if(isInit){
            showLocation();
        }
    }

    /**
     * 移除回调事件
     * @param addressCallback
     */
    public void removeAddressCallback(AddressCallback addressCallback){
        if(addressCallbacks.contains(addressCallback)){
            addressCallbacks.remove(addressCallback);
        }
    }

    /**
     * 清空回调事件
     */
    public void clearAddressCallback(){
        removeLocationUpdatesListener();
        addressCallbacks.clear();
    }

    private void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //2.获取位置提供器，GPS或是NetWork
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        String locationProvider;
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            //GPS 定位的精准度比较高，但是非常耗电。
            System.out.println("=====GPS_PROVIDER=====");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {//Google服务被墙不可用
            //网络定位的精准度稍差，但耗电量比较少。
            System.out.println("=====NETWORK_PROVIDER=====");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            System.out.println("=====NO_PROVIDER=====");
            // 当没有可用的位置提供器时，弹出Toast提示用户
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
            return;
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            // 显示当前设备的位置信息
            System.out.println("==显示当前设备的位置信息==");
            showLocation();
        } else {
            //当GPS信号弱没获取到位置的时候可从网络获取
            System.out.println("==Google服务被墙的解决办法==");
            //Google服务被墙的解决办法
            getLngAndLatWithNetwork();
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        //LocationManager 每隔 5 秒钟会检测一下位置的变化情况，当移动距离超过 10 米的时候，
        // 就会调用 LocationListener 的 onLocationChanged() 方法，并把新的位置信息作为参数传入。
        locationManager.requestLocationUpdates(locationProvider, 5000, 10, locationListener);
    }

    //获取经纬度
    private void showLocation() {
        if(location == null){
            getLocation();
        }else {
            double latitude = location.getLatitude();//纬度
            double longitude = location.getLongitude();//经度
//            for(AddressCallback addressCallback:addressCallbacks){
//                addressCallback.onGetLocation(latitude,longitude);
//            }
            if(addressCallback != null){
                addressCallback.onGetLocation(latitude,longitude);
            }
            getAddress(latitude, longitude);
        }
    }

    private void getAddress(double latitude, double longitude) {
        //Geocoder通过经纬度获取具体信息
        Geocoder gc = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> locationList = gc.getFromLocation(latitude, longitude, 1);
            if (locationList != null) {
                Address address = locationList.get(0);
                String countryName = address.getCountryName();//国家
                String countryCode = address.getCountryCode();
                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//市
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道

                for (int i = 0; address.getAddressLine(i) != null; i++) {
                    String addressLine = address.getAddressLine(i);
                    //街道名称:广东省深圳市罗湖区蔡屋围一街深圳瑞吉酒店
                    System.out.println("addressLine=====" + addressLine);
                }
                if(addressCallback != null){
                    addressCallback.onGetAddress(address);
                }
//                for(AddressCallback addressCallback:addressCallbacks){
//                    addressCallback.onGetAddress(address);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLocationUpdatesListener() {
        if (locationManager != null) {
            uniqueInstance = null;
            locationManager.removeUpdates(locationListener);
        }
    }


    private LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location loc) {
            System.out.println("==onLocationChanged==");
//            location = loc;
//            showLocation();
        }
    };

    //从网络获取经纬度
    private void getLngAndLatWithNetwork() {
        //添加用户权限申请判断
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        showLocation();
    }

    public interface AddressCallback{
        void onGetAddress(Address address);
        void onGetLocation(double lat,double lng);
    }
}
