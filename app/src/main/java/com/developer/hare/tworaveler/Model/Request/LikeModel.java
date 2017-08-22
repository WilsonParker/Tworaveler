package com.developer.hare.tworaveler.Model.Request;

/**
 * Created by Tacademy on 2017-08-22.
 */

public class LikeModel {
    private int user_no, trip_no;

    public LikeModel(int user_no, int trip_no) {
        this.user_no = user_no;
        this.trip_no = trip_no;
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
}
