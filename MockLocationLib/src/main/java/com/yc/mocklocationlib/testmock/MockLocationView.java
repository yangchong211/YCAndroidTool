package com.yc.mocklocationlib.testmock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.mocklocationlib.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 模拟位置信息提示控件
 */
public class MockLocationView extends LinearLayout {

    MockLocationManager mockLocationManager;
    private final Context context;
    private TextView tvProvider = null;
    private TextView tvTime = null;
    private EditText tvLatitude = null;
    private EditText tvLongitude = null;
    private TextView tvSystemMockPositionStatus = null;
    private Button btnStartMock = null;
    private Button btnStopMock = null;
    private ImageView ivLocationView;
    private LinearLayout llLocationDataLl;

    public MockLocationView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public MockLocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public MockLocationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    private void init(final Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.location_wiget_layout, this, true);
        initFindViewById(layout);
        //创建模拟地址管理类
        mockLocationManager = new MockLocationManager();
        btnStartMock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startMock();
            }
        });
        btnStopMock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMock();
            }
        });
        mockLocationManager.initService(context);
        //mockLocationManager.startMockLocation();
    }

    private void initFindViewById(View layout) {
        tvProvider = layout.findViewById(R.id.tv_provider);
        tvTime = layout.findViewById(R.id.tv_time);
        tvLatitude = layout.findViewById(R.id.tv_latitude);
        tvLongitude = layout.findViewById(R.id.tv_longitude);
        tvSystemMockPositionStatus = findViewById(R.id.tv_system_mock_position_status);
        ivLocationView = findViewById(R.id.location_wigdet_tip_iv);
        llLocationDataLl = findViewById(R.id.location_wigdet_data_ll);
        btnStartMock = findViewById(R.id.btn_start_mock);
        btnStopMock = findViewById(R.id.btn_stop_mock);
        tvLatitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tvLongitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    /**
     * 开始模拟定位
     */
    public void stopMock() {
        mockLocationManager.stopMockLocation();
        btnStartMock.setEnabled(true);
        btnStopMock.setEnabled(false);
    }

    /**
     * 停止模拟定位
     */
    public void startMock() {
        if (mockLocationManager.getUseMockPosition(context)) {
            String lat = tvLatitude.getText().toString().trim();
            String log = tvLongitude.getText().toString().trim();
            setMangerLocationData(parseDouble(lat),parseDouble(log));
            mockLocationManager.startMockLocation();
            btnStartMock.setEnabled(false);
            btnStopMock.setEnabled(true);
        }
    }

    @SuppressLint("MissingPermission")
    public void refreshData() {
        boolean granted = PermissionUtils.isGranted(getContext()
                ,Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (!granted){
            PermissionUtils permission = PermissionUtils.permission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            permission.request(getContext());
            return;
        }
        // 判断系统是否允许模拟位置，并addTestProvider
        if (mockLocationManager.getUseMockPosition(context)) {
            tvSystemMockPositionStatus.setText("已开启");
            ivLocationView.setVisibility(View.GONE);
            llLocationDataLl.setVisibility(View.VISIBLE);
        } else {
            tvSystemMockPositionStatus.setText("未开启");
            ivLocationView.setVisibility(View.VISIBLE);
            llLocationDataLl.setVisibility(View.GONE);
        }
        if (getLocationManager()!=null){
            getLocationManager().requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void removeUpdates() {
        if (getLocationManager()!=null){
            getLocationManager().removeUpdates(locationListener);
        }
    }

    public LocationManager getLocationManager(){
        if (mockLocationManager!=null){
            return mockLocationManager.getLocationManager();
        }
        return null;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //定位变化
            //setLocationData(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * 获取到模拟定位信息，并显示
     * @param location                          定位信息
     */
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void setLocationData(Location location) {
        tvProvider.setText(location.getProvider());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvTime.setText(simpleDateFormat.format(new Date(location.getTime())));
        tvLatitude.setText(location.getLatitude() + " °");
        tvLongitude.setText(location.getLongitude() + " °");
    }

    public void setMangerLocationData(double lat, double lon) {
        mockLocationManager.setLocationData(lat, lon);
    }

    public static double parseDouble(String string) {
        if (string == null || string.length() == 0) {
            return -1;
        }
        if (isInteger(string)) {
            return Integer.parseInt(string);
        } else if (isFloat(string)) {
            return Float.parseFloat(string);
        } else if (isDouble(string)) {
            return Double.parseDouble(string);
        }
        return -1;
    }

    private static boolean isInteger(String string) {
        boolean isInteger;
        try {
            Integer.parseInt(string);
            isInteger = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isInteger = false;
        }
        return isInteger;
    }

    private static boolean isFloat(String string) {
        boolean isFloat;
        try {
            Float.parseFloat(string);
            isFloat = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isFloat = false;
        }
        return isFloat;
    }

    private static boolean isDouble(String string) {
        boolean isDouble;
        try {
            Double.parseDouble(string);
            isDouble = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            isDouble = false;
        }
        return isDouble;
    }


}