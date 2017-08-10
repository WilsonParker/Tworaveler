package com.developer.hare.tworaveler.Data;

import com.developer.hare.tworaveler.Model.BagDeleteModel;
import com.developer.hare.tworaveler.Model.PeedItemModel;

import java.util.ArrayList;

/**
 * Created by Hare on 2017-08-01.
 */

public class DummyDataFactory {

    public static ArrayList<PeedItemModel> createPeedItems() {
        ArrayList<PeedItemModel> models = new ArrayList<PeedItemModel>();
        for (int i = 0; i < 20; i++) {
            models.add(new PeedItemModel("id_" + i, "message_" + i, "startDate_" + i, "endDate_" + i
                    , "http://t1.daumcdn.net/cartoon/58A662E302388A0001"
                    , "http://cdn.ebichu.cogul.co.kr/wp-content/uploads/2016/06/13160524/04about_img04.png"));
        }
        return models;
    }

    public static ArrayList<BagDeleteModel> createBagDeleteItems() {
        ArrayList<BagDeleteModel> models = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            models.add(new BagDeleteModel(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8FkTxVbIq7NemlwGX6JxJzS1Et-hq99RdAhd1xTSEldz_3Mu6"
                    , ""));
        return models;
    }
}
