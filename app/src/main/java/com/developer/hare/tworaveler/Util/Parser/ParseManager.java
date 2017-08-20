package com.developer.hare.tworaveler.Util.Parser;

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
}
