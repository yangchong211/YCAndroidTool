package com.yc.ycandroidtool.location;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.yc.mocklocationlib.gpsmock.GpsMock;
import com.yc.mocklocationlib.gpsmock.GpsMockFragment;
import com.yc.ycandroidtool.R;

public class LocationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_content_view);
        GpsMock.onAppInit(this);

        GpsMockFragment fragment = new GpsMockFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.setCustomAnimations(R.anim.push_bottom_in, 0);
        //transaction.show(fragment);       //暂时不用这个
        transaction.add(R.id.fl_container, fragment, "GpsMockFragment");
        transaction.commitAllowingStateLoss();
    }


}