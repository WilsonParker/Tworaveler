package com.developer.hare.tworaveler.Model.Request;

/**
 * Created by Hare on 2017-08-01.
 */

public class UserSignUpModel {
    private String email, pw, nickname;

    public UserSignUpModel(String email, String pw, String nickname) {
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
}
