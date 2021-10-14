package com.yc.ycandroidtool.anr;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.toollib.crash.CrashToolUtils;
import com.yc.ycandroidtool.MainActivity;
import com.yc.ycandroidtool.R;

public class AnrActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
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
            default:
                break;
        }
    }
}
