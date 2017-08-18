package com.developer.hare.tworaveler.Model.Response;

/**
 * Created by Hare on 2017-08-11.
 */

public class SceduleResModel {
    private int user_no;
    private String country, city, start_date, end_date, trip_pic_url, tripName;

    public SceduleResModel(int user_no, String country, String city, String start_date, String end_date, String trip_pic_url, String tripName) {
        this.user_no = user_no;
        this.country = country;
        this.city = city;
        this.start_date = start_date;
        this.end_date = end_date;
        this.trip_pic_url = trip_pic_url;
        this.tripName = tripName;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
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

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    @Override
    public String toString() {
        return "SceduleResModel{" +
                "user_no=" + user_no +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", trip_pic_url='" + trip_pic_url + '\'' +
                ", tripName='" + tripName + '\'' +
                '}';
    }
}
