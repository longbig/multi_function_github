package com.longbig.multifunction.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Map;

/**
 * @author yuyunlong
 * @date 2021/10/16 12:57 上午
 * @description
 */
@Slf4j
public class OkHttpUtils {

    public static String post(String url, String cookie, RequestBody requestBody, Map<String, String> header) throws Exception {

        String userAgent = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(Headers.of(header))
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("post请求,result:{}", result);
        return result;
    }

    public static String get(String url, String cookie, Map<String, String> header) throws Exception {

        String userAgent = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(header))
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        log.info("get请求,result:{}", result);
        return result;
    }

}
