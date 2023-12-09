package com.example.OCRBackEnd.Mapper;

import com.example.OCRBackEnd.POJO.Keyword;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface KeywordMapper {
    @Insert("insert into keywords(keywordName) values(#{keywordName})")
    boolean AddKeyword(@Param("keywordName") String keywordName);

    @Update("update keywords set keywordRate = keywordRate + 1 where keywordName = #{keywordName}")
    boolean UpdateKeywordRateByName(@Param("keywordName") String keywordName);

    @Select("select * from keywords where keywordName = #{keywordName}")
    Keyword GetKeywordByName(@Param("keywordName") String keywordName);

    @Select("select keywordName from keywords order by keywordRate desc")
    List<String> GetKeywordsInDescOrder();
}
