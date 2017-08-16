package com.developer.hare.tworaveler.Fragment.Page;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

public class FragmentRegistDayDetail extends BaseFragment {
    private UIFactory uiFactory;
    private DateManager dateManager;
    private Bundle bundle;
    private String strDate;

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


    public static FragmentRegistDayDetail newInstance(String date) {
        FragmentRegistDayDetail f = new FragmentRegistDayDetail();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString(DataDefinition.Bundle.KEY_STARTDATE, date);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regist_day_detail, null);
    }

    @Override
    protected void init(View view) {
        bundle = getArguments();
        strDate = bundle.getString(DataDefinition.Bundle.KEY_DATE);
        Log_HR.log(Log_HR.LOG_INFO, FragmentRegistDayDetail.class, "init(View)", "strDate " + strDate);

        uiFactory = UIFactory.getInstance(view);
        dateManager = DateManager.getInstance();

        TV_locationName = uiFactory.createView(R.id.fragment_regist_day_detail$TV_locationName);
        TV_locationSearch = uiFactory.createView(R.id.fragment_regist_day_detail$TV_locationSearch);
        TV_startTime = uiFactory.createView(R.id.fragment_regist_day_detail$TV_start);
        TV_endTime = uiFactory.createView(R.id.fragment_regist_day_detail$TV_end);
        ET_memo = uiFactory.createView(R.id.fragment_regist_day_detail$ET_meno);
        menuTopTitle = uiFactory.createView(R.id.fragment_regist_day_detail$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.fragment_regist_day_detail$TV_txt_1));
        textViews.add(uiFactory.createView(R.id.fragment_regist_day_detail$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.fragment_regist_day_detail$TV_txt_3));
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