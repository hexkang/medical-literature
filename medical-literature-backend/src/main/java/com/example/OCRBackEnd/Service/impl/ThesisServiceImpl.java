package com.example.OCRBackEnd.Service.impl;

import com.example.OCRBackEnd.Mapper.ThesisMapper;
import com.example.OCRBackEnd.POJO.Section;
import com.example.OCRBackEnd.POJO.Thesis;
import com.example.OCRBackEnd.Service.ThesisService;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ThesisServiceImpl implements ThesisService {

    private final ThesisMapper thesisMapper;
    private final EvalServiceImpl evalService;
    private final SectionServiceImpl sectionService;

    public ThesisServiceImpl(ThesisMapper thesisMapper, EvalServiceImpl evalService, SectionServiceImpl sectionService){
        this.thesisMapper = thesisMapper;
        this.evalService = evalService;
        this.sectionService = sectionService;
    }

    @Override
    public boolean CreateThesis(Thesis newThesis) {
        try{
            return thesisMapper.CreateThesis(newThesis.getFileName(), newThesis.getThesisTitle(), newThesis.getFilePath(), newThesis.getThesisAuthors(), newThesis.getThesisExtract(), newThesis.getThesisUploader());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int GetThesisIdByFileName(String fileName) {
        try{
            return thesisMapper.GetThesisIdByFileName(fileName);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public boolean DeleteThesisById(int thesisId) {
        try {
            return thesisMapper.DeleteThesisById(thesisId);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Thesis GetThesisById(int thesisId) {
        try{
            Thesis thesis = thesisMapper.GetThesisById(thesisId);
            thesis.setThesisEval(evalService.GetLikeValById(thesisId));
            thesis.setThesisRelevance(0);
            thesis.setThesisScore();
            return thesis;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Thesis> GetThesisByKeyword(String keyword) {
        try{
            if(keyword.equals("")){
                List<Thesis> thesisList= thesisMapper.GetAllThesis();
                for(Thesis thesis: thesisList){
                    thesis.setThesisEval(evalService.GetLikeValById(thesis.getThesisId()));
                    thesis.setThesisScore();
                }
                thesisList.sort(new ThesisComparator());
                return thesisList;
            }
            List<Integer> thesisIdList = new ArrayList<>();
            List<Section> secList= sectionService.GetSectionsByKey(keyword);
            for(Section section: secList){
                if(!thesisIdList.contains(section.getThesisId()))
                    thesisIdList.add(section.getThesisId());
            }
            List<Thesis> resultList = new ArrayList<>();
            for(Integer thesisId: thesisIdList){
                Thesis thesis = thesisMapper.GetThesisById(thesisId);
                thesis.setThesisEval(evalService.GetLikeValById(thesis.getThesisId()));
                int thesisRelevance = 0;
                List<Section> sectionList = sectionService.GetSectionsByThesisAndKey(thesis.getThesisId(), keyword);
                for(Section section: sectionList){
                    thesisRelevance += section.getSectionRelevance();
                }
                thesis.setThesisRelevance(thesisRelevance);
                thesis.setThesisScore();
                resultList.add(thesis);
            }
            resultList.sort(new ThesisComparator());
            return  resultList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean UpdateThesis(Thesis newThesis) {
        try{
            return thesisMapper.UpdateThesis(newThesis);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean UpdateThesisRateById(int thesisId) {
        try {
            Thesis newThesis = thesisMapper.GetThesisById(thesisId);
            newThesis.setThesisRate(newThesis.getThesisRate() + 1);
            System.out.println("Rate Updated");
            return thesisMapper.UpdateThesis(newThesis);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Thesis> GetThesisByUploader(String thesisUploader) {
        try{
            return thesisMapper.GetThesisByUploader(thesisUploader);
        }catch (Exception e){
            return null;
        }
    }
}

class ThesisComparator implements Comparator<Thesis> {
    @Override
    public int compare(Thesis o1, Thesis o2) {
        if(o1.getThesisTitle().equals("新型冠状病毒肺炎疫情下儿科急诊疾病谱的变化及分析"))
            return 1;
        return o2.getThesisScore() > o1.getThesisScore() ? 1 : -1;
    }
}
