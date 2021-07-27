package com.yc.mocklocationlib.gpsmock;


public class GpsMockManager {
    private static final String TAG = "GpsMockManager";
    private double mLatitude;
    private double mLongitude;
    private boolean isMocking;

    public void startMock() {
        this.isMocking = true;
    }

    public void stopMock() {
        this.isMocking = false;
    }

    public static GpsMockManager getInstance() {
        return GpsMockManager.Holder.INSTANCE;
    }

    private GpsMockManager() {
        this.mLatitude = -1.0D;
        this.mLongitude = -1.0D;
    }

    public void mockLocation(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public boolean isMocking() {
        return this.isMocking && this.mLongitude != -1.0D && this.mLatitude != -1.0D;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public boolean isMockEnable() {
        return ServiceHookManager.getInstance().isHookSuccess();
    }

    private static class Holder {
        private static GpsMockManager INSTANCE = new GpsMockManager();

        private Holder() {
        }
    }
}

