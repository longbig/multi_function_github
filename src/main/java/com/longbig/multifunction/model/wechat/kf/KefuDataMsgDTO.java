package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/6/3 21:50
 * @description
 */
@ToString
public class KefuDataMsgDTO {
    /**
     * 字段文档：https://developer.work.weixin.qq.com/document/path/94670
     */
    private String msgid;
    private String open_kfid;
    private String external_userid;
    private Long send_time;
    private Integer origin;
    private String servicer_userid;
    private String msgtype;
    private KefuTextDTO text;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getOpen_kfid() {
        return open_kfid;
    }

    public void setOpen_kfid(String open_kfid) {
        this.open_kfid = open_kfid;
    }

    public String getExternal_userid() {
        return external_userid;
    }

    public void setExternal_userid(String external_userid) {
        this.external_userid = external_userid;
    }

    public Long getSend_time() {
        return send_time;
    }

    public void setSend_time(Long send_time) {
        this.send_time = send_time;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getServicer_userid() {
        return servicer_userid;
    }

    public void setServicer_userid(String servicer_userid) {
        this.servicer_userid = servicer_userid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public KefuTextDTO getText() {
        return text;
    }

    public void setText(KefuTextDTO text) {
        this.text = text;
    }
}
