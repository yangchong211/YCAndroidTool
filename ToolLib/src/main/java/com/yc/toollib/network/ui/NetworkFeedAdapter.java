package com.yc.toollib.network.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.toollib.R;
import com.yc.toollib.network.data.NetworkFeedBean;

import java.text.DecimalFormat;
import java.text.Format;


public class NetworkFeedAdapter extends BaseRecycleAdapter<NetworkFeedBean> {

    private Context mContext;

    public NetworkFeedAdapter(Context context) {
        super(context,R.layout.item_network_feed);
        mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindData(BaseViewHolder holder, final NetworkFeedBean networkFeedModel) {
        LinearLayout mRootLayout = holder.getView(R.id.ll_feed_root_layout);
        TextView mUrlTextView = holder.getView(R.id.tv_network_feed_url);
        TextView mStatusCodeTextView = holder.getView(R.id.tv_feed_status);
        TextView mSizeTextView = holder.getView(R.id.tv_feed_size);
        TextView mCostTimeTextView = holder.getView(R.id.tv_feed_cost_time);
        TextView mMethodTextView = holder.getView(R.id.tv_feed_method);
        TextView mContentTypeTextView = holder.getView(R.id.tv_feed_content_type);
        View mStatusView = holder.getView(R.id.view_feed_status);


        mUrlTextView.setText(networkFeedModel.getUrl());
        if (networkFeedModel.getStatus() >= 400 && networkFeedModel.getStatus() <= 600) {
            mStatusView.setBackgroundResource(R.color.red);
            mStatusCodeTextView.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            mStatusView.setBackgroundResource(R.color.green);
            mStatusCodeTextView.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        mStatusCodeTextView.setText("Status: "+networkFeedModel.getStatus());

        Format format = new DecimalFormat("#.00");
        String dataSize = format.format(networkFeedModel.getSize() * 0.001) + " KB";
        mSizeTextView.setText(dataSize);
        mCostTimeTextView.setText(networkFeedModel.getCostTime() + " ms");
        mMethodTextView.setText(networkFeedModel.getMethod());
        mContentTypeTextView.setText("ContentType: "+networkFeedModel.getContentType());
    }

}
