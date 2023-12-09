package com.example.OCRBackEnd.Service.impl;

import com.example.OCRBackEnd.Mapper.EvalMapper;
import com.example.OCRBackEnd.POJO.Eval;
import com.example.OCRBackEnd.Service.EvalService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EvalServiceImpl implements EvalService {
    private final EvalMapper evalService;

    public EvalServiceImpl(EvalMapper evalService) {
        this.evalService = evalService;
    }


    @Override
    public boolean CreateEval(Eval newEval) {
        try{
            return evalService.CreateEval(newEval.getUserName(), newEval.getThesisId(), newEval.geteValue());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean DeleteEvalByNameAndId(String userName, int thesisId) {
        try{
            return evalService.DeleteEvalByNameAndId(userName, thesisId);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Integer> GetLikedIdsByName(String userName) {
        try {
            return evalService.GetLikedIdsByName(userName);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean UpdateEval(Eval newEval) {
        try{
            return evalService.UpdateEval(newEval);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int GetLikeValById(int thesisId) {
        try {
            return evalService.GetLikeValById(thesisId);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public Eval GetEvalByNameAndId(String userName, int thesisId) {
        try {
            return evalService.GetEvalByNameAndId(userName, thesisId);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("GetEvalByNameAndId Fault");
            return null;
        }
    }

    @Override
    public int GetLikedNumById(int thesisId) {
        try {
            return evalService.GetLikedNumById(thesisId);
        }catch (Exception e){
            System.out.println("GetLikedNumById Fault");
            return 1919810;
        }
    }
}
