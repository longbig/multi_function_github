package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/6/4 00:32
 * @description
 */
@ToString
public class TextContentDTO {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
