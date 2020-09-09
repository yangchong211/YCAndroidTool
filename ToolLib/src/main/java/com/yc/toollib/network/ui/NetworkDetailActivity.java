package com.yc.toollib.network.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yc.toollib.R;
import com.yc.toollib.network.data.IDataPoolHandleImpl;
import com.yc.toollib.network.data.NetworkFeedBean;
import com.yc.toollib.network.utils.NetWorkUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class NetworkDetailActivity extends AppCompatActivity {

    public static final int JSON_INDENT = 4;
    private NetworkFeedBean mNetworkFeedModel;
    private TextView mCURLTextView;
    private TextView mRequestHeadersTextView;
    private TextView mResponseHeadersTextView;
    private TextView mBodyTextView;

    public static void start(Context context, String requestId) {
        Intent starter = new Intent(context, NetworkDetailActivity.class);
        starter.putExtra("requestId", requestId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_detail);

        mCURLTextView = findViewById(R.id.curl_content_textView);
        mRequestHeadersTextView = findViewById(R.id.request_headers_textView);
        mResponseHeadersTextView = findViewById(R.id.response_headers_textView);
        mBodyTextView = findViewById(R.id.body_textView);
        initData();
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
        mCURLTextView.setText(mNetworkFeedModel.getCURL());
        mCURLTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetWorkUtils.copyToClipBoard(NetworkDetailActivity.this,
                        mNetworkFeedModel.getCURL());
            }
        });
    }

    private void setRequestHeaders() {
        mRequestHeadersTextView.setText(parseHeadersMapToString(mNetworkFeedModel.getRequestHeadersMap()));
    }

    private void setResponseHeaders() {
        mResponseHeadersTextView.setText(parseHeadersMapToString(mNetworkFeedModel.getResponseHeadersMap()));
    }

    private void setBody() {
        if (mNetworkFeedModel.getContentType().contains("json")) {
            mBodyTextView.setText(formatJson(mNetworkFeedModel.getBody()));
        } else {
            mBodyTextView.setText(mNetworkFeedModel.getBody());
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
                    .append(": ")
                    .append(headers.get(name))
                    .append("\n");
        }
        return headersBuilder.toString();
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

}
