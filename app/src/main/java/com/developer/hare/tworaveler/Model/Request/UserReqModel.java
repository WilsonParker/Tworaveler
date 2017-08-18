package com.developer.hare.tworaveler.Model.Request;

/**
 * Created by Hare on 2017-08-10.
 */

public class UserReqModel {
    private String email, pw, nickname, status_message;
    private int user_no;

    public UserReqModel(String email, String pw) {
        this.email = email;
        this.pw = pw;
    }

    public UserReqModel(String nickname, String status_message, int user_no) {
        this.nickname = nickname;
        this.status_message = status_message;
        this.user_no = user_no;
    }

    public UserReqModel(String email, String pw, String nickname) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }
}
