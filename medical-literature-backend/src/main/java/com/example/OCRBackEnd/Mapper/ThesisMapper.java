package com.example.OCRBackEnd.Mapper;

import com.example.OCRBackEnd.POJO.Thesis;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ThesisMapper {

    @Insert("insert into thesis(fileName, thesisTitle, filePath, thesisAuthors, thesisExtract, thesisUploader) values(#{fileName}, #{thesisTitle}, #{filePath}, #{thesisAuthors}, #{thesisExtract}, #{thesisUploader})")
    boolean CreateThesis(@Param("fileName")String fileName, @Param("thesisTitle")String thesisTitle, @Param("filePath")String filePath, @Param("thesisAuthors")String thesisAuthors, @Param("thesisExtract")String thesisExtract, @Param("thesisUploader")String thesisUploader);

    @Delete("delete from thesis where thesisId = #{thesisId}")
    boolean DeleteThesisById(@Param("thesisId")int thesisId);

    @Select("select thesisId from thesis where fileName = #{fileName}")
    int GetThesisIdByFileName(@Param("fileName")String fileName);

    @Select("select * from thesis")
    List<Thesis> GetAllThesis();

    @Select("select * from thesis where thesisId = #{thesisId}")
    Thesis GetThesisById(@Param("thesisId")int thesisId);

    @Select("select * from thesis where thesisUploader = #{thesisUploader}")
    List<Thesis> GetThesisByUploader(@Param("thesisUploader") String thesisUploader);

    @Update("update thesis set fileName = #{newThesis.fileName}, filePath = #{newThesis.filePath}, thesisTitle = #{newThesis.thesisTitle}, thesisRate = #{newThesis.thesisRate}, thesisExtract = #{newThesis.thesisExtract} where thesisId = #{newThesis.thesisId}")
    boolean UpdateThesis(@Param("newThesis")Thesis newThesis);
}
