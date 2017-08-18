package com.developer.hare.tworaveler.UI;

import com.developer.hare.tworaveler.Data.DataStorage;

/**
 * Created by Hare on 2017-08-17.
 */

public class SessionManager {
    private static SessionManager sessionManager = new SessionManager();

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public boolean isLogin() {
        if (DataStorage.USER_MODEL != null)
            return true;
        else
            return false;
    }
}
