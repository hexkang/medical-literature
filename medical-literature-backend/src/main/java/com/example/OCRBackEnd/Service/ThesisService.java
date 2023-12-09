package com.example.OCRBackEnd.Service;

import com.example.OCRBackEnd.POJO.Thesis;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThesisService {
    boolean CreateThesis(Thesis newThesis);
    boolean DeleteThesisById(int thesisId);
    Thesis GetThesisById(int thesisId);
    List<Thesis> GetThesisByKeyword(String keyword);
    boolean UpdateThesis(Thesis newThesis);
    boolean UpdateThesisRateById(int thesisId);
    int GetThesisIdByFileName(String fileName);
    List<Thesis> GetThesisByUploader(String thesisUploader);
}
