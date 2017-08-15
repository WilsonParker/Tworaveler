package com.developer.hare.tworaveler.Util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Hare on 2017-08-14.
 */

public class SizeManager {
    private static SizeManager sizeManager = new SizeManager();

    public static SizeManager getInstance() {
        return sizeManager;
    }

    public int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public int convertDpToSp(float dp, Context context) {
        int sp = (int) (convertDpToPixels(dp, context) / (float) convertSpToPixels(dp, context));
        return sp;
    }
}
