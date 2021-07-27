package com.yc.mocklocationlib.gpsmock;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yc.mocklocationlib.R;
import com.yc.mocklocationlib.gpsmock.web.MyWebView;
import com.yc.mocklocationlib.gpsmock.web.MyWebViewClient;
import com.yc.mocklocationlib.gpsmock.web.WebUtil;

import java.util.ArrayList;
import java.util.List;

public class GpsMockFragment extends BaseFragment implements
        SettingItemAdapter.OnSettingItemSwitchListener,
        MyWebViewClient.InvokeListener {

    private static final String TAG = "GpsMockFragment";
    private HomeTitleBar mTitleBar;
    private RecyclerView mSettingList;
    private SettingItemAdapter mSettingItemAdapter;
    private EditText mLongitude;
    private EditText mLatitude;
    private TextView mMockLocationBtn;
    private MyWebView mWebView;

    public GpsMockFragment() {

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.intiSettingList();
        this.initTitleBar();
        this.initMockLocationArea();
        this.initWebView();
    }

    private void initWebView() {
        this.mWebView = (MyWebView)this.findViewById(R.id.web_view);
        WebUtil.webViewLoadLocalHtml(this.mWebView, "html/map.html");
        this.mWebView.addInvokeListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mWebView.removeInvokeListener(this);
    }

    private void initMockLocationArea() {
        this.mLongitude = (EditText)this.findViewById(R.id.longitude);
        this.mLatitude = (EditText)this.findViewById(R.id.latitude);
        this.mLatitude.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (GpsMockFragment.this.checkInput()) {
                    GpsMockFragment.this.mMockLocationBtn.setEnabled(true);
                } else {
                    GpsMockFragment.this.mMockLocationBtn.setEnabled(false);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.mLongitude.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (GpsMockFragment.this.checkInput()) {
                    GpsMockFragment.this.mMockLocationBtn.setEnabled(true);
                } else {
                    GpsMockFragment.this.mMockLocationBtn.setEnabled(false);
                }

            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.mMockLocationBtn = (TextView)this.findViewById(R.id.mock_location);
        this.mMockLocationBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GpsMockFragment.this.checkInput()) {
                    double latitude = Double.valueOf(GpsMockFragment.this.mLatitude.getText().toString());
                    double longitude = Double.valueOf(GpsMockFragment.this.mLongitude.getText().toString());
                    GpsMockManager.getInstance().mockLocation(latitude, longitude);
                    GpsMockConfig.saveMockLocation(GpsMockFragment.this.getContext(), new LatLng(latitude, longitude));
                    Toast.makeText(GpsMockFragment.this.getContext(),
                            GpsMockFragment.this.getString(
                                    R.string.dk_gps_location_change_toast,
                                    new Object[]{GpsMockFragment.this.mLatitude.getText(),
                                            GpsMockFragment.this.mLongitude.getText()}),
                            Toast.LENGTH_LONG).show();
                    //开始开启线程去定位
                    MockGpsManager.getInstance(GpsMockFragment.this.getContext()).start();
                }
            }
        });
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(this.mLongitude.getText().toString())) {
            return false;
        } else if (TextUtils.isEmpty(this.mLatitude.getText().toString())) {
            return false;
        } else {
            try {
                double longitude = Double.valueOf(this.mLongitude.getText().toString());
                double latitude = Double.valueOf(this.mLatitude.getText().toString());
                if (longitude <= 180.0D && longitude >= -180.0D) {
                    return latitude <= 90.0D && latitude >= -90.0D;
                } else {
                    return false;
                }
            } catch (Exception var5) {
                var5.printStackTrace();
                return false;
            }
        }
    }

    private void initTitleBar() {
        this.mTitleBar = (HomeTitleBar)this.findViewById(R.id.title_bar);
        this.mTitleBar.setListener(new HomeTitleBar.OnTitleBarClickListener() {
            public void onRightClick() {
                GpsMockFragment.this.finish();
            }
        });
        mTitleBar.setTitle("mock定位坐标");
        mTitleBar.setIcon(R.drawable.dk_close_icon_big);
    }

    private void intiSettingList() {
        this.mSettingList = (RecyclerView)this.findViewById(R.id.setting_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        this.mSettingList.setLayoutManager(layoutManager);
        List<SettingItem> settingItems = new ArrayList();
        settingItems.add(new SettingItem(R.string.dk_gpsmock_open, GpsMockConfig.isGPSMockOpen(this.getContext())));
        this.mSettingItemAdapter = new SettingItemAdapter(this.getContext());
        this.mSettingItemAdapter.setData(settingItems);
        this.mSettingItemAdapter.setOnSettingItemSwitchListener(this);
        this.mSettingList.setAdapter(this.mSettingItemAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),1);
        this.mSettingList.addItemDecoration(decoration);
    }

    public void onSettingItemSwitch(View view, SettingItem data, boolean on) {
        if (data.desc == R.string.dk_gpsmock_open) {
            GpsMockConfig.setGPSMockOpen(this.getContext(), on);
            if (on) {
                GpsMockManager.getInstance().startMock();
                MockGpsManager.getInstance(this.getContext()).start();
            } else {
                GpsMockManager.getInstance().stopMock();
            }
        }

    }

    protected int onRequestLayout() {
        return R.layout.dk_fragment_gps_mock;
    }

    public void onNativeInvoke(String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            String lastPath = uri.getLastPathSegment();
            if ("sendLocation".equals(lastPath)) {
                String lat = uri.getQueryParameter("lat");
                String lnt = uri.getQueryParameter("lng");
                if (!TextUtils.isEmpty(lat) || !TextUtils.isEmpty(lnt)) {
                    this.mLatitude.setText(lat);
                    this.mLongitude.setText(lnt);
                }
            }
        }
    }
}

