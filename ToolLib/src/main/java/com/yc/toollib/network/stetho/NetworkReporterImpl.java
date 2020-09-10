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
        return true;
    }

    @Override
    public void requestWillBeSent(InspectorRequest request) {
        ToolLogUtils.d(TAG, "requestWillBeSent");
        mDataTranslator.saveInspectorRequest(request);
    }

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
