package com.longbig.multifunction.job;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yuyunlong
 * @date 2022/9/4 4:34 下午
 * @description
 */
@Slf4j
@Component
public class JuejinJob {

    @Value("${juejin.Cookie}")
    private String juejinCookie;

    /**
     * 掘金自动签到
     *
     * @return
     */
    @Scheduled(cron = "0 0 9 1/1 * ?")
    public String juejinSign() throws Exception {
        log.info("掘金自动签到开始");
        Map<String, String> header = Maps.newHashMap();
        String url = "https://api.juejin.cn/growth_api/v1/check_in";
        RequestBody requestBody = new FormBody.Builder().build();
        String response = OkHttpUtils.post(url, juejinCookie, requestBody, header);

        return response;
    }

    /**
     * 掘金自动抽奖
     *
     * @return
     */
    @Scheduled(cron = "0 0 9 1/1 * ?")
    public String juejinDraw() throws Exception {
        log.info("掘金自动抽奖开始");
        Map<String, String> header = Maps.newHashMap();
        String drawUrl = "https://api.juejin.cn/growth_api/v1/lottery/draw";
        RequestBody requestBody = new FormBody.Builder().build();
        String response = OkHttpUtils.post(drawUrl, juejinCookie, requestBody, header);
        return response;
    }
}
