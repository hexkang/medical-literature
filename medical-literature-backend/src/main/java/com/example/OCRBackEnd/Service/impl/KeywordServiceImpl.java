package com.example.OCRBackEnd.Service.impl;

import com.example.OCRBackEnd.Mapper.KeywordMapper;
import com.example.OCRBackEnd.POJO.Keyword;
import com.example.OCRBackEnd.Service.KeywordService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeywordServiceImpl implements KeywordService {
    private final KeywordMapper keywordMapper;

    public KeywordServiceImpl(KeywordMapper keywordMapper) {
        this.keywordMapper = keywordMapper;
    }

    @Override
    public Keyword GetKeywordByName(String keywordName) {
        try{
            return keywordMapper.GetKeywordByName(keywordName);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean UpdateKeywordByName(String keywordName) {
        try{
            Keyword keyword = keywordMapper.GetKeywordByName(keywordName);
            if(keyword == null)
                return keywordMapper.AddKeyword(keywordName);
            else
                return keywordMapper.UpdateKeywordRateByName(keywordName);
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public List<String> GetKeywordsInDescOrder() {
        try{
            return keywordMapper.GetKeywordsInDescOrder();
        }catch (Exception e){
            return null;
        }
    }
}
