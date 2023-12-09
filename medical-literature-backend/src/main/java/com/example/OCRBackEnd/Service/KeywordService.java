package com.example.OCRBackEnd.Service;

import com.example.OCRBackEnd.POJO.Keyword;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KeywordService {
    boolean UpdateKeywordByName(String keywordName);
    Keyword GetKeywordByName(String keywordName);
    List<String> GetKeywordsInDescOrder();
}
