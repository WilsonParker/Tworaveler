package com.developer.hare.tworaveler.Model;

/**
 * Created by Tacademy on 2017-08-22.
 */

public class FollowModel {
    private int user_no, fuser_no;

    public FollowModel(int user_no, int fuser_no) {
        this.user_no = user_no;
        this.fuser_no = fuser_no;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public int getFuser_no() {
        return fuser_no;
    }

    public void setFuser_no(int fuser_no) {
        this.fuser_no = fuser_no;
    }
}
