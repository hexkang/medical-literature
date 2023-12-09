package com.example.OCRBackEnd.POJO;

import java.beans.JavaBean;
import java.io.Serializable;

@JavaBean
public class MailRequest implements Serializable {
    private String sendTo;
    private String subject;
    private String text;
    private String filePath; // 附件路径

    public String getFilePath() {
        return filePath;
    }

    public String getSendTo() {
        return sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }
}
