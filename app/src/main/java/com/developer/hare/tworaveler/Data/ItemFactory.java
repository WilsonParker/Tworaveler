package com.developer.hare.tworaveler.Data;

import android.app.Fragment;

import com.developer.hare.tworaveler.Model.BagDeleteModel;
import com.developer.hare.tworaveler.Model.BagModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_MAP;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_ROUTE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SALE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SHOPPING;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_TRAFFIC;

/**
 * Created by Hare on 2017-08-06.
 */

public class ItemFactory {
    private static Map<String, Fragment> MainNavigationFragmentMap;
    private static Map<String, ArrayList<BagModel>> BagModelsMap;
    private static Map<String, ArrayList<BagDeleteModel>> BagDeleteModelsMap;

    private static final String[] ids = {CATEGORY_TRAFFIC,CATEGORY_MAP,CATEGORY_ROUTE,CATEGORY_SHOPPING,CATEGORY_SALE};


    static {
        createMainNavigationFragmentMap();
        createBagModels();
        createBagDeleteModels();
    }

    public static String[] getIds() {
        return ids;
    }

    private static void createMainNavigationFragmentMap() {
        MainNavigationFragmentMap = new HashMap<>();
      /*  MainNavigationFragmentMap.put(R.id.navigation$peed, FragmentPeed.newInstance());
//        MainNavigationFragmentMap.put(R.id.navigation$regist, FragmentRegist.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$alram, FragmentAlarm.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$bag, FragmentBag.newInstance());
        MainNavigationFragmentMap.put(R.id.navigation$home, FragmentMyPage.newInstance());*/
    }

    public static Fragment getMainNavigationFragment(String id) {
        return MainNavigationFragmentMap.get(id);
    }

    private static void createBagModels() {
        BagModelsMap = new HashMap<>();
        for (String id : ids)
            BagModelsMap.put(id, new ArrayList<>());
    }

    public static ArrayList<BagModel> getBagModelList(String id) {
        return BagModelsMap.get(id);
    }

    private static void createBagDeleteModels() {
        BagDeleteModelsMap = new HashMap<>();
        for (String id : ids)
            BagDeleteModelsMap.put(id, new ArrayList<>());
    }

    public static ArrayList<BagDeleteModel> getBagDeleteModelList(String id) {
        return BagDeleteModelsMap.get(id);
    }

    public static void setBagDeleteModelList(String id, ArrayList<BagDeleteModel> models) {
        BagDeleteModelsMap.put(id, models);
    }
}
