package com.yc.toollib.network.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.yc.toollib.network.data.IDataPoolHandleImpl;
import com.yc.toollib.network.data.NetworkTraceBean;
import com.yc.toollib.tool.ToolLogUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NetWorkUtils {

    public static void copyToClipBoard(Context context , String content){
        if (!TextUtils.isEmpty(content)){
            //获取剪贴版
            ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            //创建ClipData对象
            //第一个参数只是一个标记，随便传入。
            //第二个参数是要复制到剪贴版的内容
            ClipData clip = ClipData.newPlainText("", content);
            //传入clipdata对象.
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copy succeed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static LinkedHashMap<String, Long> transformToTraceDetail(Map<String, Long> eventsTimeMap){
        LinkedHashMap<String, Long> traceDetailList = new LinkedHashMap<>();
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_TOTAL,getEventCostTime(eventsTimeMap, NetworkTraceBean.CALL_START, NetworkTraceBean.CALL_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_DNS,getEventCostTime(eventsTimeMap, NetworkTraceBean.DNS_START, NetworkTraceBean.DNS_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_SECURE_CONNECT,getEventCostTime(eventsTimeMap, NetworkTraceBean.SECURE_CONNECT_START, NetworkTraceBean.SECURE_CONNECT_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_CONNECT,getEventCostTime(eventsTimeMap, NetworkTraceBean.CONNECT_START, NetworkTraceBean.CONNECT_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_REQUEST_HEADERS,getEventCostTime(eventsTimeMap, NetworkTraceBean.REQUEST_HEADERS_START, NetworkTraceBean.REQUEST_HEADERS_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_REQUEST_BODY,getEventCostTime(eventsTimeMap, NetworkTraceBean.REQUEST_BODY_START, NetworkTraceBean.REQUEST_BODY_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_RESPONSE_HEADERS,getEventCostTime(eventsTimeMap, NetworkTraceBean.RESPONSE_HEADERS_START, NetworkTraceBean.RESPONSE_HEADERS_END));
        traceDetailList.put(NetworkTraceBean.TRACE_NAME_RESPONSE_BODY,getEventCostTime(eventsTimeMap, NetworkTraceBean.RESPONSE_BODY_START, NetworkTraceBean.RESPONSE_BODY_END));
        return traceDetailList;
    }

    public static long getEventCostTime(Map<String , Long> eventsTimeMap, String startName, String endName){
        if (!eventsTimeMap.containsKey(startName) || !eventsTimeMap.containsKey(endName)) {
            return 0;
        }
        Long endTime = eventsTimeMap.get(endName);
        Long start = eventsTimeMap.get(startName);
        long result = endTime - start;
        return result;
    }

    public static void timeoutChecker(String requestId){
        if (requestId==null || requestId.length()==0){
            return;
        }
        NetworkTraceBean networkTraceModel = IDataPoolHandleImpl.getInstance().getNetworkTraceModel(requestId);
        Map<String, Long> traceItemList = networkTraceModel.getTraceItemList();
        String url = networkTraceModel.getUrl();
        check(NetworkTraceBean.TRACE_NAME_TOTAL, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_DNS, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_SECURE_CONNECT, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_CONNECT, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_REQUEST_HEADERS, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_REQUEST_BODY, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_RESPONSE_HEADERS, url, traceItemList);
        check(NetworkTraceBean.TRACE_NAME_RESPONSE_BODY, url, traceItemList);
    }

    private static void check(String key, String url , Map<String , Long> traceItemList){
        Context context = NetworkTool.getInstance().getApplication();
        if (context == null) {
            ToolLogUtils.d("OkNetworkMonitor", "context is null.");
            return;
        }
        Long costTime = traceItemList.get(key);
        /*if (isTimeout(context, key, costTime)) {
            String title = "Timeout warning. $key cost ${costTime}ms.";
            String content = "url: $url";
            Intent intent = new Intent(context, NetRequestActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationDispatcher.showNotification(context, title, content, pendingIntent, notificationId);
        }*/
    }


    public static boolean isTimeout(Context context , String key , Long costTime){
        int value = getSettingTimeout(context, key);
        if (value <= 0 || costTime == null) {
            return false;
        }
        return costTime > value;
    }

    private static int getSettingTimeout(Context context , String key){
        String string = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "0");
        try {
            int i = Integer.parseInt(string);
            return i;
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }
    }


}
