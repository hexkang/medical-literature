package com.example.OCRBackEnd.POJO;

import java.beans.JavaBean;

@JavaBean
public class Keyword {
    private int keywordId;
    private String keywordName;
    private int keywordRate;

    public int getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(int keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public int getKeywordRate() {
        return keywordRate;
    }

    public void setKeywordRate(int keywordRate) {
        this.keywordRate = keywordRate;
    }
}
