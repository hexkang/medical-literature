package com.example.OCRBackEnd.Service;

import com.example.OCRBackEnd.POJO.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

//也是没有写对应的注解的，到时候可以改一下
@Service
public interface UserService {
    //和mapper相呼应，有一个对应的方法
    User GetUserByNameAndPassword(String userName, String password);
    boolean CreateUser(User newUser);
    boolean DeleteUserByName(String userName);
    User GetUserByName(String userName);
    boolean UpdateUser(User newUser);
}