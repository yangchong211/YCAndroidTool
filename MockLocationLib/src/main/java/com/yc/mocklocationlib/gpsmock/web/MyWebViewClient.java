package com.yc.mocklocationlib.gpsmock.web;


import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yc.mocklocationlib.gpsmock.utils.LogMockGps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyWebViewClient extends WebViewClient {

    private List<InvokeListener> mListeners = new ArrayList();

    public MyWebViewClient() {
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("doraemon://invokeNative")) {
            this.handleInvokeFromJs(url);
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private void handleInvokeFromJs(String url) {
        Iterator var2 = this.mListeners.iterator();
        while(var2.hasNext()) {
            MyWebViewClient.InvokeListener listener = (MyWebViewClient.InvokeListener)var2.next();
            listener.onNativeInvoke(url);
        }
    }

    public void addInvokeListener(MyWebViewClient.InvokeListener listener) {
        this.mListeners.add(listener);
    }

    public void removeInvokeListener(MyWebViewClient.InvokeListener listener) {
        this.mListeners.remove(listener);
    }

    public interface InvokeListener {
        void onNativeInvoke(String var1);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LogMockGps.log("MyWebViewClient" , "onReceivedError:"
                    + error.getDescription().toString());
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LogMockGps.log("MyWebViewClient" , "onReceivedHttpError:"
                    + errorResponse.getStatusCode());
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        LogMockGps.log("MyWebViewClient" , "onReceivedSslError:"
                + error.toString());
    }
}

