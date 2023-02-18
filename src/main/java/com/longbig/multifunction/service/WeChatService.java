package com.longbig.multifunction.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.longbig.multifunction.config.BaseConfig;
import com.longbig.multifunction.utils.CacheHelper;
import com.longbig.multifunction.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuyunlong
 * @date 2023/2/13 11:16 下午
 * @description
 */
@Service
@Slf4j
public class WeChatService {

    @Autowired
    private BaseConfig baseConfig;

    private String WECHAT_TOKEN = "WECHAT_TOKEN";

    public String getAccessToken() throws Exception {

        String data = CacheHelper.get(WECHAT_TOKEN);
        if (StringUtils.isNotEmpty(data)) {
            log.info("cache data:{}", data);
            return data;
        }
        String corpid = baseConfig.getSCorpID();
        String corpsecret = baseConfig.getCorpsecret();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid +
                "&corpsecret=" + corpsecret;
        String jsonData = OkHttpUtils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        String accessToken = jsonObject.getString("access_token");
        CacheHelper.set(WECHAT_TOKEN, accessToken);
        return accessToken;
    }


    public String sendMsg(String msg, String touser) throws Exception {
        String accessToken = null;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            log.error("sendMsg getAccessToken error,e={}", e);
            return "fail";
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        String body = "{\n" +
                "   \"touser\" : \"" + touser + "\",\n" +
                "   \"msgtype\" : \"text\",\n" +
                "   \"agentid\" : 1000003,\n" +
                "   \"text\" : {\n" +
                "       \"content\" : \"" + msg + "\"\n" +
                "   },\n" +
                "   \"safe\":0,\n" +
                "   \"enable_id_trans\": 0,\n" +
                "   \"enable_duplicate_check\": 0,\n" +
                "   \"duplicate_check_interval\": 1800\n" +
                "}";
        MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON1, body);
        log.info("send msg:{}", requestBody);
        OkHttpUtils.post(url, "", requestBody, Maps.newHashMap());
        return "success;";
    }

}
