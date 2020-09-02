package com.yc.ycandroidtool;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
                Integer.parseInt("12.3");
                break;
            case R.id.tv_2:
                ArrayList<String> list = new ArrayList<>();
                list.get(5);
                break;
            case R.id.tv_3:
                Activity activity = null;
                activity.finish();
                break;
        }
    }

}
