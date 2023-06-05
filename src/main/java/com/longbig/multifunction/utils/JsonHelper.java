package com.longbig.multifunction.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuyunlong
 * @date 2023/6/3 23:55
 * @description
 */
@Slf4j
public class JsonHelper {

    public static <T> T parseJsonToObject(String jsonData, Class<T> clazz) {
        try {
            return JSON.parseObject(jsonData, clazz);
        } catch (Exception e) {
            log.error("parseJsonToObject error,e={}", e);
        }
        return null;
    }
}
