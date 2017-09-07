package com.developer.hare.tworaveler.Util.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hare on 2017-08-07.
 */

public class ParseManager {
    private static final ParseManager ourInstance = new ParseManager();

    public static ParseManager getInstance() {
        return ourInstance;
    }


    public int[] stringArrToIntArr(String[] sArr) {
        //        int[] is = Arrays.asList(sArray).stream().mapToInt(Integer::parseInt).toArray();
//        Arrays.asList(sArray).stream().forEach(this::stringToInt);
        int[] iArr = new int[sArr.length];
        for (int i = 0; i < sArr.length; i++)
            iArr[i] = Integer.parseInt(sArr[i]);
        return iArr;
    }

    public Map<String, Object> toMap(Object object) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            String mName = method.getName().toUpperCase();
            if (mName.contains("GET")) {
                map.put(mName.replace("GET", "").toLowerCase(), method.invoke(object));
            }
        }
        return map;
    }
}
