package com.yc.toollib.network.stetho;




import android.support.annotation.Nullable;

import com.yc.toollib.tool.ToolLogUtils;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;


public class NetworkReporterImpl implements NetworkEventReporter {

    private static final String TAG = "NetworkReporterImpl";
    private AtomicInteger mNextRequestId = new AtomicInteger(0);
    private DataTranslator mDataTranslator;
    private static NetworkReporterImpl sInstance;

    public static NetworkReporterImpl getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkReporterImpl();
        }
        return sInstance;
    }

    private NetworkReporterImpl() {
        mDataTranslator = new DataTranslator();
    }

    @Override
    public boolean isEnabled() {
        //这里默认为true，表示需要创建发送请求
        return true;
    }

    /**
     * 指示将要发送请求，但尚未通过网络传递。
     * 表示将通过HTTP发送的请求。注意，对于许多HTTP实现，构造的请求可能与实际通过网络发送的请求不同。
     * 例如，额外的头，如{@code Host}， {@code User-Agent}， {@code Content-Type}等，可能不是这个请求的一部分，但如果需要，应该注入。
     * 一些堆栈提供对即将发送到服务器的原始请求的检查，这是更好的。
     * @param request Request descriptor.
     */
    @Override
    public void requestWillBeSent(InspectorRequest request) {
        ToolLogUtils.d(TAG, "requestWillBeSent");
        //这里改为自己实现，可以把信息保存到map集合中
        mDataTranslator.saveInspectorRequest(request);
    }

    /**
     * 指示刚从网络接收到响应消息，但尚未读取消息体。
     * @param response Response descriptor.
     */
    @Override
    public void responseHeadersReceived(InspectorResponse response) {
        ToolLogUtils.d(TAG, "responseHeadersReceived");
        mDataTranslator.saveInspectorResponse(response);
    }

    @Override
    public void httpExchangeFailed(String requestId, String errorText) {
        ToolLogUtils.d(TAG, "httpExchangeFailed");
    }

    @Nullable
    @Override
    public InputStream interpretResponseStream(String requestId, @Nullable String contentType,
                                               @Nullable String contentEncoding,
                                               @Nullable InputStream inputStream, ResponseHandler responseHandler) {
        ToolLogUtils.d(TAG, "interpretResponseStream");
        return mDataTranslator.saveInterpretResponseStream(requestId, contentType, contentEncoding, inputStream);
    }

    @Override
    public void responseReadFailed(String requestId, String errorText) {
        ToolLogUtils.d(TAG, "responseReadFailed");
    }

    @Override
    public void responseReadFinished(String requestId) {
        ToolLogUtils.d(TAG, "responseReadFinished");
    }

    @Override
    public void dataSent(String requestId, int dataLength, int encodedDataLength) {
        ToolLogUtils.d(TAG, "dataSent");
    }

    @Override
    public void dataReceived(String requestId, int dataLength, int encodedDataLength) {
        ToolLogUtils.d(TAG, "dataReceived");
    }

    @Override
    public String nextRequestId() {
        ToolLogUtils.d(TAG, "nextRequestId");
        return String.valueOf(mNextRequestId.getAndIncrement());
    }

    @Override
    public void webSocketCreated(String requestId, String url) {
        ToolLogUtils.d(TAG, "webSocketCreated");
    }

    @Override
    public void webSocketClosed(String requestId) {
        ToolLogUtils.d(TAG, "webSocketClosed");
    }

    @Override
    public void webSocketWillSendHandshakeRequest(InspectorWebSocketRequest request) {
        ToolLogUtils.d(TAG, "webSocketWillSendHandshakeRequest");
    }

    @Override
    public void webSocketHandshakeResponseReceived(InspectorWebSocketResponse response) {
        ToolLogUtils.d(TAG, "webSocketHandshakeResponseReceived");
    }

    @Override
    public void webSocketFrameSent(InspectorWebSocketFrame frame) {
        ToolLogUtils.d(TAG, "webSocketFrameSent");
    }

    @Override
    public void webSocketFrameReceived(InspectorWebSocketFrame frame) {
        ToolLogUtils.d(TAG, "webSocketFrameReceived");
    }

    @Override
    public void webSocketFrameError(String requestId, String errorMessage) {
        ToolLogUtils.d(TAG, "webSocketFrameError");
    }
}
