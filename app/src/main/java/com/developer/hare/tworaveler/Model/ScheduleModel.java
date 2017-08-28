package com.developer.hare.tworaveler.Model;

import java.io.Serializable;

/**
 * Created by Hare on 2017-08-11.
 */

public class ScheduleModel implements Serializable {
    // Regist Detail Model
    private int user_no, trip_no, likeCount, CommentCount;
    private String nickname, status_message, country, city, start_date, end_date, profile_pic_thumbnail, trip_pic_url, reg_date, mod_date, tripName;
    private boolean isLike;

    public ScheduleModel(int user_no, String nickname, String status_message, String country, String city, String start_date, String end_date, String tripName) {
        this.user_no = user_no;
        this.nickname = nickname;
        this.status_message = status_message;
        this.country = country;
        this.city = city;
        this.start_date = start_date;
        this.end_date = end_date;
        this.tripName = tripName;
    }

    public ScheduleModel(UserModel userModel, String country, String city, String start_date, String end_date, String tripName) {
        this.user_no = userModel.getUser_no();
        this.nickname = userModel.getNickname();
        this.status_message = userModel.getStatus_message();
        this.country = country;
        this.city = city;
        this.start_date = start_date;
        this.end_date = end_date;
        this.tripName = tripName;
    }

    public ScheduleModel(UserModel userModel, CityModel cityModel, String start_date, String end_date, String tripName) {
        this.user_no = userModel.getUser_no();
        this.nickname = userModel.getNickname();
        this.status_message = userModel.getStatus_message();
        this.country = cityModel.getCountry();
        this.city = cityModel.getCity();
        this.start_date = start_date;
        this.end_date = end_date;
        this.tripName = tripName;
    }


    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public int getTrip_no() {
        return trip_no;
    }

    public void setTrip_no(int trip_no) {
        this.trip_no = trip_no;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProfile_pic_thumbnail() {
        return profile_pic_thumbnail;
    }

    public void setProfile_pic_thumbnail(String profile_pic_thumbnail) {
        this.profile_pic_thumbnail = profile_pic_thumbnail;
    }

    public String getTrip_pic_url() {
        return trip_pic_url;
    }

    public void setTrip_pic_url(String trip_pic_url) {
        this.trip_pic_url = trip_pic_url;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getMod_date() {
        return mod_date;
    }

    public void setMod_date(String mod_date) {
        this.mod_date = mod_date;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    @Override
    public String toString() {
        return "ScheduleModel{" +
                "user_no=" + user_no +
                ", trip_no=" + trip_no +
                ", likeCount=" + likeCount +
                ", CommentCount=" + CommentCount +
                ", nickname='" + nickname + '\'' +
                ", status_message='" + status_message + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", profile_pic_thumbnail='" + profile_pic_thumbnail + '\'' +
                ", trip_pic_url='" + trip_pic_url + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", mod_date='" + mod_date + '\'' +
                ", tripName='" + tripName + '\'' +
                ", isLike=" + isLike +
                '}';
    }
}
