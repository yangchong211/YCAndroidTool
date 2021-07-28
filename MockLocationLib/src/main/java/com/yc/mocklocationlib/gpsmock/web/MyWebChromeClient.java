package com.yc.mocklocationlib.gpsmock.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.yc.mocklocationlib.gpsmock.utils.LogMockGps;

public class MyWebChromeClient extends WebChromeClient {

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        LogMockGps.log("MyWebChromeClient" , "onReceivedTitle:" + title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        LogMockGps.log("MyWebChromeClient" , "onProgressChanged:" + newProgress);
    }


}
