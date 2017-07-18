package com.example.admin.authfrag;

/**
 * Created by Admin on 2/22/2017.
 */

public class UserModel {

    String name;
    String email;
    String mobilenum;

    public UserModel(){

    }

    public UserModel(String name, String email, String mobilenum) {
        this.name = name;
        this.email = email;
        this.mobilenum = mobilenum;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilenum() {
        return mobilenum;
    }

    public void setMobilenum(String mobilenum) {
        this.mobilenum = mobilenum;
    }
}
