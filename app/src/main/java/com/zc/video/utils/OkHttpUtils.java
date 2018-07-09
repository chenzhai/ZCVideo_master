package com.zc.video.utils;

import com.zc.video.AppManager;

import okhttp3.Callback;
import okhttp3.Request;

public class OkHttpUtils {

    private static final String REQUEST_TAG = "okhttp";

    public static Request buildRuquest(String url) {
        if (AppManager.isNetWorkAvailable()) {
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
            return request;
        }
        return null;
    }

    public static void excute(String url, Callback callback) {
        Request request = buildRuquest(url);
        excute(request,callback);
    }

    public static void excute(Request request, Callback callback) {
        AppManager.getOkHttpClient().newCall(request).enqueue(callback);
    }
}
