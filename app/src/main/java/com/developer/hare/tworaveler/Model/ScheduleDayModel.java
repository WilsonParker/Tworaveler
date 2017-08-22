package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-11.
 */

public class ScheduleDayModel {
    // Regist Day Detail Model
    private int dtrip_no, trip_no, likeCount, CommentCount;
    private String dPicture_url, startTime, endTime, memo, address, trip_date;

    public int getDtrip_no() {
        return dtrip_no;
    }

    public void setDtrip_no(int dtrip_no) {
        this.dtrip_no = dtrip_no;
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

    public String getdPicture_url() {
        return dPicture_url;
    }

    public void setdPicture_url(String dPicture_url) {
        this.dPicture_url = dPicture_url;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(String trip_date) {
        this.trip_date = trip_date;
    }
}
