package com.developer.hare.tworaveler.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;

public class RegistDayDetail extends AppCompatActivity {
    private TextView TV_start, TV_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_day_detail);
        init();
    }

    private void init() {
        UIFactory uiFactory = UIFactory.getInstance(this);
        TV_start = uiFactory.createView(R.id.regist_day_detail$TV_start);
        TV_end = uiFactory.createView(R.id.regist_day_detail$TV_end);
    }

}
