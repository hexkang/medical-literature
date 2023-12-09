package com.example.OCRBackEnd.POJO;

import java.beans.JavaBean;

@JavaBean
public class Eval {
    private int evalId;
    private String userName;
    private int thesisId;
    private int eValue;

    public Eval(String userName, int thesisId, int eValue){
        this.userName = userName;
        this.thesisId = thesisId;
        this.eValue = eValue;
    }
    public Eval(){}

    public int getEvalId() {
        return evalId;
    }

    public void setEvalId(int evalId) {
        this.evalId = evalId;
    }

    public int getThesisId() {
        return thesisId;
    }

    public String getUserName() {
        return userName;
    }

    public int geteValue() {
        return eValue;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void seteValue(int eValue) {
        this.eValue = eValue;
    }
}
