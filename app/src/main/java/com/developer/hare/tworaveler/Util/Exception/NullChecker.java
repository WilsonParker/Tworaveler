package com.developer.hare.tworaveler.Util.Exception;

/**
 * Created by Hare on 2017-08-31.
 */

public class NullChecker {

    public boolean nullCheck(Object... obj) {
        return false;
    }

    private boolean checkNnull(Object obj) {
        boolean result = false;
        if (obj instanceof String) {
            String s = (String) obj;
            if (s == null | s.isEmpty())
                result = true;
        }
        return result;
    }
}
