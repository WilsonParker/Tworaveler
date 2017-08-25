package com.developer.hare.tworaveler.Model;

/**
 * Created by Tacademy on 2017-08-23.
 */

public class CommentModel {
    private int comment_no, trip_no;
    private String nickname, content;

    public CommentModel(int trip_no, String nickname, String content) {
        this.trip_no = trip_no;
        this.nickname = nickname;
        this.content = content;
    }

    public int getComment_no() {
        return comment_no;
    }

    public void setComment_no(int comment_no) {
        this.comment_no = comment_no;
    }

    public int getTrip_no() {
        return trip_no;
    }

    public void setTrip_no(int trip_no) {
        this.trip_no = trip_no;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "comment_no=" + comment_no +
                ", trip_no=" + trip_no +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
