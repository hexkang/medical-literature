package com.example.OCRBackEnd.Service;

import com.example.OCRBackEnd.POJO.Eval;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EvalService {
    boolean CreateEval(Eval newEval);
    boolean DeleteEvalByNameAndId(String userName, int thesisId);
    int GetLikeValById(int thesisId);
    List<Integer> GetLikedIdsByName(String userName);
    boolean UpdateEval(Eval newEval);
    Eval GetEvalByNameAndId(String userName, int thesisId);
    int GetLikedNumById(int thesisId);
}
