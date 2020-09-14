package com.yc.toollib.network.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yc.toollib.R;
import com.yc.toollib.network.data.IDataPoolHandleImpl;
import com.yc.toollib.network.data.NetworkFeedBean;
import com.yc.toollib.network.utils.NetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.LinkedHashMap;
import java.util.Map;


public class NetworkDetailActivity extends AppCompatActivity {

    public static final int JSON_INDENT = 4;
    private NetworkFeedBean mNetworkFeedModel;
    private LinearLayout mLlBack;
    private TextView mTvDelete;
    private TextView mTvUrlContent;
    private TextView mTvRequestHeaders;
    private TextView mTvResponseHeaders;
    private TextView mTvBody;

    public static void start(Context context, String requestId) {
        Intent starter = new Intent(context, NetworkDetailActivity.class);
        starter.putExtra("requestId", requestId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_detail);
        initFindViewById();
        initData();
        initListener();
    }

    private void initFindViewById() {


        mLlBack = findViewById(R.id.ll_back);
        mTvDelete = findViewById(R.id.tv_delete);
        mTvUrlContent = findViewById(R.id.tv_url_content);
        mTvRequestHeaders = findViewById(R.id.tv_request_headers);
        mTvResponseHeaders = findViewById(R.id.tv_response_headers);
        mTvBody = findViewById(R.id.tv_body);

    }

    private void initData() {
        String requestId = getIntent().getStringExtra("requestId");
        if (TextUtils.isEmpty(requestId)) {
            return;
        }
        mNetworkFeedModel = IDataPoolHandleImpl.getInstance().getNetworkFeedModel(requestId);
        if (mNetworkFeedModel == null) {
            return;
        }
        setCURLContent();
        setRequestHeaders();
        setResponseHeaders();
        setBody();
    }

    private void setCURLContent() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Request URL",mNetworkFeedModel.getUrl());
        map.put("Request Method",mNetworkFeedModel.getMethod());
        int status = mNetworkFeedModel.getStatus();
        if (status==200){
            map.put("Status Code","200"+"  ok");
        } else {
            map.put("Status Code",status+"  ok");
        }
        map.put("Remote Address","待定");
        map.put("Referrer Policy","待定");
        Format format = new DecimalFormat("#.00");
        String dataSize = format.format(mNetworkFeedModel.getSize() * 0.001) + " KB";
        map.put("size",dataSize);
        map.put("connectTimeoutMillis",mNetworkFeedModel.getConnectTimeoutMillis()+"");
        map.put("readTimeoutMillis",mNetworkFeedModel.getReadTimeoutMillis()+"");
        map.put("writeTimeoutMillis",mNetworkFeedModel.getWriteTimeoutMillis()+"");
        String string = parseHeadersMapToString(map);
        mTvUrlContent.setText(string);
        mTvUrlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWorkUtils.copyToClipBoard(NetworkDetailActivity.this,
                        mNetworkFeedModel.getCURL());
            }
        });
    }

    private void setRequestHeaders() {
        Map<String, String> requestHeadersMap = mNetworkFeedModel.getRequestHeadersMap();
        String string = parseHeadersMapToString(requestHeadersMap);
        mTvRequestHeaders.setText(string);
    }

    private void setResponseHeaders() {
        Map<String, String> responseHeadersMap = mNetworkFeedModel.getResponseHeadersMap();
        String string = parseHeadersMapToString(responseHeadersMap);
        mTvResponseHeaders.setText(string);
    }

    private void setBody() {
        if (mNetworkFeedModel.getContentType().contains("json")) {
            mTvBody.setText(formatJson(mNetworkFeedModel.getBody()));
        } else {
            mTvBody.setText(mNetworkFeedModel.getBody());
        }
    }

    private String parseHeadersMapToString(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return "Header is Empty.";
        }
        StringBuilder headersBuilder = new StringBuilder();
        for (String name : headers.keySet()) {
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            headersBuilder
                    .append(name)
                    .append(" : ")
                    .append(headers.get(name))
                    .append("\n");
        }
        return headersBuilder.toString().trim();
    }

    private String formatJson(String body) {
        String message;
        try {
            if (body.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(body);
                message = jsonObject.toString(JSON_INDENT);
            } else if (body.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(body);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = body;
            }
        } catch (JSONException e) {
            message = body;
        }
        return message;
    }

    private void initListener() {
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NetworkDetailActivity.this,"后期完善",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
