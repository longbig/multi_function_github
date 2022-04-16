package com.longbig.multifunction.job;

import okhttp3.*;

/**
 * @author yuyunlong
 * @date 2021/10/16 12:57 上午
 * @description
 */
public class OkHttpUtils {


    public static String getJD() throws Exception {
        String url = "https://api.m.jd.com/client.action?functionId=signBeanAct&body=%7B%22fp%22%3A%22-1%22%2C%22shshshfp%22%3A%22-1%22%2C%22shshshfpa%22%3A%22-1%22%2C%22referUrl%22%3A%22-1%22%2C%22userAgent%22%3A%22-1%22%2C%22jda%22%3A%22-1%22%2C%22rnVersion%22%3A%223.9%22%7D&appid=ld&client=apple&clientVersion=10.0.4&networkType=wifi&osVersion=14.8.1&uuid=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6&openudid=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6&jsonp=jsonp_1645885800574_58482";

        String userAgent = "okhttp/3.12.1;jdmall;android;version/10.3.4;build/92451;";
        String cookie = "__jd_ref_cls=JingDou_SceneHome_NewGuidExpo; mba_muid=1645885780097318205272.81.1645885790055; mba_sid=81.5; __jda=122270672.1645885780097318205272.1645885780.1645885780.1645885780.1; __jdb=122270672.1.1645885780097318205272|1.1645885780; __jdc=122270672; __jdv=122270672%7Ckong%7Ct_1000170135%7Ctuiguang%7Cnotset%7C1644027879157; pre_seq=0; pre_session=3acd1f6361f86fc0a1bc23971b2e7bbe6197afb6|143; unpl=JF8EAKZnNSttWRkDURtVThUWHAgEWw1dH0dXOjMMAFVcTQQAEwZORxR7XlVdXhRKFx9sZhRUX1NIVw4YBCsiEEpcV1ZVC0kVAV9XNVddaEpkBRwAExEZQ1lWW1kMTBcEaWcAUVpeS1c1KwUbGyB7bVFeXAlOFQJobwxkXGhJVQQZBR0UFU1bZBUzCQYXBG1vBl1VXElRAR8FGxUWS1hRWVsISCcBb2cHUm1b%7CV2_ZzNtbRYAFxd9DUNcKRxYB2ILGloRUUYcIVpAAHsbWQZjVBEJclRCFnUUR11nGlgUZgIZXkFcQRRFCEJkexhdB24LFFtEUHMQfQ5GXH0pXAQJbRZeLAcCVEULRmR6KV5VNVYSCkVVRBUiAUEDKRgMBTRREV9KUUNGdlxAByhNWwVvBUIKEVBzJXwJdlR6GF0GZAoUWUdRQCUpUBkCJE0ZWTVcIlxyVnMURUooDytAGlU1Vl9fEgUWFSIPRFN7TlUCMFETDUIEERZ3AEBUKBoIAzRQRlpCX0VFIltBZHopXA%253d%253d; " +
                "pt_key=【替换为你的pt_key】; " +
                "pt_pin=【替换为你的pt_pin】; pwdt_id=jd_505bacd333f6b; sid=1b2c8b7ce820c4188f048e689bf58c8w; visitkey=36446698972455355";
        RequestBody body = new FormBody.Builder().add("body", "").build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Cookie", cookie)
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("connection", "Keep-Alive")
                .addHeader("accept", "*/*")
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        System.out.println(result);
        return result;
    }

}
