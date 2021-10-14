package com.yc.ycandroidtool.memory;

import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.memorylib.AppMemoryUtils;
import com.yc.netlib.utils.NetDeviceUtils;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toollib.crash.MemoryUtils;
import com.yc.toollib.tool.ToolLogUtils;
import com.yc.ycandroidtool.R;

public class MemoryActivity extends AppCompatActivity implements View.OnClickListener {

    CallBack2 callBack;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);


        tvInfo = findViewById(R.id.tv_info);

        callBack = new CallBack2();
        getApplication().registerComponentCallbacks(callBack);

        getMemory();
    }

    private void getMemory() {
        StringBuffer sb = new StringBuffer();
        // 获取Java的最大内存
        long max = Runtime.getRuntime().maxMemory();
        sb.append("\n最大内存：").append(max);
        // 获取Java已经使用分配的内存
        long totalMemory = Runtime.getRuntime().totalMemory();
        sb.append("\n使用内存：").append(totalMemory);
        // 获取Java 空闲的内存
        long freeMemory = Runtime.getRuntime().freeMemory();
        sb.append("\n空闲的内存：").append(freeMemory);
        sb.append("\n手机总内存:").append(AppMemoryUtils.getTotalMemory(this));
        sb.append("\n手机可用内存:").append(AppMemoryUtils.getAvailMemory(this));
        final int pid = MemoryUtils.getCurrentPid();
        AppMemoryUtils.PssInfo pssInfo = AppMemoryUtils.getAppPssInfo(this, pid);
        sb.append("\ndalvik堆大小:").append(AppMemoryUtils.getFormatSize(pssInfo.dalvikPss));
        sb.append("\n手机堆大小:").append(AppMemoryUtils.getFormatSize(pssInfo.nativePss));
        sb.append("\nPSS内存使用量:").append(AppMemoryUtils.getFormatSize(pssInfo.totalPss));
        sb.append("\n其他比例大小:").append(AppMemoryUtils.getFormatSize(pssInfo.otherPss));

        final AppMemoryUtils.DalvikHeapMem dalvikHeapMem = AppMemoryUtils.getAppDalvikHeapMem();
        sb.append("\n已用内存:").append(AppMemoryUtils.getFormatSize(dalvikHeapMem.allocated));
        sb.append("\n最大内存:").append(AppMemoryUtils.getFormatSize(dalvikHeapMem.maxMem));
        sb.append("\n空闲内存:").append(AppMemoryUtils.getFormatSize(dalvikHeapMem.freeMem));

        long appTotalDalvikHeapSize = AppMemoryUtils.getAppTotalDalvikHeapSize(this);
        sb.append("\n应用占用内存:").append(MemoryUtils.getFormatSize(appTotalDalvikHeapSize));
        tvInfo.setText(sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().unregisterComponentCallbacks(callBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                break;
            case R.id.tv_2:
                break;
            case R.id.tv_3:
                break;
        }
    }

    private static class CallBack1 implements ComponentCallbacks{

        // 通过在设备配置您的组件运行时改变了系统调用。
        // 需要注意的是，不同的活动，其他组件从未当配置更改重新启动：他们必须应对变化的结果，如再获取资源。
        // 在这个函数被调用的时候，你的资源对象将被更新，以返回匹配新的配置资源的值
        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }


        // 这是当整个系统运行在内存不足，并积极运行的进程应修剪其内存使用调用。
        // 虽然没有定义在此将被称为确切点，通常当所有的后台进程已被杀害它会发生。
        // 也就是说，达到了托管服务和前台UI杀死进程的点之前，我们想避免杀害。
        // 您应该实现此方法来释放你可能持有的任何缓存或其他不必要的资源。
        // 该系统将从此方法返回后执行垃圾收集你。
        // 最好，你应该实现ComponentCallbacks2.onTrimMemory
        // 从ComponentCallbacks2根据不同级别的内存需求逐步卸下你的资源。
        // 该API可用于API级别14和更高的，
        // 所以你应该只使用这个onLowMemory方法，旧版本的回退，
        // 可以治疗一样ComponentCallbacks2.onTrimMemory与ComponentCallbacks2.TRIM_MEMORY_COMPLETE水平。
        @Override
        public void onLowMemory() {

        }
    }

    private static class CallBack2 implements ComponentCallbacks2 {

        /**
         * 您应该实现onTrimMemory（int）以根据当前系统约束逐步释放内存。
         * 使用此回调来释放资源有助于提供整体响应更快的系统，同时通过允许系统使您的进程保持更长时间，直接有益于您的应用程序的用户体验。
         * @param level
         */
        @Override
        public void onTrimMemory(int level) {
            ToolLogUtils.i("CallBack---onTrimMemory--"+level);
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            ToolLogUtils.i("CallBack---onConfigurationChanged--");
        }

        @Override
        public void onLowMemory() {
            ToolLogUtils.i("CallBack---onLowMemory--");
        }
    }

}
