package com.developer.hare.tworaveler.Model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Hare on 2017-08-01.
 */

public class BagModel implements Serializable {
    private String _id, category_theme, category_pic_url, category_pic_thumbnail_url, mod_date, reg_date;
    private int item_no, user_no;
    private File file;
    private boolean isFile;

    public BagModel(int user_no, File file, String category_theme) {
        this.user_no = user_no;
        this.file = file;
        this.category_theme= category_theme;
        isFile = true;
    }

    public BagModel(String _id, String category_pic_url, String category_pic_thumbnail_url) {
        this._id = _id;
        this.category_pic_url = category_pic_url;
        this.category_pic_thumbnail_url = category_pic_thumbnail_url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategory_theme() {
        return category_theme;
    }

    public void setCategory_theme(String category_theme) {
        this.category_theme = category_theme;
    }

    public String getCategory_pic_url() {
        return category_pic_url;
    }

    public void setCategory_pic_url(String category_pic_url) {
        this.category_pic_url = category_pic_url;
    }

    public String getCategory_pic_thumbnail_url() {
        return category_pic_thumbnail_url;
    }

    public void setCategory_pic_thumbnail_url(String category_pic_thumbnail_url) {
        this.category_pic_thumbnail_url = category_pic_thumbnail_url;
    }

    public String getMod_date() {
        return mod_date;
    }

    public void setMod_date(String mod_date) {
        this.mod_date = mod_date;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public int getItem_no() {
        return item_no;
    }

    public void setItem_no(int item_no) {
        this.item_no = item_no;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
