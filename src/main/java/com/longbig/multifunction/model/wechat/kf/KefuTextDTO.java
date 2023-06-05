package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/6/4 00:02
 * @description
 */
@ToString
public class KefuTextDTO {
    private String content;
    private String menu_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }
}
