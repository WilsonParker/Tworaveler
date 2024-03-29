package com.developer.hare.tworaveler.Model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-10.
 */

public class UserModel implements Serializable {
    public static final int TYPE_TWORAVELER = 1, TYPE_KAKAO = 2, TYPE_FACEBOOK = 3;
    private int user_no, type;
    private String email, pw, nickname, profile_pic_url, profile_pic_thumbnail_url, status_message, reg_date, del_yn, sessionID, Cookie;
    private ArrayList<Integer> followees, followers;
    private File file;
    private boolean isFile;

    public UserModel(int user_no, String email, String pw, String nickname, String profile_pic_url, String profile_pic_thumbnail_url, String status_message, String reg_date, String del_yn, String sessionID, ArrayList<Integer> followees, ArrayList<Integer> followers) {
        this.user_no = user_no;
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.profile_pic_url = profile_pic_url;
        this.profile_pic_thumbnail_url = profile_pic_thumbnail_url;
        this.status_message = status_message;
        this.reg_date = reg_date;
        this.del_yn = del_yn;
        this.sessionID = sessionID;
        this.followees = followees;
        this.followers = followers;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
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

    public String getProfile_pic_thumbnail_url() {
        return profile_pic_thumbnail_url;
    }

    public void setProfile_pic_thumbnail_url(String profile_pic_thumbnail_url) {
        this.profile_pic_thumbnail_url = profile_pic_thumbnail_url;
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

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getCookie() {
        return Cookie;
    }

    public void setCookie(String cookie) {
        this.Cookie = cookie;
    }

    public ArrayList<Integer> getFollowees() {
        if (followees == null)
            followees = new ArrayList<>();
        return followees;
    }

    public void setFollowees(ArrayList<Integer> followees) {
        this.followees = followees;
    }

    public ArrayList<Integer> getFollowers() {
        if (followers == null)
            followers = new ArrayList<>();
        return followers;
    }

    public void setFollowers(ArrayList<Integer> followers) {
        this.followers = followers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "user_no=" + user_no +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", pw='" + pw + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profile_pic_url='" + profile_pic_url + '\'' +
                ", profile_pic_thumbnail_url='" + profile_pic_thumbnail_url + '\'' +
                ", status_message='" + status_message + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", del_yn='" + del_yn + '\'' +
                ", sessionID='" + sessionID + '\'' +
                ", Cookie='" + Cookie + '\'' +
                ", followees=" + followees +
                ", followers=" + followers +
                ", file=" + file +
                ", isFile=" + isFile +
                '}';
    }
}
