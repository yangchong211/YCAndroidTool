package com.yc.ycandroidtool.location;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


import com.yc.mocklocationlib.easymock.LocationUtils;
import com.yc.mocklocationlib.gpsmock.GpsMock;
import com.yc.mocklocationlib.gpsmock.GpsMockFragment;
import com.yc.toollib.tool.ToolLogUtils;
import com.yc.ycandroidtool.R;

public class LocationActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_content_view);

        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意6.0及以上版本需要在申请完权限后调用方法
                LocationUtils.getInstance(LocationActivity.this).setAddressCallback(new LocationUtils.AddressCallback() {
                    @Override
                    public void onGetAddress(Address address) {
                        String countryName = address.getCountryName();//国家
                        String adminArea = address.getAdminArea();//省
                        String locality = address.getLocality();//市
                        String subLocality = address.getSubLocality();//区
                        String featureName = address.getFeatureName();//街道
                        ToolLogUtils.d("LocationUtils 定位地址 countryName："+countryName
                                +" adminArea："+ adminArea+" locality："+ locality+" subLocality："
                                + subLocality+" featureName："+ featureName);
                    }

                    @Override
                    public void onGetLocation(double lat, double lng) {
                        ToolLogUtils.d("LocationUtils 定位经纬度"+" lat："+ lat+" lng："+ lng);
                    }
                });
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsMock.onAppInit(LocationActivity.this);
                GpsMockFragment fragment = new GpsMockFragment();
                FragmentManager fragmentManager = LocationActivity.this.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //transaction.setCustomAnimations(R.anim.push_bottom_in, 0);
                //transaction.show(fragment);       //暂时不用这个
                transaction.add(android.R.id.content, fragment, "GpsMockFragment");
                transaction.commitAllowingStateLoss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtils.getInstance(LocationActivity.this).removeLocationUpdatesListener();
    }
}