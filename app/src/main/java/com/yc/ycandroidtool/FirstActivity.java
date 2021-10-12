package com.yc.ycandroidtool;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.toollib.crash.CrashToolUtils;

import java.util.ArrayList;

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
                CrashToolUtils.reStartApp1(this,2000);
                break;
            case R.id.tv_2:
                CrashToolUtils.reStartApp2(this,2000, MainActivity.class);
                break;
            case R.id.tv_3:
                CrashToolUtils.reStartApp3(this);
                break;
        }
    }

}
