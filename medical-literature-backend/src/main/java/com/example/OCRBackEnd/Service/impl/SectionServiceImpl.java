package com.example.OCRBackEnd.Service.impl;

import com.example.OCRBackEnd.Mapper.SectionMapper;
import com.example.OCRBackEnd.POJO.Section;
import com.example.OCRBackEnd.Service.SectionService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SectionServiceImpl implements SectionService{
    private final SectionMapper sectionMapper;

    public SectionServiceImpl(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }

    @Override
    public boolean CreateSection(Section newSection) {
        try{
            return sectionMapper.CreateSection(newSection);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean DeleteSectionByThesis(int thesisId) {
        try{
            return sectionMapper.DeleteSectionByThesis(thesisId);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Section> GetSectionsByThesis(int thesisId) {
        try{
            return sectionMapper.GetSectionsByThesis(thesisId);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Section> GetSectionsByThesisAndKey(int thesisId, String keyword) {
        try{
            return sectionMapper.GetSectionsByThesisAndKey(thesisId, keyword);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Section> GetSectionsByKey(String keyword) {
        try{
            return sectionMapper.GetSectionsByKey(keyword);
        }catch (Exception e){
            return null;
        }
    }
}
