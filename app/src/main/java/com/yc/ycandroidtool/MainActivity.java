package com.yc.ycandroidtool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.catonhelperlib.fps.PerformanceActivity;
import com.yc.longevitylib.LongevityMonitor;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toollib.tool.ToolFileUtils;
import com.yc.toollib.tool.ToolLogUtils;
import com.yc.ycandroidtool.fs.StudentActivity;
import com.yc.ycandroidtool.memory.MemoryActivity;
import com.yc.ycandroidtool.net.NetworkActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
        findViewById(R.id.tv_9).setOnClickListener(this);

        //开启保活sdk
        LongevityMonitor.onActivityCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                startActivity(new Intent(this,FirstActivity.class));
                break;
            case R.id.tv_2:
                break;
            case R.id.tv_3:
                CrashToolUtils.startCrashListActivity(this);
                break;
            case R.id.tv_4:
                CrashToolUtils.startCrashTestActivity(this);
                break;
            case R.id.tv_5:
                //崩溃文件存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashLogs
                //崩溃页面截图存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashPics
                String crashLogPath = ToolFileUtils.getCrashLogPath(this);
                String crashPicPath = ToolFileUtils.getCrashPicPath(this);
                Toast.makeText(this,crashLogPath,Toast.LENGTH_SHORT).show();
                ToolLogUtils.i("目录---crashLogPath-"+crashLogPath);
                ToolLogUtils.i("目录---crashPicPath-"+crashPicPath);
                break;
            case R.id.tv_6:
                startActivity(new Intent(this, StudentActivity.class));
                break;
            case R.id.tv_7:
                startActivity(new Intent(this, NetworkActivity.class));
                break;
            case R.id.tv_8:
                startActivity(new Intent(this, MemoryActivity.class));
                break;
            case R.id.tv_9:
                startActivity(new Intent(this, PerformanceActivity.class));
                break;
        }
    }
}