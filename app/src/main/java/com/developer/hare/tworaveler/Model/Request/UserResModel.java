package com.developer.hare.tworaveler.Model.Request;

/**
 * Created by Hare on 2017-08-10.
 */

public class UserResModel {
    private String email, pw;

    public UserResModel(String email, String pw) {
        this.email = email;
        this.pw = pw;
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
}
