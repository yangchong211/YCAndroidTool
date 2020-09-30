package com.yc.toollib.network.utils;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yc.toollib.R;
import com.yc.toollib.network.floating.FloatWindow;
import com.yc.toollib.network.floating.PermissionListener;
import com.yc.toollib.network.floating.Screen;
import com.yc.toollib.network.floating.ViewStateListener;
import com.yc.toollib.network.stetho.NetworkInterceptor;
import com.yc.toollib.network.stetho.NetworkListener;
import com.yc.toollib.network.ui.NetRequestActivity;
import com.yc.toollib.network.ui.NetworkDetailActivity;
import com.yc.toollib.network.ui.NetworkManager;
import com.yc.toollib.tool.ToolLogUtils;

import java.net.Proxy;

import okhttp3.OkHttpClient;


public class NetworkTool {

    private static NetworkTool INSTANCE;
    private Application app;

    /**
     * 获取NetworkTool实例 ,单例模式
     */
    public static NetworkTool getInstance() {
        if (INSTANCE == null) {
            synchronized (NetworkTool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetworkTool();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Application application){
        this.app = application;
        setOkHttpHook();
        NetworkManager.get().startMonitor();
    }

    public void stopMonitor(){
        NetworkManager.get().stopMonitor();
    }

    public Application getApplication() {
        return app;
    }

    /**
     *     每个拦截器都有自己的相对优点。
     *
     *     应用拦截器
     *     不需要担心中间响应，如重定向和重试。
     *     总是调用一次，即使从缓存提供HTTP响应。
     *     遵守应用程序的原始意图。
     *     不注意OkHttp注入的头像If-None-Match。
     *     允许短路和不通话Chain.proceed()。
     *     允许重试并进行多次呼叫Chain.proceed()。
     *
     *     网络拦截器
     *     能够对重定向和重试等中间响应进行操作。
     *     不调用缓存的响应来短路网络。
     *     观察数据，就像通过网络传输一样。
     *     访问Connection该请求。
     */
    private OkHttpClient.Builder localBuild;
    public OkHttpClient.Builder addOkHttp(OkHttpClient.Builder builder,boolean isProxy){
        ToolLogUtils.i("OkHttpHook"+"-------addOkHttp");
        if (localBuild==null){
            localBuild = builder
                    .eventListenerFactory(NetworkListener.get())
                    //.addNetworkInterceptor(new WeakNetworkInterceptor())
                    .addNetworkInterceptor(new NetworkInterceptor());
            if (isProxy){
                //基于抓包原理的基础上，直接使用okHttp禁止代理，经过测试，可以避免第三方工具(比如charles)抓包
                localBuild.proxy(Proxy.NO_PROXY);
            }
        }
        return localBuild;
    }

    public void setOkHttpHook(){
        OkHttpHooker.installEventListenerFactory(NetworkListener.get());
        //OkHttpHooker.installDns(new CustomGlobalDns());
        OkHttpHooker.installInterceptor(new NetworkInterceptor());
    }

    /**
     * 设置全局悬浮按钮，点击可以去网络列表页面
     * @param context                           上下文
     */
    public void setFloat(final Context context){
        ImageView imageView = new ImageView(context.getApplicationContext());
        imageView.setImageResource(R.drawable.ic_show_error);
        FloatWindow
                .with(context)
                .setView(imageView)
                //设置悬浮控件宽高
                .setWidth(Screen.width, 0.2f)
                .setHeight(Screen.width, 0.2f)
                //设置控件初始位置
                .setX(Screen.width, 0.8f)
                .setY(Screen.height, 0.3f)
                //桌面显示
                .setDesktopShow(false)
                //设置这两个页面隐藏
                .setFilter(false, NetRequestActivity.class , NetworkDetailActivity.class)
                .setViewStateListener(new ViewStateListener() {
                    @Override
                    public void onPositionUpdate(int x, int y) {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onPositionUpdate");
                    }

                    @Override
                    public void onShow() {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onShow");
                    }

                    @Override
                    public void onHide() {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onHide");
                    }

                    @Override
                    public void onDismiss() {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onDismiss");
                    }

                    @Override
                    public void onMoveAnimStart() {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onMoveAnimStart");
                    }

                    @Override
                    public void onMoveAnimEnd() {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onMoveAnimEnd");
                    }

                    @Override
                    public void onBackToDesktop() {
                        ToolLogUtils.i("NetworkTool-------ViewStateListener------onBackToDesktop");
                    }
                })    //监听悬浮控件状态改变
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        ToolLogUtils.i("NetworkTool-------PermissionListener------onSuccess");
                    }

                    @Override
                    public void onFail() {
                        ToolLogUtils.i("NetworkTool-------PermissionListener------onFail");
                    }
                })  //监听权限申请结果
                .build();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequestActivity.start(context.getApplicationContext());
            }
        });
    }
}
