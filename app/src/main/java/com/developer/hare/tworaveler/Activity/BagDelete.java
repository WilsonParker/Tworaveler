package com.developer.hare.tworaveler.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.BagDeleteAdapter;
import com.developer.hare.tworaveler.Data.DummyDataFactory;
import com.developer.hare.tworaveler.Data.ItemFactory;
import com.developer.hare.tworaveler.Model.BagDeleteModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.CustomNavigationView;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Map;

import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_MAP;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SALE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SHOP;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_SUBWAY;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bag.CATEGORY_TICKET;

public class BagDelete extends AppCompatActivity {

    private RecyclerView RV_deletelist;
    private BagDeleteAdapter bagDeleteAdapter;
    private TextView textView;
    private LinearLayout linearLayout;
    private MenuTopTitle menuTopTitle;
    private final int imageCount = 3;

    private UIFactory uiFactory;
    private ArrayList<BagDeleteModel> items, selected_items;
    private Map<Integer, ArrayList<BagDeleteModel>> bags;
    private CustomNavigationView customNavigationBagView;


    private BottomNavigationViewEx.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            items = new ArrayList<>();
//            items = DummyDataFactory.createBagDeleteItems();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_delete);
        init();

    }

    private void init() {
        uiFactory = UIFactory.getInstance(this);
        selected_items = new ArrayList<>();

        RV_deletelist = uiFactory.createView(R.id.bag_delete$RV);
        menuTopTitle = uiFactory.createView(R.id.bag_delete$topbar);
        customNavigationBagView = uiFactory.createView(R.id.bag_delete$BN_navigation);
        linearLayout = uiFactory.createView(R.id.bag_delete$LL_empty);
        textView = uiFactory.createView(R.id.bag_delete$name);
        FontManager.getInstance().setFont(textView, "NotoSansCJKkr-Regular.otf");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(BagDelete.this, imageCount);
        RV_deletelist.setLayoutManager(gridLayoutManager);

        createNavigationBagView();
        itemEmptyCheck(items);
        bagDeleteAdapter = new BagDeleteAdapter(items, selected_items, BagDelete.this);
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
                Log_HR.log(Log_HR.LOG_INFO, BagDelete.class, "onClick(View)", "selected_size : " + selected_items.size());
                for (String id : ItemFactory.getIds()) {
                    ArrayList<BagDeleteModel> l1 = ItemFactory.getBagDeleteModelList(id);
                    for (BagDeleteModel bdm : selected_items) {
                        if (l1.contains(bdm))
                            l1.remove(bdm);
                    }

                }
                bagDeleteAdapter.notifyDataSetChanged();
                selected_items = new ArrayList<BagDeleteModel>();
            }
        });

    }

    private void setItems(String id) {
        items = ItemFactory.getBagDeleteModelList(id);
        if (items.size() == 0) {
            items = DummyDataFactory.createBagDeleteItems();
            ItemFactory.setBagDeleteModelList(id, items);
        }
        if (bagDeleteAdapter != null)
            selected_items = bagDeleteAdapter.getSelected_Items();
        bagDeleteAdapter = new BagDeleteAdapter(items, selected_items, this);
        RV_deletelist.setAdapter(bagDeleteAdapter);
        itemEmptyCheck(items);
    }

    private void itemEmptyCheck(ArrayList<BagDeleteModel> items) {
        if (items.size() > 0) {
            linearLayout.setVisibility(View.INVISIBLE);
            RV_deletelist.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            RV_deletelist.setVisibility(View.INVISIBLE);
        }
    }

    private void createNavigationBagView() {
        customNavigationBagView = uiFactory.createView(R.id.bag_delete$BN_navigation);
        ArrayList<CustomNavigationView.NavigationItem> items = new ArrayList<>();
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_ticket_click, R.drawable.icon_ticket_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setItems(CATEGORY_TICKET);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_map_click, R.drawable.icon_map_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setItems(CATEGORY_MAP);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_subway_click, R.drawable.icon_subway_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setItems(CATEGORY_SUBWAY);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_shop_click, R.drawable.icon_shop_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setItems(CATEGORY_SHOP);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_sale_click, R.drawable.icon_sale_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setItems(CATEGORY_SALE);
            }
        }));
        customNavigationBagView.bindItemView(this, items);
        customNavigationBagView.setFirstClickItem(0);
    }
}
