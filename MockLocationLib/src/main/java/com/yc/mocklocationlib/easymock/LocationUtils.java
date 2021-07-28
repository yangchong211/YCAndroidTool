package com.yc.mocklocationlib.easymock;

public final class LocationUtils {

    public static boolean DEBUG = true;

    public static void log(String msg) {
        d("LocationUtils", msg);
    }

    /**
     * 打印debug级别信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if ($(msg)) {
            return;
        }
        if (tag == null){
            tag = "LocationUtils";
        }
        if (DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    private static boolean $(String msg) {
        return msg == null;
    }

}
