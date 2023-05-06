package com.longbig.multifunction.api;

import com.longbig.multifunction.config.BaseConfig;
import com.longbig.multifunction.config.BaseConstant;
import com.longbig.multifunction.dto.WechatXmlDTO;
import com.longbig.multifunction.model.wechat.aes.WXBizMsgCrypt;
import com.longbig.multifunction.service.ChatGptService;
import com.longbig.multifunction.service.WeChatService;
import com.longbig.multifunction.utils.CacheHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author yuyunlong
 * @date 2023/2/18 6:29 下午
 * @description
 */
@RestController
@Slf4j
public class ChatGPTController {


    @Autowired
    private BaseConfig baseConfig;

    @Autowired
    private ChatGptService chatGptService;
    @Autowired
    private WeChatService weChatService;

    private Executor executor = Executors.newCachedThreadPool();

    @GetMapping("/receiveMsgFromWechat")
    public String receiveMsgFromDd(@RequestParam("msg_signature") String msg_signature,
                                   @RequestParam("timestamp") String timestamp,
                                   @RequestParam("nonce") String nonce,
                                   @RequestParam("echostr") String echostr) throws Exception {
        log.info("receiveMsgFromDd, msg_signature:{}, timestamp:{}, nonce:{}, echostr:{}",
                msg_signature, timestamp, nonce, echostr);
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(baseConfig.getSToken(), baseConfig.getSEncodingAESKey(),
                baseConfig.getSCorpID());

        String sEchoStr = null;
        try {
            sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,
                    nonce, echostr);
            log.info("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            return sEchoStr;
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            log.error("verifyurl error,e={}", e);
            return "";
        }
    }


    @PostMapping(value = "/receiveMsgFromWechat",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=utf-8")
    public String receiveMsgFromDd(@RequestParam("msg_signature") String msg_signature,
                                   @RequestParam("timestamp") String timestamp,
                                   @RequestParam("nonce") String nonce,
                                   @RequestBody WechatXmlDTO body) throws Exception {
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(baseConfig.getSToken(), baseConfig.getSEncodingAESKey(),
                baseConfig.getSCorpID());

        String sEchoStr = null;
        try {
            String msg = body.getEncrypt();
            String xmlcontent = wxcpt.decrypt(msg);
            log.info("xml content msg: " + xmlcontent);
            String data = StringUtils.substringBetween(xmlcontent, "<Content><![CDATA[", "]]></Content>");
            executor.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    String fromUser = StringUtils.substringBetween(xmlcontent, "<FromUserName><![CDATA[", "]]></FromUserName>");
                    //是否开启连续对话
                    if (BaseConstant.CHAT_FLOW_OPEN.equals(data) || BaseConstant.CHAT_FLOW_CLOSE.equals(data)) {
                        ChatFlowhandler(data, fromUser);
                        return;
                    }
                    // 调openai
                    String result = chatGptService.gptNewComplete(data, fromUser);
                    //给微信发消息
                    String send = weChatService.sendMsg(result, fromUser);
                }
            });
            return data;
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            log.error("DecryptMsg msg error,e={}", e);
            return "";
        }
    }

    private void ChatFlowhandler(String data, String fromUser) {
        String result = "";
        if (BaseConstant.CHAT_FLOW_OPEN.equals(data)) {
            CacheHelper.setUserChatFlowOpen(fromUser);
            result = "连续对话开启，有效期30分钟，连续对话超过" + baseConfig.getChatGptFlowNum() + "次后自动关闭";
        } else if (BaseConstant.CHAT_FLOW_CLOSE.equals(data)) {
            CacheHelper.setUserChatFlowClose(fromUser);
            result = "连续对话关闭";
        } else if (BaseConstant.CHAT_GPT_4_OPEN.equals(data)) {
            CacheHelper.setUserChatGpt4Open(fromUser);
            result = "GPT4对话开启";
        } else if (BaseConstant.CHAT_GPT_4_CLOSE.equals(data)) {
            CacheHelper.setUserChatGpt4Close(fromUser);
            result = "GPT4对话关闭";
        }

        try {
            String send = weChatService.sendMsg(result, fromUser);
        } catch (Exception e) {
            log.error("weChatService.sendMsg error,e={}", e);
        }

    }
}
