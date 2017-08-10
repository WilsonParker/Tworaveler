package com.developer.hare.tworaveler.Model.Request;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-08-04.
 */

public class RequestArrayModel<T> {
    private int success;
    private String message;
    private ArrayList<T> result;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<T> getResult() {
        return result;
    }

    public void setResult(ArrayList<T> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
