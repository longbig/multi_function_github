package com.longbig.multifunction.model.wechat.kf;

import lombok.ToString;

import java.util.List;

/**
 * @author yuyunlong
 * @date 2023/6/3 21:48
 * @description 微信客服读取接收消息文档：https://developer.work.weixin.qq.com/document/path/94670
 */
@ToString
public class KefuDataDTO {
    private Integer errcode;
    private String errmsg;
    private String next_cursor;
    private Integer has_more;
    private List<KefuDataMsgDTO> msg_list;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public Integer getHas_more() {
        return has_more;
    }

    public void setHas_more(Integer has_more) {
        this.has_more = has_more;
    }

    public List<KefuDataMsgDTO> getMsg_list() {
        return msg_list;
    }

    public void setMsg_list(List<KefuDataMsgDTO> msg_list) {
        this.msg_list = msg_list;
    }
}
