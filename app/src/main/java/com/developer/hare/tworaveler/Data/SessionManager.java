package com.developer.hare.tworaveler.Data;

import com.developer.hare.tworaveler.Model.UserModel;

/**
 * Created by Hare on 2017-08-17.
 */

public class SessionManager {
    private UserModel USER_MODEL;
    private static SessionManager sessionManager = new SessionManager();

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public boolean isLogin() {
        if (USER_MODEL != null)
            return true;
        else
            return false;
    }

    public UserModel getUserModel() {
        return this.USER_MODEL;
    }

    public void setUserModel(UserModel userModel) {
        this.USER_MODEL = userModel;
    }
}
