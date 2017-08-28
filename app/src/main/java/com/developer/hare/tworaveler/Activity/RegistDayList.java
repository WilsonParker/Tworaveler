package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.RegistDayDetailListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;

import java.util.ArrayList;

public class RegistDayList extends AppCompatActivity {

    private UIFactory uiFactory;
    private DateManager dateManager;
    private Intent intent;
    private String strDate;

    private MenuTopTitle menuTopTitle;
    private TextView TV_date;
    private ImageView IV_noData;
    private RecyclerView recyclerView;
    private LinearLayout LL_empty, LL_list;
    private ArrayList<ScheduleDayModel> items = new ArrayList<>();

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_day_detail_list);
        init();
    }

    protected void init() {
        intent = getIntent();
        strDate = intent.getExtras().getString(DataDefinition.Bundle.KEY_DATE);

        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();

        LL_empty = uiFactory.createView(R.id.activity_regist_day_detail_list$LL_empty);
        LL_list = uiFactory.createView(R.id.activity_regist_day_detail_list$LL_list);
        TV_date = uiFactory.createView(R.id.activity_regist_day_detail_list$TV_date);
        IV_noData= uiFactory.createView(R.id.activity_regist_day_detail_list$IV_nologin);
        IV_noData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        recyclerView = uiFactory.createView(R.id.activity_regist_day_detail_list$RV_list);
        menuTopTitle = uiFactory.createView(R.id.activity_regist_day_detail_list$menuTopTItle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        items = createListDate();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        items = createListDate();
        initLayout();
    }

    private void initLayout() {
        if (items.isEmpty()) {
            LL_empty.setVisibility(View.VISIBLE);
            LL_list.setVisibility(View.GONE);
        } else {
            LL_empty.setVisibility(View.GONE);
            LL_list.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new RegistDayDetailListAdapter(items));
        }
    }

    private ArrayList<ScheduleDayModel> createListDate() {
        ArrayList<ScheduleDayModel> items = new ArrayList<>();
        return items;
    }

    private void onRegister() {
    }

}
