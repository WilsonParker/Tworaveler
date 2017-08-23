package com.developer.hare.tworaveler.Model.Request;

/**
 * Created by Hare on 2017-08-10.
 */

public class BagReqModel {
    private int user_no;
    private String category_theme;

    public BagReqModel(int user_no, String category_theme) {
        this.user_no = user_no;
        this.category_theme = category_theme;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public String getCategory_theme() {
        return category_theme;
    }

    public void setCategory_theme(String category_theme) {
        this.category_theme = category_theme;
    }
}
