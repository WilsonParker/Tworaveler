package com.developer.hare.tworaveler.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.FontManager;

import java.util.ArrayList;

public class RegistDayDetail extends AppCompatActivity {
    private UIFactory uiFactory;
    private DateManager dateManager;
    private String strDate;
    private Intent intent;

    private MenuTopTitle menuTopTitle;
    private TextView TV_locationName, TV_locationSearch, TV_startTime, TV_endTime;
    private EditText ET_memo;

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

        }
    };

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
        setContentView(R.layout.activity_regist_day_detail);
        init();
    }
    
    protected void init() {
        intent = getIntent();
        strDate = intent.getExtras().getString(DataDefinition.Intent.KEY_DATE);

        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();

        TV_locationName = uiFactory.createView(R.id.activity_regist_day_detail$TV_locationName);
        TV_locationSearch = uiFactory.createView(R.id.activity_regist_day_detail$TV_locationSearch);
        TV_startTime = uiFactory.createView(R.id.activity_regist_day_detail$TV_start);
        TV_endTime = uiFactory.createView(R.id.activity_regist_day_detail$TV_end);
        ET_memo = uiFactory.createView(R.id.activity_regist_day_detail$ET_meno);
        menuTopTitle = uiFactory.createView(R.id.activity_regist_day_detail$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_1));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_3));
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
        textViews.clear();
        textViews.add(TV_locationName);
        textViews.add(TV_locationSearch);
        textViews.add(TV_startTime);
        textViews.add(TV_endTime);
        textViews.add(ET_memo);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
        createListDate();
    }

    private void createListDate() {

    }

    private void onRegist() {

    }
}
