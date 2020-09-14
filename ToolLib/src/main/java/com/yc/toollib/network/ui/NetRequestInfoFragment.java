package com.yc.toollib.network.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.toollib.R;
import com.yc.toollib.network.data.IDataPoolHandleImpl;
import com.yc.toollib.network.data.NetworkTraceBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class NetRequestInfoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Activity activity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.base_recycler_view, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        NetworkTraceAdapter adapter = new NetworkTraceAdapter(activity);
        List<NetworkTraceBean> traceList = new ArrayList<>();
        Map<String, NetworkTraceBean> traceModelMap = IDataPoolHandleImpl.getInstance().getTraceModelMap();
        if (traceModelMap!=null){
            Collection<NetworkTraceBean> values = traceModelMap.values();
            traceList.addAll(values);
            try {
                Collections.sort(traceList, new Comparator<NetworkTraceBean>() {
                    @Override
                    public int compare(NetworkTraceBean networkTraceModel1, NetworkTraceBean networkTraceModel2) {
                        return (int) (networkTraceModel2.getTime() - networkTraceModel1.getTime());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adapter.setData(traceList);
        mRecyclerView.setAdapter(adapter);
    }
}
