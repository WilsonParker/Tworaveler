package com.developer.hare.tworaveler.Model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Hare on 2017-08-01.
 */

public class BagModel implements Serializable {
    private String image, theme;
    private File file;
    private boolean isFile;

    public BagModel(String image, String theme) {
        this.image = image;
        this.theme = theme;
        this.isFile = false;
    }

    public BagModel(File file, String theme) {
        this.file = file;
        this.theme = theme;
        this.isFile = true;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
