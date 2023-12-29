package com.longbig.multifunction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longbig.multifunction.config.BaseConfig;
import com.longbig.multifunction.model.wechat.kf.*;
import com.longbig.multifunction.utils.CacheHelper;
import com.longbig.multifunction.utils.JsonHelper;
import com.longbig.multifunction.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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

    /**
     * 企业微信自建三方应用accessToken名
     */
    private String WECHAT_TOKEN = "WECHAT_ROBOT_TOKEN";
    /**
     * 微信客服accessToken名
     */
    private String KF_TOKEN = "KF_TOKEN";

    private String cursorKey = "cursor";


    private String getAccessToken(String tokenName, String corpsecret) throws Exception {

        String data = CacheHelper.get(tokenName);
        if (StringUtils.isNotEmpty(data)) {
//            log.info("cache data:{}", data);
            return data;
        }
        String corpid = baseConfig.getSCorpID();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid +
                "&corpsecret=" + corpsecret;
        String jsonData = OkHttpUtils.get(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        String accessToken = jsonObject.getString("access_token");
        CacheHelper.set(tokenName, accessToken);
        return accessToken;
    }


    public String sendMsg(String msg, String touser) throws Exception {
        String accessToken = null;
        List<String> msgList = Lists.newArrayList();
        try {
            String corpsecret = baseConfig.getCorpsecret();
            accessToken = getAccessToken(WECHAT_TOKEN, corpsecret);
        } catch (Exception e) {
            log.error("sendMsg getAccessToken error,e={}", e);
            return "fail";
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        if (msg.length() > 2048) {
            int count = msg.length() / 2048 + 1;
            int beginIndex = 0, endIndex = 2048;
            for (int i = 0; i < count; i++) {
                String temp = msg.substring(beginIndex, endIndex);
                msgList.add(temp);
                beginIndex = endIndex;
                endIndex += 2048;
                endIndex = endIndex > msg.length() ? msg.length() : endIndex;
            }
        } else {
            msgList.add(msg);
        }

        for (String s : msgList) {
            // 对 s 中的 " 转义为 \"
            s = s.replaceAll("\"", "\\\\\"");

            String body = "{\n" +
                    "   \"touser\" : \"" + touser + "\",\n" +
                    "   \"msgtype\" : \"markdown\",\n" +
                    "   \"agentid\" : " + baseConfig.getAgentId() + ",\n" +
                    "   \"markdown\" : {\n" +
                    "       \"content\" : \"" + s + "\"\n" +
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
        }
        return "success;";
    }

    public String sendKfMsg(KefuHandleDTO kefuHandleDTO) throws Exception {
        String accessToken = null;
        List<String> msgList = Lists.newArrayList();
        try {
            String corpsecret = baseConfig.getKfsecret();
            accessToken = getAccessToken(KF_TOKEN, corpsecret);
        } catch (Exception e) {
            log.error("sendKfMsg getAccessToken error,e={}", e);
            return "fail";
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/kf/send_msg?access_token=" + accessToken;
        String msg = kefuHandleDTO.getChatGptData();
        if (msg.length() > 2048) {
            int count = msg.length() / 2048 + 1;
            int beginIndex = 0, endIndex = 2048;
            for (int i = 0; i < count; i++) {
                String temp = msg.substring(beginIndex, endIndex);
                msgList.add(temp);
                beginIndex = endIndex;
                endIndex += 2048;
                endIndex = endIndex > msg.length() ? msg.length() : endIndex;
            }
        } else {
            msgList.add(msg);
        }

        for (String s : msgList) {
            String body = "{\n" +
                    "   \"touser\" : \"" + kefuHandleDTO.getFromUser() + "\",\n" +
                    "   \"msgtype\" : \"text\",\n" +
                    "   \"text\" : {\n" +
                    "       \"content\" : \"" + s + "\"\n" +
                    "   },\n" +
                    "   \"open_kfid\": \"" + kefuHandleDTO.getOpenKfid() + "\"\n" +
                    "}";
            MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON1, body);
            log.info("sendKfMsg:{}", requestBody);
            OkHttpUtils.post(url, "", requestBody, Maps.newHashMap());
        }
        return "success";
    }

    /**
     * 获取客服接收的消息
     * @return
     */
    public KefuHandleDTO readKfReceiveMsg(KefuNoticeDTO kefuNoticeDTO) {
        String kfsecret = baseConfig.getKfsecret();
        try {
            String accessToken = getAccessToken(KF_TOKEN, kfsecret);
            String cursorCache = CacheHelper.getWechatCache(cursorKey);

            String url = "https://qyapi.weixin.qq.com/cgi-bin/kf/sync_msg?access_token=" + accessToken;
            Map body = Maps.newHashMap();
            body.put("cursor", cursorCache);
            body.put("token", kefuNoticeDTO.getToken());
            body.put("limit", 1000);
            body.put("voice_format", 0);
            body.put("open_kfid", kefuNoticeDTO.getOpenKfId());
            MediaType JSON1 = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON1, JSON.toJSONString(body));
            String msg = OkHttpUtils.post(url, "", requestBody, Maps.newHashMap());
            if (StringUtils.isBlank(msg)) {
                return null;
            }
            KefuDataDTO kefuDataDTO = JsonHelper.parseJsonToObject(msg, KefuDataDTO.class);
            String cursorNew = kefuDataDTO.getNext_cursor();
            CacheHelper.setWechatCache(cursorKey, cursorNew);

            KefuHandleDTO kefuHandleDTO = new KefuHandleDTO();
            List<KefuDataMsgDTO> kefuDataMsgDTOS = kefuDataDTO.getMsg_list();
            //TODO 这里包含event和text类型数据，看看怎么处理
            if (!CollectionUtils.isEmpty(kefuDataMsgDTOS)) {
                for (KefuDataMsgDTO kefuDataMsgDTO : kefuDataMsgDTOS) {
                    if (kefuDataMsgDTO.getMsgtype().equals("text")) {
                        kefuHandleDTO.setData(kefuDataMsgDTO.getText().getContent());
                        kefuHandleDTO.setFromUser(kefuDataMsgDTO.getExternal_userid());
                        kefuHandleDTO.setOpenKfid(kefuDataMsgDTO.getOpen_kfid());
                        break;
                    }
                }

            }
            return kefuHandleDTO;

        } catch (Exception e) {
            log.error("readKfReceiveMsg error, e={}", e);
        }
        return null;
    }

}
