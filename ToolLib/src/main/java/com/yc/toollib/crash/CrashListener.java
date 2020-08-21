package com.yc.toollib.crash;

public interface CrashListener {

    void againStartApp();

    void recordException(Throwable ex);

}
