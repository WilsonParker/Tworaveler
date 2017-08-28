package com.developer.hare.tworaveler.Fragment.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.AlamListAdapter;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;

import java.util.ArrayList;

public class FragmentAlarm extends BaseFragment {
    private static FragmentAlarm instance = new FragmentAlarm();
    private TextView textView;
    private UIFactory uiFactory;
    private RecyclerView RV_list;
    private LinearLayout LL_empty;
    private AlamListAdapter alamListAdapter;
    private ArrayList<UserModel> items = new ArrayList<>();

    public FragmentAlarm() {
        // Required empty public constructor
    }

    public static FragmentAlarm newInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, null);
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);
        RV_list = uiFactory.createView(R.id.fragment_alarm$RV_list);
        LL_empty = uiFactory.createView(R.id.fragment_alarm$LL_empty);
        textView = uiFactory.createView(R.id.fragment_alarm$TV_alarm);
        FontManager.getInstance().setFont(textView,  "NotoSansCJKkr-Regular.otf");

        alamListAdapter = new AlamListAdapter(items);
        RV_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RV_list.setAdapter(alamListAdapter);
    }
}
