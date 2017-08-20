package com.developer.hare.tworaveler.Model;

import java.io.Serializable;

/**
 * Created by Tacademy on 2017-08-04.
 */

public class CityModel implements Serializable {

    private int location_no;
    private String city, country, main_pic_url, main_pic_thumbnail_url;
    private String[] etc_pic_url;

    public int getLocation_no() {
        return location_no;
    }

    public void setLocation_no(int location_no) {
        this.location_no = location_no;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMain_pic_url() {
        return main_pic_url;
    }

    public void setMain_pic_url(String main_pic_url) {
        this.main_pic_url = main_pic_url;
    }

    public String getMain_pic_thumbnail_url() {
        return main_pic_thumbnail_url;
    }

    public void setMain_pic_thumbnail_url(String main_pic_thumbnail_url) {
        this.main_pic_thumbnail_url = main_pic_thumbnail_url;
    }

    public String[] getEtc_pic_url() {
        return etc_pic_url;
    }

    public void setEtc_pic_url(String[] etc_pic_url) {
        this.etc_pic_url = etc_pic_url;
    }
}
