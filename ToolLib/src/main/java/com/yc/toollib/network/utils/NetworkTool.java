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
import com.yc.toollib.network.ui.NetRequestActivity;
import com.yc.toollib.network.ui.NetworkDetailActivity;
import com.yc.toollib.tool.ToolLogUtils;


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
    }

    public Application getApplication() {
        return app;
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
