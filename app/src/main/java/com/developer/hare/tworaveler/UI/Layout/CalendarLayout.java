package com.developer.hare.tworaveler.UI.Layout;

import android.content.Context;
import android.util.AttributeSet;

import com.squareup.timessquare.CalendarPickerView;

public class CalendarLayout extends CalendarPickerView {
    private int headerTextColor;

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        headerTextColor = 1;
    }

}
