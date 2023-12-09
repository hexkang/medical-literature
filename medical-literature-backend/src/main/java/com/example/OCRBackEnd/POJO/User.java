package com.example.OCRBackEnd.POJO;

import java.beans.JavaBean;
import java.util.List;

@JavaBean
public class User {
    private int userId;
    private String userName;
    private String password;
    private boolean isAdmin;
    private String userEmail;
    private List<Integer> likedThesisIds;

    public User(String userName, String password, String userEmail){
        this.userName = userName;
        this.password = password;
        this.userEmail = userEmail;
    }
    public User(){

    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getLikedThesisIds() {
        return likedThesisIds;
    }

    public void setLikedThesisIds(List<Integer> likedThesisIds) {
        this.likedThesisIds = likedThesisIds;
    }
}
