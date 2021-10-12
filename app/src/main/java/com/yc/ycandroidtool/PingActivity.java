package com.yc.ycandroidtool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.netlib.ping.PingView;


public class PingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEt;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private PingView mPing;

    public static void start(Context context){
        Intent intent = new Intent(context, PingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPing.cancelPing();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);
        initFindViewById();
        initListener();
    }

    private void initFindViewById() {
        mEt = findViewById(R.id.et);
        mBtn1 = findViewById(R.id.btn1);
        mBtn3 = findViewById(R.id.btn3);
        mBtn2 = findViewById(R.id.btn2);
        mPing = findViewById(R.id.ping);
    }

    private void initListener() {
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn1){
            ping1();
        } else if (v == mBtn2){
            ping2();
        } else if (v == mBtn3){

        }
    }

    private void ping1() {
        String net = mEt.getText().toString().trim();
        if (net==null || net.length()==0){
            Toast.makeText(this,"输入内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        mPing.setDeviceId("12312424423");
        mPing.setUserId("342");
        mPing.setVersionName("1.1.1");
        mPing.pingHost(net);
    }

    private void ping2() {
        mPing.setDeviceId("12312424423");
        mPing.setUserId("342");
        mPing.setVersionName("1.1.1");
        mPing.pingHost("https://www.wanandroid.com/article/list/0/json");
    }
}
