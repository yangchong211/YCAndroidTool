package com.yc.appstatuslib.info;

import com.yc.appstatuslib.utils.CpuUtils;

public class CpuInfo {
    public int cpuCount;
    public String cpuTemperature = "";

    public CpuInfo() {
    }

    static CpuInfo builder() {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.cpuCount = CpuUtils.cpuCount();
        cpuInfo.cpuTemperature = CpuUtils.cpuTemperature();
        return cpuInfo;
    }

    public String toString() {
        return "CpuInfo{cpuCount=" + this.cpuCount + ", cpuTemperature='" + this.cpuTemperature + '\'' + '}';
    }
}
