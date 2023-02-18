package com.longbig.multifunction.dto;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author yuyunlong
 * @date 2023/2/13 8:40 上午
 * @description
 */
@XmlRootElement(name = "xml")
@ToString
public class WechatXmlDTO implements Serializable {
    private static final long serialVersionUID = 10002L;

    @XmlElement(name = "ToUserName")
    private String ToUserName;

    @XmlElement(name = "AgentID")
    private String AgentID;

    @XmlElement(name = "Encrypt")
    private String Encrypt;


    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getEncrypt() {
        return Encrypt;
    }

    public void setEncrypt(String encrypt) {
        Encrypt = encrypt;
    }
}
