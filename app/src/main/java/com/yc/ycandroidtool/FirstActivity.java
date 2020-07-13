package com.yc.ycandroidtool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                CrashToolUtils.reStartApp1(this,1000);
                break;
            case R.id.tv_2:
                CrashToolUtils.reStartApp2(this,1000);
                break;
            case R.id.tv_3:
                Integer.parseInt("12.3.7");
                break;
        }
    }
}
