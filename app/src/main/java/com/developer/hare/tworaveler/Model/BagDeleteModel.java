package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-01.
 */

public class BagDeleteModel {
    private String image, theme, deleteId;
    private boolean isChecked;

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public BagDeleteModel(String image, String theme) {
        this.image = image;
        this.theme = theme;
    }

    public BagDeleteModel(String image, String theme, String deleteId) {
        this.image = image;
        this.theme = theme;
        this.deleteId = deleteId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
