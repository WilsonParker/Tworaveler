package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-10.
 */

public class AlamModel {
    private int user_no, file;
    private String nickname, profile_pic_url_thumbnail;
    private boolean isLike, isFollow;

    public AlamModel(int file, String nickname) {
        this.file = file;
        this.nickname = nickname;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_pic_url_thumbnail() {
        return profile_pic_url_thumbnail;
    }

    public void setProfile_pic_url_thumbnail(String profile_pic_url_thumbnail) {
        this.profile_pic_url_thumbnail = profile_pic_url_thumbnail;
    }
}
