package com.example.letswatch;

public class commentModel {
    String date , time , uid , userimage , usermsg , username;

    public commentModel() {
    }

    public commentModel(String date, String time, String uid, String userimage, String usermsg, String username) {
        this.date = date;
        this.time = time;
        this.uid = uid;
        this.userimage = userimage;
        this.usermsg = usermsg;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
