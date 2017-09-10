package com.developer.hare.tworaveler.Util.Parser;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by Hare on 2017-08-14.
 */

public class SizeManager {
    private static SizeManager sizeManager = new SizeManager();

    private static DisplayMetrics displayMetrics;
    public static SizeManager getInstance() {
        return sizeManager;
    }

    public static void init(Context context){
        displayMetrics = context.getResources().getDisplayMetrics();
    }
    public int convertDpToPixels(float dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return px;
    }

    public int convertSpToPixels(float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics);
        return px;
    }

    public int convertDpToSp(float dp) {
        int sp = (int) (convertDpToPixels(dp) / (float) convertSpToPixels(dp));
        return sp;
    }
}
