package com.example.OCRBackEnd.POJO;

import java.beans.JavaBean;

@JavaBean
public class Section {
    private int thesisId;
    private int sectionPage;
    private int sectionId;
    private int sectionRelevance;
    private boolean sectionType;
    private String sectionContent;

    public Section(int thesisId, int sectionPage, int sectionId, boolean sectionType, String sectionContent){
        this.thesisId = thesisId;
        this.sectionPage = sectionPage;
        this.sectionId = sectionId;
        this.sectionType = sectionType;
        this.sectionContent = sectionContent;
    }

    public int getSectionRelevance() {
        return sectionRelevance;
    }

    public void setSectionRelevance(int sectionRelevance) {
        this.sectionRelevance = sectionRelevance;
    }

    public int getThesisId() {
        return thesisId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public int getSectionPage() {
        return sectionPage;
    }

    public void setSectionType(boolean sectionType) {
        this.sectionType = sectionType;
    }

    public String getSectionContent() {
        return sectionContent;
    }

    public void setThesisId(int thesisId) {
        this.thesisId = thesisId;
    }

    public void setSectionContent(String sectionContent) {
        this.sectionContent = sectionContent;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public void setSectionPage(int sectionPage) {
        this.sectionPage = sectionPage;
    }

    public boolean isSectionType() {
        return sectionType;
    }
}
