package com.aniket.healthcare;

public class User {
    String userId, mob, emr1, emr2, emr3, mail;

    public User(){};

    public User(String userId,String mob,String emr1,String emr2,String emr3,String mail){
        this.userId = userId;
        this.mob = mob;
        this.emr1 = emr1;
        this.emr2 = emr2;
        this.emr3 = emr3;
        this.mail = mail;
    }

    public String getUserId(){
        return userId;
    }

    public String getMob(){
        return mob;
    }

    public String getemr1(){
        return emr1;
    }

    public String getEmr2(){
        return emr2;
    }

    public String getEmr3(){
        return emr3;
    }

    public String getMail(){
        return mail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public void setEmr1(String emr1) {
        this.emr1 = emr1;
    }

    public void setEmr2(String emr2) {
        this.emr2 = emr2;
    }

    public void setEmr3(String emr3) {
        this.emr3 = emr3;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
