package com.developer.hare.tworaveler.Util;

import com.developer.hare.tworaveler.Listener.OnListScrollListener;

/**
 * Created by Hare on 2017-08-23.
 */

public class ScrollEndMethod {
    public static ScrollEndMethod scrollEndMethod = new ScrollEndMethod();

    public static ScrollEndMethod getInstance() {
        return scrollEndMethod;
    }

    public void actionAfterScrollEnd(int size, int position, OnListScrollListener onListScrollListener) {
        if (size - 1 == position)
            onListScrollListener.scrollEnd();
    }
}
