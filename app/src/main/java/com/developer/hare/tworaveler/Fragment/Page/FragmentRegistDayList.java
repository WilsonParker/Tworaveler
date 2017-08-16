package com.developer.hare.tworaveler.Fragment.Page;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developer.hare.tworaveler.Adapter.RegistDayDetailListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

public class FragmentRegistDayList extends BaseFragment {
    private UIFactory uiFactory;
    private DateManager dateManager;
    private Bundle bundle;
    private String strDate;

    private MenuTopTitle menuTopTitle;
    private TextView TV_date;
    private ImageView IV_noData;
    private RecyclerView recyclerView;
    private LinearLayout LL_empty, LL_list;
    private ArrayList<ScheduleDayModel> items = new ArrayList<>();

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


    public static FragmentRegistDayList newInstance(String date) {
        FragmentRegistDayList f = new FragmentRegistDayList();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString(DataDefinition.Bundle.KEY_STARTDATE, date);
        f.setArguments(args);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regist_day_detail_list, null);
    }

    @Override
    protected void init(View view) {
        bundle = getArguments();
        strDate = bundle.getString(DataDefinition.Bundle.KEY_DATE);
        Log_HR.log(Log_HR.LOG_INFO, FragmentRegistDayList.class, "init(View)", "strDate " + strDate);

        uiFactory = UIFactory.getInstance(view);
        dateManager = DateManager.getInstance();

        LL_empty = uiFactory.createView(R.id.fragment_regist_detail_list$LL_empty);
        LL_list = uiFactory.createView(R.id.fragment_regist_detail_list$LL_list);
        TV_date = uiFactory.createView(R.id.fragment_regist_detail_list$TV_date);
        IV_noData= uiFactory.createView(R.id.fragment_regist_detail_list$IV_nologin);
        IV_noData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });
        recyclerView = uiFactory.createView(R.id.fragment_regist_detail_list$RV_list);
        menuTopTitle = uiFactory.createView(R.id.fragment_regist_detail_list$menuTopTItle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });
        items = createListDate();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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

    private void onRegist() {
        FragmentManager.getInstance().setFragmentContent(FragmentRegistDayDetail.newInstance(""));
    }
}