package com.yc.mocklocationlib.gpsmock.point;


import com.yc.mocklocationlib.gpsmock.point.GcjPointer;
import com.yc.mocklocationlib.gpsmock.point.GeoPointer;

public class WgsPointer extends GeoPointer {
    public WgsPointer(double latitude, double longitude) {
        super(latitude, longitude);
    }

    public GcjPointer toGcjPointer() {
        if (GeoPointer.outOfChina(this.mLatitude, this.mLongitude)) {
            return new GcjPointer(this.mLatitude, this.mLongitude);
        } else {
            double[] delta = GeoPointer.delta(this.mLatitude, this.mLongitude);
            return new GcjPointer(this.mLatitude + delta[0], this.mLongitude + delta[1]);
        }
    }
}
