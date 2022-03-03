package com.yc.ycandroidtool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.catonhelperlib.fps.PerformanceActivity;
import com.yc.catonhelperlib.fps.PerformanceManager;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.netlib.connect.ConnectionActivity;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toollib.tool.ToolFileUtils;
import com.yc.toollib.tool.ToolLogUtils;
import com.yc.ycandroidtool.anr.AnrActivity;
import com.yc.ycandroidtool.canary.CanaryActivity;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                CrashToolUtils.startCrashTestActivity(this);
                break;
            case R.id.tv_2:
                CrashToolUtils.startCrashListActivity(this);
                break;
            case R.id.tv_3:
                startActivity(new Intent(this, NetworkActivity.class));
                break;
            case R.id.tv_4:
                startActivity(new Intent(this, FileExplorerActivity.class));
                break;
            case R.id.tv_5:
                startActivity(new Intent(this, ConnectionActivity.class));
                break;
            case R.id.tv_6:
                startActivity(new Intent(this, CanaryActivity.class));
                break;
        }
    }
}