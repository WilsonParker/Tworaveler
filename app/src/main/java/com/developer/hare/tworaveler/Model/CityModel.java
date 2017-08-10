package com.developer.hare.tworaveler.Model;

import java.io.Serializable;

/**
 * Created by Tacademy on 2017-08-04.
 */

public class CityModel implements Serializable {
    /*
     * 가  나  다 ----
     *         라 -----
     *     마  바 -----
     *         사 -----
     */

    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
