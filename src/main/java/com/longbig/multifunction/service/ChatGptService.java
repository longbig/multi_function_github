package com.longbig.multifunction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longbig.multifunction.config.BaseConfig;
import com.longbig.multifunction.model.chatgpt.GptMessageDto;
import com.longbig.multifunction.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yuyunlong
 * @date 2023/2/12 6:53 下午
 * @description
 */
@Service
@Slf4j
public class ChatGptService {

    @Autowired
    private BaseConfig baseConfig;

    /**
     * openai complete功能
     * 接口文档：https://platform.openai.com/docs/api-reference/completions/create
     *
     * @return
     */
    public String openAiComplete(String text) throws Exception {
        log.info("调用GPT3模型对话,text:{}", text);
        Map<String, String> header = Maps.newHashMap();
        String drawUrl = "https://api.openai.com/v1/completions";
        String cookie = "";
        header.put("Authorization", "Bearer " + baseConfig.getChatGptApiKey());
        Map<String, Object> body = Maps.newHashMap();
        body.put("model", "text-davinci-003");
        body.put("prompt", text);
        body.put("max_tokens", 1024);
        body.put("temperature", 1);
        MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON1, JSON.toJSONString(body));

        String response = OkHttpUtils.post(drawUrl, cookie, requestBody, header);
        if (StringUtils.isBlank(response)) {
            return "访问超时";
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("choices");
        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        String result = (String) jsonObject1.get("text");
        log.info("openAiComplete result:{}", result);
        return result;
    }

    /**
     * openai GPT 3.5 complete功能
     * 接口文档：https://platform.openai.com/docs/api-reference/completions/create
     *
     * @return
     */
    public String gptNewComplete(String text) {
        log.info("调用GPT3.5模型对话,text:{}", text);
        Map<String, String> header = Maps.newHashMap();
        String drawUrl = "https://api.openai.com/v1/chat/completions";
        String cookie = "";
        header.put("Authorization", "Bearer " + baseConfig.getChatGptApiKey());
        Map<String, Object> body = Maps.newHashMap();
        List<GptMessageDto> msgs = Lists.newArrayList();
        GptMessageDto gptMessageDto = new GptMessageDto();
        gptMessageDto.setRole("user");
        gptMessageDto.setContent(text);
        msgs.add(gptMessageDto);
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", msgs);
        body.put("max_tokens", 1024);
        body.put("temperature", 1);
        MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON1, JSON.toJSONString(body));

        String response = null;
        try {
            response = OkHttpUtils.post(drawUrl, cookie, requestBody, header);
        } catch (Exception e) {
            return "访问超时";
        }

        if (StringUtils.isBlank(response)) {
            return "访问超时";
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("choices");
        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        JSONObject jsonObject2 = (JSONObject) jsonObject1.get("message");
        String result = (String) jsonObject2.get("content");
        log.info("gptNewComplete result:{}", result);
        return result;
    }

}
