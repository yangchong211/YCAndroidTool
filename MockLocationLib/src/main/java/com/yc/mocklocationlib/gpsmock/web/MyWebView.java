package com.yc.mocklocationlib.gpsmock.web;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ProgressBar;

import com.yc.mocklocationlib.R;

import static android.webkit.WebSettings.LOAD_DEFAULT;


public class MyWebView extends WebView {

    private ProgressBar mProgressBar;
    private MyWebViewClient mMyWebViewClient;
    private Activity mContainerActivity;

    public MyWebView(Context context) {
        super(context);
        this.init(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context) {
        if (!(context instanceof Activity)) {
            throw new RuntimeException("only support Activity context");
        } else {
            this.mContainerActivity = (Activity)context;
            WebSettings webSettings = this.getSettings();
            webSettings.setPluginState(PluginState.ON);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(false);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setDefaultTextEncodingName("UTF-8");
            webSettings.setDomStorageEnabled(true);
            webSettings.setCacheMode(LOAD_DEFAULT);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            if (VERSION.SDK_INT < 18) {
                webSettings.setRenderPriority(RenderPriority.HIGH);
            }
            if (VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(0);
            }
            this.mMyWebViewClient = new MyWebViewClient();
            this.setWebViewClient(this.mMyWebViewClient);
            this.setWebChromeClient(new MyWebChromeClient() {
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress < 100) {
                        MyWebView.this.showLoadProgress(newProgress);
                    } else {
                        MyWebView.this.hideLoadProgress();
                    }
                }
            });
            this.addProgressView();
        }
    }

    public void addInvokeListener(MyWebViewClient.InvokeListener listener) {
        this.mMyWebViewClient.addInvokeListener(listener);
    }

    public void removeInvokeListener(MyWebViewClient.InvokeListener listener) {
        this.mMyWebViewClient.removeInvokeListener(listener);
    }

    private void addProgressView() {
        this.mProgressBar = new ProgressBar(this.getContext(), (AttributeSet)null, 16842872);
        this.mProgressBar.setLayoutParams(new LayoutParams(-1, 10, 0, 0));
        Integer progressBarColor = this.getResources().getColor(R.color.color_55A8FD);
        ClipDrawable d = new ClipDrawable(new ColorDrawable(progressBarColor), 3, 1);
        this.mProgressBar.setProgressDrawable(d);
        this.mProgressBar.setVisibility(GONE);
        this.addView(this.mProgressBar);
    }

    public void showLoadProgress(int progress) {
        if (null != this.mProgressBar) {
            if (this.mProgressBar.getVisibility() == View.GONE) {
                this.mProgressBar.setVisibility(VISIBLE);
            }

            this.mProgressBar.setProgress(progress);
        }

    }

    public void hideLoadProgress() {
        if (null != this.mProgressBar) {
            this.mProgressBar.setVisibility(GONE);
        }
    }

    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http://")
                && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        super.loadUrl(url);
    }

    public Activity getActivity() {
        return this.mContainerActivity;
    }
}

