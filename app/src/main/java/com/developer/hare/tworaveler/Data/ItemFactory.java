package com.developer.hare.tworaveler.Data;

import android.app.Fragment;

import com.developer.hare.tworaveler.Fragment.Menu.FragmentAlarm;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentBag;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentMyPage;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentPeed;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentRegist;
import com.developer.hare.tworaveler.Model.BagDeleteModel;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hare on 2017-08-06.
 */

public class ItemFactory {
    private static Map<Integer, Fragment> MainNavigationFragmentMap;
    private static Map<Integer, ArrayList<BagModel>> BagModelsMap;
    private static Map<Integer, ArrayList<BagDeleteModel>> BagDeleteModelsMap;

    private static final int[] ids = {R.id.navigation_bag$ticket, R.id.navigation_bag$map, R.id.navigation_bag$root, R.id.navigation_bag$sale, R.id.navigation_bag$shopping};


    static {
        createMainNavigationFragmentMap();
        createBagModels();
        createBagDeleteModels();
    }

    public static int[] getIds() {
        return ids;
    }

    private static void createMainNavigationFragmentMap() {
        MainNavigationFragmentMap = new HashMap<>();
        MainNavigationFragmentMap.put(R.id.navigation$peed, FragmentPeed.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$regist, FragmentRegist.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$alram, FragmentAlarm.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$bag, FragmentBag.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$home, FragmentMyPage.newInstance());
    }

    public static Fragment getMainNavigationFragment(int id) {
        return MainNavigationFragmentMap.get(id);
    }

    private static void createBagModels() {
        BagModelsMap = new HashMap<>();
        for (int id : ids)
            BagModelsMap.put(id, new ArrayList<>());
    }

    public static ArrayList<BagModel> getBagModelList(int id) {
        return BagModelsMap.get(id);
    }

    private static void createBagDeleteModels() {
        BagDeleteModelsMap = new HashMap<>();
        for (int id : ids)
            BagDeleteModelsMap.put(id, new ArrayList<>());
    }

    public static ArrayList<BagDeleteModel> getBagDeleteModelList(int id) {
        return BagDeleteModelsMap.get(id);
    }

    public static void setBagDeleteModelList(int id, ArrayList<BagDeleteModel> models) {
        BagDeleteModelsMap.put(id, models);
    }
}
