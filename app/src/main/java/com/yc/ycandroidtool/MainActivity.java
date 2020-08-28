package com.yc.ycandroidtool;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yc.toollib.crash.CrashListActivity;
import com.yc.toollib.crash.CrashToolUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    String a = "aa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                startActivity(new Intent(this,FirstActivity.class));
                break;
            case R.id.tv_2:
                startActivity(new Intent(this,TestActivity.class));
                break;
            case R.id.tv_3:
                CrashToolUtils.startCrashListActivity(this);
                break;
            case R.id.tv_4:
                CrashToolUtils.startCrashTestActivity(this);
                break;
        }
    }
}