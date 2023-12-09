package com.example.OCRBackEnd.Mapper;

import com.example.OCRBackEnd.POJO.Section;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SectionMapper {
    @Insert("insert into sections(thesisId, sectionPage, sectionId, sectionType, sectionContent) values(#{newSection.thesisId}, #{newSection.sectionPage}, #{newSection.sectionId}, #{newSection.sectionType}, #{newSection.sectionContent})")
    boolean CreateSection(@Param("newSection") Section newSection);

    @Delete("delete from sections where thesisId = #{thesisId}")
    boolean DeleteSectionByThesis(@Param("thesisId")int thesisId);

    @Select("select * from sections where thesisId = #{thesisId}")
    List<Section> GetSectionsByThesis(@Param("thesisId")int thesisId);

    @Select("select *, (length(sectionContent) - length(replace(sectionContent, #{keyword}, \"\"))) / length(#{keyword}) as \"sectionRelevance\" from sections where thesisId = #{thesisId} and locate(#{keyword}, sectionContent) > 0")
    List<Section> GetSectionsByThesisAndKey(@Param("thesisId")int thesisId, @Param("keyword")String keyword);

    @Select("select * from sections where locate(#{keyword}, sectionContent) > 0")
    List<Section> GetSectionsByKey(@Param("keyword") String keyword);
}
