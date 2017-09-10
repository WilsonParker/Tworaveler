package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

import com.developer.hare.tworaveler.Util.LogManager;

/**
 * Created by Hare on 2017-07-04.
 */

public class UIFactory {
    private static UIFactory uiFactory = new UIFactory();
    private static boolean isActivity = false;
    private static final int BASE_WIDTH = 360, BASE_HEIGHT = 640;
    private static double RAT_DEVISE_WIDTH, RAT_DEVICE_HEIGHT;
    private Activity activity;
    private View view;

    public static UIFactory getInstance(View view) {
        uiFactory.setResource(view);
        isActivity = false;
        return uiFactory;
    }

    public static void init(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width, height;
        RAT_DEVISE_WIDTH = width = size.x;
        RAT_DEVICE_HEIGHT = height = size.y;
        LogManager.log(LogManager.LOG_INFO, UIFactory.class, "init(Activity)", String.format("width : %s, height : %s",width,height));
//        width : 720, height : 1280
    }

    public static UIFactory getInstance(Activity activity) {
        uiFactory.setResource(activity);
        isActivity = true;
        return uiFactory;
    }


    public <E extends View> E createView(int id) {
        E e;
        if (isActivity)
            e = activity.findViewById(id);
        else
            e = view.findViewById(id);
        initView(e);
        return e;
    }

    public void setResource(Activity activity) {
        this.activity = activity;
    }

    public void setResource(View view) {
        this.view = view;
    }

    private View initView(View view) {

        return view;
    }
}
