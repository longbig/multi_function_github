package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/6/4 00:31
 * @description 字段说明：https://developer.work.weixin.qq.com/document/path/94677#%E6%96%87%E6%9C%AC%E6%B6%88%E6%81%AF
 */
@ToString
public class KefuSendTextDTO {
    /**
     * 字段文档：https://developer.work.weixin.qq.com/document/path/94677#文本消息
     */
    private String touser;
    private String open_kfid;
    private String msgid;
    private String msgtype = "text";
    private TextContentDTO text;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getOpen_kfid() {
        return open_kfid;
    }

    public void setOpen_kfid(String open_kfid) {
        this.open_kfid = open_kfid;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public TextContentDTO getText() {
        return text;
    }

    public void setText(TextContentDTO text) {
        this.text = text;
    }
}
