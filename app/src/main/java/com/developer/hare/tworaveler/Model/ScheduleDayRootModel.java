package com.developer.hare.tworaveler.Model;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-08-18.
 */

public class ScheduleDayRootModel {
    private String city, country;
    private ArrayList<ScheduleDayModel> DetaliedTrip;

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

    public ArrayList<ScheduleDayModel> getDetaliedTrip() {
        return DetaliedTrip;
    }

    public void setDetaliedTrip(ArrayList<ScheduleDayModel> detaliedTrip) {
        DetaliedTrip = detaliedTrip;
    }


}
