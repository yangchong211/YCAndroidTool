package com.yc.toollib.crash;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yc.toollib.R;

import java.util.ArrayList;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 制造异常测试类
 *     revise:
 * </pre>
 */
public class CrashTestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_test);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_1) {
            Integer.parseInt("12.3");
        } else if (id == R.id.tv_2) {
            ArrayList<String> list = new ArrayList<>();
            list.get(5);
        } else if (id == R.id.tv_3) {
            Activity activity = null;
            activity.finish();
        } else if (id == R.id.tv_4){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CrashTestActivity.this,"吐司",Toast.LENGTH_SHORT).show();
                }
            }).start();
        }
    }

}
