package com.longbig.multifunction.model.chatgpt;

import lombok.ToString;

/**
 * @author yuyunlong
 * @date 2023/3/4 20:47
 * @description
 */
@ToString
public class GptMessageDto {
    private String role;
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
