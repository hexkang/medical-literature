package com.example.OCRBackEnd.Service;

import com.example.OCRBackEnd.POJO.Section;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectionService {
    boolean CreateSection(Section newSection);
    boolean DeleteSectionByThesis(int thesisId);
    List<Section> GetSectionsByThesis(int thesisId);
    List<Section> GetSectionsByThesisAndKey(int thesisId, String keyword);
    List<Section> GetSectionsByKey(String keyword);
}
