package com.yc.appstatuslib.info;

import com.yc.appstatuslib.cpu.Cpu;

public class CpuInfo {
    public int cpuCount;
    public String cpuTemperature = "";

    public CpuInfo() {
    }

    static CpuInfo builder() {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.cpuCount = Cpu.cpuCount();
        cpuInfo.cpuTemperature = Cpu.cpuTemperature();
        return cpuInfo;
    }

    public String toString() {
        return "CpuInfo{cpuCount=" + this.cpuCount + ", cpuTemperature='" + this.cpuTemperature + '\'' + '}';
    }
}
