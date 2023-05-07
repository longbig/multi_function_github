package com.longbig.multifunction.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.longbig.multifunction.model.chatgpt.GptMessageDto;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yuyunlong
 * @date 2023/2/18 9:44 下午
 * @description
 */
public class CacheHelper {

    private static Cache<String, String> cache;

    private static Cache<String, List<GptMessageDto>> chatGptCache;

    //用户连续对话开关
    private static Cache<String, Boolean> userChatFlowSwitch;
    //用户GPT4对话开关
    private static Cache<String, Boolean> userChatGpt4Switch;

    static {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(120, TimeUnit.MINUTES)
                .build();

        chatGptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();

        userChatFlowSwitch = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();

        userChatGpt4Switch = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build();
    }

    public static void set(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.getIfPresent(key);
    }

    public static void setGptCache(String username, List<GptMessageDto> gptMessageDtos) {
        chatGptCache.put(username, gptMessageDtos);
    }

    public static List<GptMessageDto> getGptCache(String username) {
        List<GptMessageDto> messageDtos = chatGptCache.getIfPresent(username);
        if (CollectionUtils.isEmpty(messageDtos)) {
            return Lists.newArrayList();
        }
        return messageDtos;
    }

    public static void setUserChatFlowOpen(String username) {
        userChatFlowSwitch.put(username, true);
    }

    public static void setUserChatFlowClose(String username) {
        userChatFlowSwitch.put(username, false);
        chatGptCache.invalidate(username);
    }

    public static Boolean getUserChatFlowSwitch(String username) {
        Boolean result = userChatFlowSwitch.getIfPresent(username);
        if (Objects.isNull(result)) {
            return false;
        }
        return result;
    }

    public static void setUserChatGpt4Open(String username) {
        userChatGpt4Switch.put(username, true);
    }

    public static void setUserChatGpt4Close(String username) {
        userChatGpt4Switch.put(username, false);
    }

    public static Boolean getUserChatGpt4Switch(String username) {
        Boolean result = userChatGpt4Switch.getIfPresent(username);
        if (Objects.isNull(result)) {
            return false;
        }
        return result;
    }
}
