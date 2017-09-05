package com.developer.hare.tworaveler.Model;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-08-23.
 */

public class CommentModel {
    private int comment_no, trip_no, __v, user_no, dtrip_no;
    private String nickname, content, _id, del_yn, mod_date, reg_date, email, status_message, profile_pic_thumbnail_url, profile_pic_url, trip_date;
    private ArrayList<Integer> followees, followers;

    public CommentModel(int trip_no, int user_no, int dtrip_no, String nickname, String content) {
        this.user_no = user_no;
        this.trip_no = trip_no;
        this.dtrip_no = dtrip_no;
        this.nickname = nickname;
        this.content = content;
    }
    public CommentModel(int trip_no, int user_no, String nickname, String content) {
        this.user_no = user_no;
        this.trip_no = trip_no;
        this.nickname = nickname;
        this.content = content;
    }

    public int getComment_no() {
        return comment_no;
    }

    public void setComment_no(int comment_no) {
        this.comment_no = comment_no;
    }

    public int getTrip_no() {
        return trip_no;
    }

    public void setTrip_no(int trip_no) {
        this.trip_no = trip_no;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public int getDtrip_no() {
        return dtrip_no;
    }

    public void setDtrip_no(int dtrip_no) {
        this.dtrip_no = dtrip_no;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDel_yn() {
        return del_yn;
    }

    public void setDel_yn(String del_yn) {
        this.del_yn = del_yn;
    }

    public String getMod_date() {
        return mod_date;
    }

    public void setMod_date(String mod_date) {
        this.mod_date = mod_date;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getProfile_pic_thumbnail_url() {
        return profile_pic_thumbnail_url;
    }

    public void setProfile_pic_thumbnail_url(String profile_pic_thumbnail_url) {
        this.profile_pic_thumbnail_url = profile_pic_thumbnail_url;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public ArrayList<Integer> getFollowees() {
        return followees;
    }

    public void setFollowees(ArrayList<Integer> followees) {
        this.followees = followees;
    }

    public ArrayList<Integer> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Integer> followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "comment_no=" + comment_no +
                ", trip_no=" + trip_no +
                ", __v=" + __v +
                ", user_no=" + user_no +
                ", dtrip_no=" + dtrip_no +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", _id='" + _id + '\'' +
                ", del_yn='" + del_yn + '\'' +
                ", mod_date='" + mod_date + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", email='" + email + '\'' +
                ", status_message='" + status_message + '\'' +
                ", profile_pic_thumbnail_url='" + profile_pic_thumbnail_url + '\'' +
                ", profile_pic_url='" + profile_pic_url + '\'' +
                ", trip_date='" + trip_date + '\'' +
                ", followees=" + followees +
                ", followers=" + followers +
                '}';
    }
}
