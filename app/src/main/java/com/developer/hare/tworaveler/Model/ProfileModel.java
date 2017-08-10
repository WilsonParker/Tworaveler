package com.developer.hare.tworaveler.Model;

/**
 * Created by Tacademy on 2017-08-08.
 */

public class ProfileModel {
    String profileImage;

    public ProfileModel(String profileImage) {
        this.profileImage = profileImage;
    }

    public ProfileModel() {
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
