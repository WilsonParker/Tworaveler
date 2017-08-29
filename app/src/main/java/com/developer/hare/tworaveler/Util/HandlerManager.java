package com.developer.hare.tworaveler.Util;

import android.os.Handler;

/**
 * Created by Hare on 2017-08-06.
 */

public class HandlerManager {
    private static final HandlerManager ourInstance = new HandlerManager();
    private static final Handler handler = new Handler();

    public static HandlerManager getInstance() {
        return ourInstance;
    }

    public Handler getHandler() {
        return handler;
    }

    public void post(Runnable runnable){
        handler.post(runnable);
    }
}
