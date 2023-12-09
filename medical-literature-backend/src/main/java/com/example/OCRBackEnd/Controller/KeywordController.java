package com.example.OCRBackEnd.Controller;

import com.example.OCRBackEnd.POJO.Thesis;
import com.example.OCRBackEnd.Service.impl.KeywordServiceImpl;
import com.example.OCRBackEnd.Service.impl.ThesisServiceImpl;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

@RestController
public class KeywordController {
    private final KeywordServiceImpl keywordService;
    private final ThesisServiceImpl thesisService;

    public KeywordController(KeywordServiceImpl keywordService, ThesisServiceImpl thesisService) {
        this.keywordService = keywordService;
        this.thesisService = thesisService;
    }

    @RequestMapping(value = "/api/getRank")
    public String GetRankResponse(){
        try{
            List<String> keywordList = keywordService.GetKeywordsInDescOrder();
            while(keywordList.size() > 10){
                keywordList.remove(keywordList.size() - 1);
            }
            while(keywordList.size() < 10){
                keywordList.add("");
            }
            List<Thesis> thesisList = thesisService.GetThesisByKeyword("");
            List<String> thesisNameList = new ArrayList<>();
            while(thesisList.size() > 10){
                thesisList.remove(thesisList.size() - 1);
            }
            for(Thesis thesis: thesisList){
                thesisNameList.add(thesis.getThesisTitle());
            }
            while(thesisNameList.size() < 10){
                thesisNameList.add("");
            }
            return new JSONObject().put("code", 0).put("data", new JSONObject().put("wordRank", keywordList).put("thesisRank", thesisNameList)).toString();
        } catch (Exception e){
            return new JSONObject().put("code", 403).toString();
        }
    }
}
