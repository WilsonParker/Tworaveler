package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.SignIn;
import com.developer.hare.tworaveler.Adapter.AlamListAdapter;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.AlamModel;
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
    private ArrayList<AlamModel> items = new ArrayList<>();

    public static FragmentAlarm newInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        itemEmptyCheck();
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);
        RV_list = uiFactory.createView(R.id.fragment_alarm$RV_list);
        LL_empty = uiFactory.createView(R.id.fragment_alarm$LL_empty);
        LL_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SignIn.class));
            }
        });
        textView = uiFactory.createView(R.id.fragment_alarm$TV_alarm);
        FontManager.getInstance().setFont(textView,  "NotoSansCJKkr-Regular.otf");

        alamListAdapter = new AlamListAdapter(items);
        RV_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RV_list.setAdapter(alamListAdapter);
    }
    private void itemEmptyCheck() {
        if (SessionManager.getInstance().isLogin()) {
            makeItem();
            LL_empty.setVisibility(View.INVISIBLE);
            RV_list.setVisibility(View.VISIBLE);
        } else {
            LL_empty.setVisibility(View.VISIBLE);
            RV_list.setVisibility(View.INVISIBLE);
        }
    }
    private void makeItem(){
        items.clear();
        items.add(new AlamModel(R.drawable.image_history_profile, SessionManager.getInstance().getUserModel().getNickname()));
        alamListAdapter.notifyDataSetChanged();

    }
}
