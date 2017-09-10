package com.developer.hare.tworaveler.Util.Exception;

/**
 * Created by Hare on 2017-08-31.
 */

public class NullChecker {
    private static NullChecker nullChecker = new NullChecker();

    public static NullChecker getInstance() {
        return nullChecker;
    }

    public boolean nullCheck(Object... obj) {
        return false;
    }

    public boolean nullCheck(Object obj) {
        boolean result = false;
        if (obj == null) {
            result = true;
        } else if (obj instanceof String) {
            String s = (String) obj;
            if (s == null | s.isEmpty() | s.trim().equals(""))
                result = true;
        }
        return result;
    }
}
