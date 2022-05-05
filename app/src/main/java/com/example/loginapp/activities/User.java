package com.example.loginapp.activities;

import java.security.PublicKey;

public class User {
    public String fullName,age,email,userType="user";
    public User(){

    }

    public User(String fullName, String age, String email, String userType){
        this.fullName=fullName;
        this.age=age;
        this.email=email;
        this.userType=userType;
    }
}
