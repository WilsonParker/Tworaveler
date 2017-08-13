package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-10.
 */

public class UserModel {
    private int user_no;
    private String email, pw, nickname, profile_pic_url, profile_pic_url_thumbnail, status_message, reg_date, del_yn;

    public UserModel(int user_no, String email, String pw, String nickname, String profile_pic_url, String profile_pic_url_thumbnail, String status_message, String reg_date, String del_yn) {
        this.user_no = user_no;
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.profile_pic_url = profile_pic_url;
        this.profile_pic_url_thumbnail = profile_pic_url_thumbnail;
        this.status_message = status_message;
        this.reg_date = reg_date;
        this.del_yn = del_yn;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
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

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getProfile_pic_url_thumbnail() {
        return profile_pic_url_thumbnail;
    }

    public void setProfile_pic_url_thumbnail(String profile_pic_url_thumbnail) {
        this.profile_pic_url_thumbnail = profile_pic_url_thumbnail;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getDel_yn() {
        return del_yn;
    }

    public void setDel_yn(String del_yn) {
        this.del_yn = del_yn;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "user_no=" + user_no +
                ", email='" + email + '\'' +
                ", pw='" + pw + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profile_pic_url='" + profile_pic_url + '\'' +
                ", profile_pic_url_thumbnail='" + profile_pic_url_thumbnail + '\'' +
                ", status_message='" + status_message + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", del_yn='" + del_yn + '\'' +
                '}';
    }
}