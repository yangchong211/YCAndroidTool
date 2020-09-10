package com.yc.toollib.network.ping;

/**
 * 监控网络诊断的跟踪信息
 */
public interface NetDiagnoListener {

    /**
     * 当结束之后返回日志
     *
     * @param log
     */
    void OnNetDiagnoFinished(String log);


    /**
     * 跟踪过程中更新日志
     *
     * @param log
     */
    void OnNetDiagnoUpdated(String log);
}
