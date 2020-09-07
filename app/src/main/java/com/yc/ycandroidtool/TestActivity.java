package com.yc.ycandroidtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            Activity activity = null;
            activity.finish();
        } else if (view.getId() == R.id.btn2) {
            Intent intent = new Intent(TestActivity.this, TestActivity2.class);
            startActivity(intent);
        }
    }




}
