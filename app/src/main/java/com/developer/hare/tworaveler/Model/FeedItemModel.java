package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-01.
 */

public class FeedItemModel {
    private String nickname, status_message, profile_pic_thumbnail, country, city, tripName, trip_pic_url, start_date, end_date;
    private int trip_no, user_no, likeCount, CommentCount;
    private boolean isLike;

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

    public String getProfile_pic_thumbnail() {
        return profile_pic_thumbnail;
    }

    public void setProfile_pic_thumbnail(String profile_pic_thumbnail) {
        this.profile_pic_thumbnail = profile_pic_thumbnail;
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

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTrip_pic_url() {
        return trip_pic_url;
    }

    public void setTrip_pic_url(String trip_pic_url) {
        this.trip_pic_url = trip_pic_url;
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

    public int getTrip_no() {
        return trip_no;
    }

    public void setTrip_no(int trip_no) {
        this.trip_no = trip_no;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
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

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
