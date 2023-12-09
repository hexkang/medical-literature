package com.example.OCRBackEnd.Mapper;

import com.example.OCRBackEnd.POJO.Eval;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EvalMapper {
    @Insert("insert into evals(userName, thesisId, eValue) values(#{userName}, #{thesisId}, #{eValue})")
    boolean CreateEval(@Param("userName")String userName, @Param("thesisId")int thesisId, @Param("eValue")int eValue);

    @Delete("delete from evals where userName = #{userName} and thesisId = #{thesisId}")
    boolean DeleteEvalByNameAndId(@Param("userName")String userName, @Param("thesisId")int thesisId);

    @Select("select thesisId from evals where userName = #{userName} and eValue > 0")
    List<Integer> GetLikedIdsByName(@Param("userName")String userName);

    @Select("select * from evals where userName = #{userName} and thesisId = #{thesisId}")
    Eval GetEvalByNameAndId(@Param("userName")String userName, @Param("thesisId")int thesisId);

    @Select("select count(*) from evals where thesisId = #{thesisId} and eValue > 0")
    int GetLikedNumById(@Param("thesisId")int thesisId);

    @Select("select sum(eValue) from evals where thesisId = #{thesisId}")
    int GetLikeValById(@Param("thesisId")int thesisId);

    @Update("update evals set userName = #{newEval.userName}, thesisId = #{newEval.thesisId}, eValue = #{newEval.eValue} where evalId = #{newEval.evalId}")
    boolean UpdateEval(@Param("newEval")Eval newEval);
}
