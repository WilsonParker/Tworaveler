package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-11.
 */

public class SceduleModel {
    // Regist Detail Model
    private int user_no, likes;
    private String country, city, start_date, end_date, trip_pic_url, reg_date, mod_date, del_yn;

    public SceduleModel(int user_no, int likes, String country, String city, String start_date, String end_date, String trip_pic_url, String reg_date, String mod_date, String del_yn) {
        this.user_no = user_no;
        this.likes = likes;
        this.country = country;
        this.city = city;
        this.start_date = start_date;
        this.end_date = end_date;
        this.trip_pic_url = trip_pic_url;
        this.reg_date = reg_date;
        this.mod_date = mod_date;
        this.del_yn = del_yn;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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

    public String getDel_yn() {
        return del_yn;
    }

    public void setDel_yn(String del_yn) {
        this.del_yn = del_yn;
    }

    @Override
    public String toString() {
        return "SceduleModel{" +
                "user_no=" + user_no +
                ", likes=" + likes +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", trip_pic_url='" + trip_pic_url + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", mod_date='" + mod_date + '\'' +
                ", del_yn='" + del_yn + '\'' +
                '}';
    }
}
