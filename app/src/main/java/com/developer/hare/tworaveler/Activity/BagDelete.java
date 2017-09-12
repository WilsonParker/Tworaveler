package com.developer.hare.tworaveler.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.BagDeleteAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.CustomNavigationView;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.Util.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_MAP;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SALE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SHOP;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SUBWAY;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_TICKET;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class BagDelete extends AppCompatActivity {

    private RecyclerView RV_deletelist;
    private BagDeleteAdapter bagDeleteAdapter;
    private TextView textView;
    private LinearLayout linearLayout;
    private MenuTopTitle menuTopTitle;
    private final int imageCount = 3;

    private String selected_theme;
    private Activity activity;
    private UserModel userModel;
    private UIFactory uiFactory;
    private ArrayList<BagModel> items = new ArrayList<>(), selected_items = new ArrayList<>();
    private Map<String, ArrayList<BagModel>> bags = new HashMap<>();
    private CustomNavigationView customNavigationBagView;

    public final String[] BAG_CATEGORYS = {CATEGORY_TICKET, CATEGORY_MAP, CATEGORY_SUBWAY, CATEGORY_SHOP, CATEGORY_SALE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_delete);
        init();
    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        activity = this;

        RV_deletelist = uiFactory.createView(R.id.bag_delete$RV);
        menuTopTitle = uiFactory.createView(R.id.bag_delete$topbar);
        customNavigationBagView = uiFactory.createView(R.id.bag_delete$BN_navigation);
        linearLayout = uiFactory.createView(R.id.bag_delete$LL_empty);
        textView = uiFactory.createView(R.id.bag_delete$name);
        FontManager.getInstance().setFont(textView, "NotoSansCJKkr-Regular.otf");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, imageCount);
        RV_deletelist.setLayoutManager(gridLayoutManager);

        bagDeleteAdapter = new BagDeleteAdapter(items, selected_items, activity);
        RV_deletelist.setAdapter(bagDeleteAdapter);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBagItems();
            }
        });
        createNavigationBagView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void itemEmptyCheck(ArrayList<BagModel> items) {
        if (items.size() > 0) {
            linearLayout.setVisibility(View.INVISIBLE);
            RV_deletelist.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            RV_deletelist.setVisibility(View.INVISIBLE);
        }
    }

    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }

    private void createList(String theme) {
        if (!sessionCheck()) {
            AlertManager.getInstance().showNotLoginAlert(this, R.string.bagDelete_alert_title_fail);
            return;
        }
        items = bags.get(theme);
        if (items != null) {
            setItem(theme);
            return;
        }
        Net.getInstance().getFactoryIm().selectBagList(userModel.getUser_no(), theme).enqueue(new Callback<ResponseArrayModel<BagModel>>() {
            @Override
            public void onResponse(Call<ResponseArrayModel<BagModel>> call, Response<ResponseArrayModel<BagModel>> response) {
                if (response.isSuccessful()) {
                    // 성공했을 경우
                    switch (response.body().getSuccess()) {
                        case CODE_SUCCESS:
                            ResponseArrayModel<BagModel> rbag = response.body();
                            items = rbag.getResult();
                            if (items.size() == 0)
                                items = new ArrayList<BagModel>();
                            setItem(theme);
                            break;
                        case DataDefinition.Network.CODE_BAG_ITEM_FIND_FAIL:
                            netFailAlert(R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail);
                            break;
                    }

                } else {
                    netFailAlert(R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail);
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayModel<BagModel>> call, Throwable t) {
                netFailAlert(R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail_2);
            }
        });
    }

    private void deleteBagItems() {
        ArrayList<Integer> nos = new ArrayList<>();
        for (BagModel model : selected_items)
            nos.add(model.getItem_no());

        Net.getInstance().getFactoryIm().deleteBagItemList(nos).enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                LogManager.log(BagDelete.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                if (response.isSuccessful()) {
                    switch (response.body().getSuccess()) {
                        case CODE_SUCCESS:
                            for (String id : BAG_CATEGORYS) {
                                ArrayList<BagModel> bagList = bags.get(id);
                                if (bagList == null)
                                    continue;
                                for (BagModel selectedModel : selected_items) {
                                    if (bagList.contains(selectedModel))
                                    bagList.remove(selectedModel);
                                }
                            }
                            createList(selected_theme);
                            bagDeleteAdapter.notifyDataSetChanged();
                            break;
                        case DataDefinition.Network.CODE_BAG_ITEM_FIND_FAIL:
                            netFailAlert(R.string.bagDelete_alert_title_fail, R.string.bagDelete_alert_content_fail);
                            break;
                    }
                } else {
                    netFailAlert(R.string.bagDelete_alert_title_fail, R.string.bagDelete_alert_content_fail);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                LogManager.log(BagDelete.class, "onFailure(Call<ResponseArrayModel<String>> call, Throwable t)", t);
                netFailAlert(R.string.bagDelete_alert_title_fail, R.string.bagDelete_alert_content_fail_2);
            }
        });
    }

    private void setItem(String theme) {
        if (bagDeleteAdapter != null)
            selected_items = bagDeleteAdapter.getSelected_Items();
        bags.put(theme, items);
        bagDeleteAdapter = new BagDeleteAdapter(items, selected_items, activity);
        RV_deletelist.setAdapter(bagDeleteAdapter);
        itemEmptyCheck(items);
    }

    private void netFailAlert(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(activity, title, content);
    }

    private void createNavigationBagView() {
        customNavigationBagView = uiFactory.createView(R.id.bag_delete$BN_navigation);
        ArrayList<CustomNavigationView.NavigationItem> items = new ArrayList<>();
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_ticket_click, R.drawable.icon_ticket_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                selected_theme = CATEGORY_TICKET;
                createList(CATEGORY_TICKET);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_map_click, R.drawable.icon_map_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                selected_theme = CATEGORY_MAP;
                createList(CATEGORY_MAP);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_subway_click, R.drawable.icon_subway_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                selected_theme = CATEGORY_SUBWAY;
                createList(CATEGORY_SUBWAY);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_shop_click, R.drawable.icon_shop_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                selected_theme = CATEGORY_SHOP;
                createList(CATEGORY_SHOP);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_sale_click, R.drawable.icon_sale_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                selected_theme = CATEGORY_SALE;
                createList(CATEGORY_SALE);
            }
        }));
        customNavigationBagView.bindItemView(this, items);
        customNavigationBagView.actionItem(0);
    }

}
