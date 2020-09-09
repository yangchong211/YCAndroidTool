package com.yc.toollib.network.data;


import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : DataPoolImpl
 *     revise:
 * </pre>
 */
public class IDataPoolHandleImpl implements IDataPoolHandle {

    private static IDataPoolHandleImpl sDataPoolImpl;
    private HashMap<String, NetworkFeedBean> mNetworkFeedMap;
    private Map<String, NetworkTraceBean> mTraceModelMap;

    public static IDataPoolHandleImpl getInstance() {
        if (sDataPoolImpl == null) {
            sDataPoolImpl = new IDataPoolHandleImpl();
        }
        return sDataPoolImpl;
    }

    private IDataPoolHandleImpl() {
        initDataPool();
    }

    @Override
    public void initDataPool() {
        if (mNetworkFeedMap == null) {
            mNetworkFeedMap = new HashMap();
        }
        mNetworkFeedMap.clear();
    }

    @Override
    public void clearDataPool() {
        if (mNetworkFeedMap != null) {
            mNetworkFeedMap.clear();
        }
    }

    @Override
    public void addNetworkFeedData(String key, NetworkFeedBean networkFeedModel) {
        if (mNetworkFeedMap == null) {
            initDataPool();
        }
        mNetworkFeedMap.put(key, networkFeedModel);
    }

    @Override
    public void removeNetworkFeedData(String key) {
        if (mNetworkFeedMap == null) {
            initDataPool();
            return;
        }
        if (mNetworkFeedMap.containsKey(key)) {
            mNetworkFeedMap.remove(key);
        }
    }

    @Override
    public HashMap<String, NetworkFeedBean> getNetworkFeedMap() {
        return mNetworkFeedMap;
    }

    @Override
    public NetworkFeedBean getNetworkFeedModel(String requestId) {
        if (mNetworkFeedMap == null) {
            initDataPool();
        }
        NetworkFeedBean networkFeedModel = mNetworkFeedMap.get(requestId);
        if (networkFeedModel == null) {
            networkFeedModel = new NetworkFeedBean();
            networkFeedModel.setRequestId(requestId);
            networkFeedModel.setCreateTime(System.currentTimeMillis());
            mNetworkFeedMap.put(requestId, networkFeedModel);
        }
        return networkFeedModel;
    }

    @Override
    public NetworkTraceBean getNetworkTraceModel(String id) {
        if (mTraceModelMap == null) {
            mTraceModelMap = new HashMap<>();
        }
        if (mTraceModelMap.containsKey(id)) {
            return mTraceModelMap.get(id);
        } else {
            NetworkTraceBean traceModel = new NetworkTraceBean();
            traceModel.setId(id);
            traceModel.setTime(System.currentTimeMillis());
            mTraceModelMap.put(id, traceModel);
            return traceModel;
        }
    }

    public Map<String, NetworkTraceBean> getTraceModelMap() {
        return mTraceModelMap;
    }
}
