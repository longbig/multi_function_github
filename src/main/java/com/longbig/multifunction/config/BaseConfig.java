package com.longbig.multifunction.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuyunlong
 * @date 2023/2/18 9:37 下午
 * @description
 */
@Configuration
@Getter
public class BaseConfig {

    @Value("${wechat.sToken}")
    private String sToken;
    @Value("${wechat.sCorpID}")
    private String sCorpID;
    @Value("${wechat.sEncodingAESKey}")
    private String sEncodingAESKey;

    @Value("${wechat.corpsecret}")
    private String corpsecret;

    @Value("${wechat.agentId}")
    private String agentId;

    @Value("${chatgpt.apiKey}")
    private String chatGptApiKey;

    @Value("${chatgpt.flow.num}")
    private Integer chatGptFlowNum;

}
