package com.example.OCRBackEnd.POJO;

import java.beans.JavaBean;
import java.util.List;

@JavaBean
public class Thesis {
    private int thesisId;
    private String fileName;
    private String filePath;
    private String thesisTitle;
    private String thesisAuthors;
    private int thesisEval;
    private int thesisRate;
    private int thesisRelevance;
    private double thesisScore;
    private String thesisExtract;
    private List<Section> thesisSections;
    private String thesisUploader;

    public Thesis(){}

    public int getThesisRelevance() {
        return thesisRelevance;
    }

    public void setThesisRelevance(int thesisRelevance) {
        this.thesisRelevance = thesisRelevance;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getThesisId() {
        return thesisId;
    }

    public int getThesisEval() {
        return thesisEval;
    }

    public int getThesisRate() {
        return thesisRate;
    }

    public List<Section> getThesisSections() {
        return thesisSections;
    }

    public String getThesisAuthors() {
        return thesisAuthors;
    }

    public String getThesisExtract() {
        return thesisExtract;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisAuthors(String thesisAuthors) {
        this.thesisAuthors = thesisAuthors;
    }

    public void setThesisEval(int thesisEval) {
        this.thesisEval = thesisEval;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public void setThesisExtract(String thesisExtract) {
        this.thesisExtract = thesisExtract;
    }

    public void setThesisRate(int thesisRate) {
        this.thesisRate = thesisRate;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public void setThesisSections(List<Section> thesisSections) {
        this.thesisSections = thesisSections;
    }

    public double getThesisScore() {
        return thesisScore;
    }

    public void setThesisScore() {
        this.thesisScore = 0.6 * (double) thesisRelevance + 0.3 * (double) thesisRate + 0.1 * (double) thesisEval;
    }

    public String getThesisUploader() {
        return thesisUploader;
    }

    public void setThesisUploader(String thesisUploader) {
        this.thesisUploader = thesisUploader;
    }
}

