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
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Map;

public class BagDelete extends AppCompatActivity {

    private RecyclerView RV_deletelist;
    private BagDeleteAdapter bagDeleteAdapter;
    private TextView deletename;
    private LinearLayout linearLayout;
    private MenuTopTitle menuTopTitle;

    private UIFactory uiFactory;
    private ArrayList<BagDeleteModel> items, selected_items;
    private Map<Integer, ArrayList<BagDeleteModel>> bags;


    private BottomNavigationViewEx.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            items = new ArrayList<>();
//            items = DummyDataFactory.createBagDeleteItems();

            setItems(item.getItemId());
            int imageCount = 3;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(BagDelete.this, imageCount);
            RV_deletelist.setLayoutManager(gridLayoutManager);

            itemEmptyCheck(items);
            if (bagDeleteAdapter != null)
                selected_items = bagDeleteAdapter.getSelected_Items();
            bagDeleteAdapter = new BagDeleteAdapter(items, selected_items, BagDelete.this);
            RV_deletelist.setAdapter(bagDeleteAdapter);
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

        deletename = uiFactory.createView(R.id.bag_delete$name);
        RV_deletelist = uiFactory.createView(R.id.bag_delete$RV);
        menuTopTitle = uiFactory.createView(R.id.bag_delete$topbar);
        linearLayout = uiFactory.createView(R.id.bag_delete$LL_empty);

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
                for (int id : ItemFactory.getIds()) {
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

        BottomNavigationViewEx navigationViewEx = uiFactory.createView(R.id.bag_delete$BN_navigation);
        navigationViewEx.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigationViewEx.setCurrentItem(0);
        navigationViewEx.enableShiftingMode(false);
        navigationViewEx.enableItemShiftingMode(false);

    }

    private void setItems(int id) {
        items = ItemFactory.getBagDeleteModelList(id);
        if (items.size() == 0) {
            items = DummyDataFactory.createBagDeleteItems();
            ItemFactory.setBagDeleteModelList(id, items);
        }
    }

    private void itemEmptyCheck(ArrayList<BagDeleteModel> items) {
        if (items.size() > 0) {
            linearLayout.setVisibility(View.GONE);
            RV_deletelist.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            RV_deletelist.setVisibility(View.GONE);
        }
    }
}
