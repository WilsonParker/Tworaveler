package com.developer.hare.tworaveler.Model;

/**
 * Created by Hare on 2017-08-01.
 */

public class PeedItemModel {
    private String id, message, startDate, endDate, profileUri, coverUri;

    public PeedItemModel(String id, String message, String startDate, String endDate, String profileUri, String coverUri) {
        this.id = id;
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
        this.profileUri = profileUri;
        this.coverUri = coverUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getDate() {
        return this.startDate + " ~ " + this.endDate;
    }

    @Override
    public String toString() {
        return "PeedItemModel{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", profileUri='" + profileUri + '\'' +
                ", coverUri='" + coverUri + '\'' +
                '}';
    }
}
