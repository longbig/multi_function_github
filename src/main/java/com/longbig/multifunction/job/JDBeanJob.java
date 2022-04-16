package com.longbig.multifunction.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author yuyunlong
 * @date 2022/2/27 12:54 下午
 * @description
 */
@Component
@Slf4j
public class JDBeanJob {

    /**
     * 定时任务，每天8点执行
     * @throws Exception
     */
    @Scheduled(cron = "0 0 8 1/1 * ?")
    public String getJd() throws Exception {
        log.info("京豆任务开始");
        String result = OkHttpUtils.getJD();
        log.info("任务执行成功");
        log.info("返回消息:{}", result);
        return result;
    }
}
