package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/6/3 20:58
 * @description 微信客服接收消息触发的xml data
 */
@ToString
public class KefuNoticeDTO {
    private String ToUserName;
    private String CreateTime;
    private String MsgType;
    private String Event;
    private String Token;
    private String OpenKfId;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getOpenKfId() {
        return OpenKfId;
    }

    public void setOpenKfId(String openKfId) {
        OpenKfId = openKfId;
    }
}
