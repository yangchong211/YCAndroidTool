package com.yc.appstatuslib;


import android.util.Log;
import java.io.FileWriter;

public class LogUtils {
    private static final String TAG = "_AndyTest_";
    private static final boolean sOpenLog = true;
    private static final boolean sOpenLogToFile = false;
    private static FileWriter mFileWriter = null;

    public LogUtils() {
    }

    public static void d(String logKey, String msg) {
        d(logKey, msg, 2);
    }

    public static void d(String logKey, String msg, Object... args) {
        d(logKey, msg + args, 2);
    }

    public static void d(String logKey, String msg, int stackIndex) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[stackIndex];
        String log = build(msg, ste);
        Log.d("_AndyTest_", "[" + logKey + "]" + log);
    }

    public static void d(String msg) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        String log = build(msg, ste);
        Log.d("_AndyTest_", log);
    }

    public static void i(String logKey, String msg, int stackIndex) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[stackIndex];
        String log = build(msg, ste);
        Log.i("_AndyTest_", "[" + logKey + "]" + log);
    }

    public static void i(String logKey, String msg) {
        i(logKey, msg, 2);
    }

    public static void v(String logKey, String msg, int stackIndex) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[stackIndex];
        String log = build(msg, ste);
        Log.v("_AndyTest_", "[" + logKey + "]" + log);
    }

    public static void v(String logKey, String msg) {
        v(logKey, msg, 2);
    }

    public static void w(String logKey, String msg) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        String log = build(logKey, msg, ste);
        Log.w("_AndyTest_", "[" + logKey + "]" + log);
    }

    public static void e(String tag, Throwable tr) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        String log = build(tag, "", ste, tr);
        Log.e(tag, log, tr);
    }

    public static void e(String logKey, String msg) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        String log = build(logKey, msg, ste);
        Log.e(logKey, log);
    }

    public static void e(String logKey, String msg, Throwable e) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[1];
        String log = build(logKey, msg, ste);
        Log.e("_AndyTest_", log, e);
    }

    public static void t(String tag, String str) {
        i(tag, "DebugInfo: " + str, 2);
        Throwable e = new Throwable(tag);
        e.printStackTrace();
    }

    private static void writeToFile(String strLog) {
    }

    private static String build(String log, StackTraceElement ste) {
        StringBuilder buf = new StringBuilder();
        buf.append("[").append(Thread.currentThread().getId()).append("]");
        if (ste.isNativeMethod()) {
            buf.append("(Native Method)");
        } else {
            String fName = ste.getFileName();
            if (fName == null) {
                buf.append("(Unknown Source)");
            } else {
                int lineNum = ste.getLineNumber();
                buf.append('(');
                buf.append(fName);
                if (lineNum >= 0) {
                    buf.append(':');
                    buf.append(lineNum);
                }

                buf.append("):");
            }
        }

        buf.append(log);
        return buf.toString();
    }

    private static String build(String logKey, String msg, StackTraceElement ste) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(logKey).append("]").append(build(msg, ste));
        return sb.toString();
    }

    private static String build(String logKey, String msg, StackTraceElement ste, Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(logKey).append("]").append(ste.toString()).append(":").append(msg).append("\r\n").append("e:").append(e.getMessage());
        return sb.toString();
    }
}

