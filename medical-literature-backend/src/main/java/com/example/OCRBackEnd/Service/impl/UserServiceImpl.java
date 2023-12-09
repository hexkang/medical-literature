package com.example.OCRBackEnd.Service.impl;

import com.example.OCRBackEnd.Mapper.UserMapper;
import com.example.OCRBackEnd.POJO.User;
import com.example.OCRBackEnd.Service.UserService;
import org.springframework.stereotype.Repository;

@Repository
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean CreateUser(User newUser) {
        try{
            return userMapper.CreateUser(newUser);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public User GetUserByNameAndPassword(String userName, String password) {
        try {
            return userMapper.GetUserByNameAndPassword(userName, password);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public User GetUserByName(String userName) {
        try{
            return userMapper.GetUserByName(userName);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean DeleteUserByName(String userName) {
        try{
            return userMapper.DeleteUserByName(userName);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean UpdateUser(User newUser) {
        try{
            return userMapper.UpdateUser(newUser);
        }catch (Exception e){
            return false;
        }
    }
}