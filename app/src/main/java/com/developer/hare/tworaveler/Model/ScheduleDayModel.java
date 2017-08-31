package com.developer.hare.tworaveler.Model;

import java.io.Serializable;

/**
 * Created by Hare on 2017-08-11.
 */

public class ScheduleDayModel implements Serializable {
    // Regist Day Detail Model
    private int dtrip_no, trip_no, latitude, longitude, likeCount, CommentCount;
    private String dtrip_pic_url, start_time, end_time, memo, address, trip_date, trip_address;

    public ScheduleDayModel(int trip_no, int latitude, int longitude, String start_time, String end_time, String memo, String address, String trip_address, String trip_date) {
        this.trip_no = trip_no;
        this.latitude = latitude;
        this.longitude = longitude;
        this.start_time = start_time;
        this.end_time = end_time;
        this.memo = memo;
        this.address = address;
        this.trip_address = trip_address;
        this.trip_date = trip_date;
    }

    public ScheduleDayModel(ScheduleModel scheduleModel, int latitude, int longitude, String start_time, String end_time, String memo, String address, String trip_date) {
        this.trip_no = scheduleModel.getTrip_no();
        this.trip_address = scheduleModel.getCountry() + "\b" + scheduleModel.getCity();
        this.latitude = latitude;
        this.longitude = longitude;
        this.start_time = start_time;
        this.end_time = end_time;
        this.memo = memo;
        this.address = address;
        this.trip_date = trip_date;
    }

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

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
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

    public String getDtrip_pic_url() {
        return dtrip_pic_url;
    }

    public void setDtrip_pic_url(String dtrip_pic_url) {
        this.dtrip_pic_url = dtrip_pic_url;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    public String getTrip_address() {
        return trip_address;
    }

    public void setTrip_address(String trip_address) {
        this.trip_address = trip_address;
    }

    @Override
    public String toString() {
        return "ScheduleDayModel{" +
                "dtrip_no=" + dtrip_no +
                ", trip_no=" + trip_no +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", likeCount=" + likeCount +
                ", CommentCount=" + CommentCount +
                ", dtrip_pic_url='" + dtrip_pic_url + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", memo='" + memo + '\'' +
                ", address='" + address + '\'' +
                ", trip_date='" + trip_date + '\'' +
                ", trip_address='" + trip_address + '\'' +
                '}';
    }
}