package com.yc.toollib.network.ui;

import android.content.Context;
import android.widget.TextView;

import com.yc.toollib.R;
import com.yc.toollib.network.data.NetworkTraceBean;
import com.yc.toollib.network.utils.NetWorkUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public class NetworkTraceAdapter extends BaseRecycleAdapter<NetworkTraceBean> {

    public NetworkTraceAdapter(Context context) {
        super(context,R.layout.item_network_trace);
    }

    @Override
    protected void bindData(BaseViewHolder holder, NetworkTraceBean networkTraceBean) {
        TextView urlTextView = holder.getView(R.id.tv_trace_url);
        NetTraceView traceDetailView = holder.getView(R.id.ntv_trace_detail_view);
        String url = networkTraceBean.getUrl();
        Map<String, Long> networkEventsMap = networkTraceBean.getNetworkEventsMap();
        LinkedHashMap<String, Long> stringLongLinkedHashMap = NetWorkUtils.transformToTraceDetail(networkEventsMap);
        urlTextView.setText(url);
        traceDetailView.addTraceDetail(stringLongLinkedHashMap);
    }

}
