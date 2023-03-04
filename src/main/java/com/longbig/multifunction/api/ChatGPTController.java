package com.longbig.multifunction.api;

import com.longbig.multifunction.config.BaseConfig;
import com.longbig.multifunction.dto.WechatXmlDTO;
import com.longbig.multifunction.model.wechat.aes.WXBizMsgCrypt;
import com.longbig.multifunction.service.ChatGptService;
import com.longbig.multifunction.service.WeChatService;
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
                    // 调openai
                    String result = chatGptService.gptNewComplete(data);
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
}
