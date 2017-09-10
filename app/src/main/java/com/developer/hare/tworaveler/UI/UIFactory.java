package com.developer.hare.tworaveler.UI;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Util.Parser.SizeManager;

/**
 * Created by Hare on 2017-07-04.
 */

public class UIFactory {
    public static final int TYPE_BASIC = 1, TYPE_MARGIN = 2, TYPE_BASIC_MARGIN = 3, TYPE_RADIUS = 4;

    private static UIFactory uiFactory = new UIFactory();
    private static boolean isActivity = false;
    private static final int BASE_DIGIT = 3, BASE_WIDTH = 360, BASE_HEIGHT = 640;
    private static double DIGIT, RAT_DEVISE_WIDTH, RAT_DEVICE_HEIGHT;
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
        DIGIT = Math.pow(10, BASE_DIGIT);
        RAT_DEVISE_WIDTH = Math.round(((double) size.x / BASE_WIDTH) * DIGIT) / DIGIT;
        RAT_DEVICE_HEIGHT = Math.round(((double) size.y / BASE_HEIGHT) * DIGIT) / DIGIT;
    }

    private int rounds(double d, double rat) {
        return (int) Math.round(Math.round((d * rat) * DIGIT) / DIGIT);
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
        return e;
    }

    public <E extends View> E createViewWithRateParams(int id) {
        return createViewWithRateParams(id, TYPE_BASIC_MARGIN);
    }

    public <E extends View> E createViewWithRateParams(int id, int type) {
        E e = createView(id);
        ViewGroup.MarginLayoutParams mLayoutParams = (ViewGroup.MarginLayoutParams) e.getLayoutParams();
        if ((type & TYPE_BASIC) != 0) {
            int width = mLayoutParams.width, height = mLayoutParams.height;
            if (width != ViewGroup.LayoutParams.MATCH_PARENT && width != ViewGroup.LayoutParams.WRAP_CONTENT)
                mLayoutParams.width = rounds(mLayoutParams.width, RAT_DEVISE_WIDTH);
            if (height != ViewGroup.LayoutParams.MATCH_PARENT && height != ViewGroup.LayoutParams.WRAP_CONTENT)
                mLayoutParams.height = rounds(mLayoutParams.height, RAT_DEVICE_HEIGHT);
        }

        if ((type & TYPE_MARGIN) != 0) {
            mLayoutParams.topMargin = rounds(mLayoutParams.topMargin, RAT_DEVICE_HEIGHT);
            mLayoutParams.bottomMargin = rounds(mLayoutParams.bottomMargin, RAT_DEVICE_HEIGHT);
            mLayoutParams.leftMargin = rounds(mLayoutParams.leftMargin, RAT_DEVISE_WIDTH);
            mLayoutParams.rightMargin = rounds(mLayoutParams.rightMargin, RAT_DEVISE_WIDTH);
        }

        if ((type & TYPE_RADIUS) != 0) {
            GradientDrawable gradientDrawable = (GradientDrawable) e.getBackground();
            gradientDrawable.setCornerRadius(rounds(SizeManager.getInstance().convertDpToPixels(13), RAT_DEVICE_HEIGHT));
        }
        e.setLayoutParams(mLayoutParams);
        return e;
    }

    public void setResource(Activity activity) {
        this.activity = activity;
    }

    public void setResource(View view) {
        this.view = view;
    }

}
