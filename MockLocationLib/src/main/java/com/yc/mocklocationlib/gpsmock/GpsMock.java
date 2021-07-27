package com.yc.mocklocationlib.gpsmock;


import android.content.Context;

import com.yc.mocklocationlib.gpsmock.bean.LatLng;
import com.yc.mocklocationlib.gpsmock.utils.GpsMockConfig;


public class GpsMock {

    public static void onAppInit(Context context) {
        if (GpsMockConfig.isGPSMockOpen(context)) {
            GpsMockManager.getInstance().startMock();
            LatLng latLng = GpsMockConfig.getMockLocation(context);
            if (latLng == null) {
                return;
            }
            GpsMockManager.getInstance().mockLocation(latLng.latitude, latLng.longitude);
            MockGpsManager.getInstance(context).start();
        }
    }
}

