package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/6/4 00:23
 * @description
 */
@ToString
public class KefuHandleDTO {
    private String data;
    private String fromUser;
    private String openKfid;
    private String chatGptData;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getOpenKfid() {
        return openKfid;
    }

    public void setOpenKfid(String openKfid) {
        this.openKfid = openKfid;
    }

    public String getChatGptData() {
        return chatGptData;
    }

    public void setChatGptData(String chatGptData) {
        this.chatGptData = chatGptData;
    }
}
