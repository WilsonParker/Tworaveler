package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-11.
 */

public class ScheduleDayModel {
    // Regist Day Detail Model
    private String imangeUrl, city, startTime, endTime, memo;

    public ScheduleDayModel(String imangeUrl, String city, String startTime, String endTime, String memo) {
        this.imangeUrl = imangeUrl;
        this.city = city;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
    }

    public String getImangeUrl() {
        return imangeUrl;
    }

    public void setImangeUrl(String imangeUrl) {
        this.imangeUrl = imangeUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
