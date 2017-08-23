package com.example.hp.stepcount.Message;

/**
 * Created by HP on 2017/6/26.
 */

public class User {
    private String USER_ID;
    private String USER_PSW;
    private String USER_NAME;
    private String USER_PHONE;
    private String USER_AGE;
    private String USER_HEIGH;
    private String USER_WEIGHT;

    public void setUid(String USER_ID){
        this.USER_ID = USER_ID;
    }

    public String getUid(){
        return USER_ID;
    }

    public void setUpsw(String USER_PSW){
        this.USER_PSW = USER_PSW;
    }

    public String getUpsw(){
        return USER_PSW;
    }

    public void setUname(String USER_NAME){
        this.USER_NAME = USER_NAME;
    }

    public String getUname(){
        return USER_NAME;
    }

    public void setUphone(String USER_PHONE){
        this.USER_PHONE = USER_PHONE;
    }

    public String getUphone(){
        return USER_PHONE;
    }

    public void setUage(String USER_AGE){
        this.USER_AGE = USER_AGE;
    }

    public String getUage(){
        return USER_AGE;
    }

    public void setUheigh(String USER_HEIGH){
        this.USER_HEIGH = USER_HEIGH;
    }

    public String getUheigh(){
        return USER_HEIGH;
    }

    public void setUweight(String USER_WEIGHT){
        this.USER_WEIGHT = USER_WEIGHT;
    }

    public String getUweight(){
        return USER_WEIGHT;
    }

}
