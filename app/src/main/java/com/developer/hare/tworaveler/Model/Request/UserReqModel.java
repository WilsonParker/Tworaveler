package com.developer.hare.tworaveler.Model.Request;

/**
 * Created by Hare on 2017-08-10.
 */

public class UserReqModel {
    private String email, pw, nickname, status_message, origin_nickname, modified_nickname;
    private int user_no;

    public UserReqModel(String email, String pw) {
        this.email = email;
        this.pw = pw;
    }

    public UserReqModel(String email, String pw, String nickname) {
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
    }

    public UserReqModel(int user_no, String origin_nickname, String modified_nickname, String status_message) {
        this.origin_nickname = origin_nickname;
        this.modified_nickname = modified_nickname;
        this.status_message = status_message;
        this.user_no = user_no;
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

    public String getOrigin_nickname() {
        return origin_nickname;
    }

    public void setOrigin_nickname(String origin_nickname) {
        this.origin_nickname = origin_nickname;
    }

    public String getModified_nickname() {
        return modified_nickname;
    }

    public void setModified_nickname(String modified_nickname) {
        this.modified_nickname = modified_nickname;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }
}
