/*
 * Copyright (c) 2014-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.yc.ycandroidtool.test.example;

import android.os.SystemClock;


import androidx.annotation.Nullable;

import com.yc.toollib.tool.ToolLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Implementation of {@link NetworkEventReporter} which allows callers to inform the Stetho
 * system of network traffic.  Callers can safely eagerly access this class and store a
 * reference if they wish.  When WebKit Inspector clients are connected, the internal
 * implementation will be automatically wired up to them.
 */
public class NetworkEventReporterImpl implements NetworkEventReporter {
  private static final String TAG = "NetworkEventReporterImpl";
  private final AtomicInteger mNextRequestId = new AtomicInteger(0);

  private static NetworkEventReporter sInstance;

  private NetworkEventReporterImpl() {
  }

  /**
   * Static accessor allowing callers to easily hook into the WebKit Inspector system without
   * creating dependencies on the main Stetho initialization code path.
   */
  public static synchronized NetworkEventReporter get() {
    if (sInstance == null) {
      sInstance = new NetworkEventReporterImpl();
    }
    return sInstance;
  }


  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void requestWillBeSent(NetworkEventReporter.InspectorRequest request) {
    ToolLogUtils.d(TAG, "requestWillBeSent");
  }

  @Override
  public void responseHeadersReceived(NetworkEventReporter.InspectorResponse response) {
    ToolLogUtils.d(TAG, "responseHeadersReceived");
  }

  @Override
  public void httpExchangeFailed(String requestId, String errorText) {
    ToolLogUtils.d(TAG, "httpExchangeFailed");
  }

  @Nullable
  @Override
  public InputStream interpretResponseStream(String requestId, @Nullable String contentType, @Nullable String contentEncoding, @Nullable InputStream inputStream, ResponseHandler responseHandler) {
    return null;
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
  public void webSocketWillSendHandshakeRequest(NetworkEventReporter.InspectorWebSocketRequest request) {
    ToolLogUtils.d(TAG, "webSocketWillSendHandshakeRequest");
  }

  @Override
  public void webSocketHandshakeResponseReceived(NetworkEventReporter.InspectorWebSocketResponse response) {
    ToolLogUtils.d(TAG, "webSocketHandshakeResponseReceived");
  }

  @Override
  public void webSocketFrameSent(NetworkEventReporter.InspectorWebSocketFrame frame) {
    ToolLogUtils.d(TAG, "webSocketFrameSent");
  }

  @Override
  public void webSocketFrameReceived(NetworkEventReporter.InspectorWebSocketFrame frame) {
    ToolLogUtils.d(TAG, "webSocketFrameReceived");
  }

  @Override
  public void webSocketFrameError(String requestId, String errorMessage) {
    ToolLogUtils.d(TAG, "webSocketFrameError");
  }
}
