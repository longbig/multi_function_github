package com.longbig.multifunction.config;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author yuyunlong
 * @date 2023/3/5 10:26
 * @description
 */
public class BaseConstant {

    public static final String CHAT_FLOW_OPEN = "开始连续对话";

    public static final String CHAT_FLOW_CLOSE = "结束连续对话";

    public static final String CHAT_GPT_4_OPEN = "开始GPT4";

    public static final String CHAT_GPT_4_CLOSE = "结束GPT4";

    public static List<String> chatArrayList = Lists.newArrayList();

    static {
        chatArrayList.add(CHAT_FLOW_CLOSE);
        chatArrayList.add(CHAT_FLOW_OPEN);
        chatArrayList.add(CHAT_GPT_4_OPEN);
        chatArrayList.add(CHAT_GPT_4_CLOSE);
    }

    public static Boolean isInChatArray(String message) {
        return chatArrayList.contains(message);
    }



}
