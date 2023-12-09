package com.example.OCRBackEnd.Mapper;

import com.example.OCRBackEnd.POJO.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {

    @Select("select * from users where userName = #{userName} and password = #{password}")
    User GetUserByNameAndPassword(@Param("userName")String userName, @Param("password")String password);

    @Insert("insert into users(userName, password, userEmail) values(#{newUser.userName}, #{newUser.password}, #{newUser.userEmail})")
    boolean CreateUser(@Param("newUser")User newUser);

    @Delete("delete from users where userName = #{userName}")
    boolean DeleteUserByName(@Param("userName")String userName);

    @Select("select * from users where userName = #{userName}")
    User GetUserByName(@Param("userName")String userName);

    @Update("update users set password = #{newUser.password}, isAdmin = #{newUser.isAdmin}, userEmail = #{newUser.userEmail} where userName = #{newUser.userName}")
    boolean UpdateUser(@Param("newUser")User newUser);
}
