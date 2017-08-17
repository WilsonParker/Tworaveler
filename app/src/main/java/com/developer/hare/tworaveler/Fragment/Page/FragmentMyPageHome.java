package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.FeedListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;

public class FragmentMyPageHome extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private RecyclerView recyclerView;
//    private HomeListAdapter homeListAdapter;
    private FeedListAdapter feedListAdapter;
    private Context context;
    private TextView TV_noItem;

    public FragmentMyPageHome() {
    }

    public static FragmentMyPageHome newInstance() {
        FragmentMyPageHome fragment = new FragmentMyPageHome();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page_home, null);
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);

        menuTopTitle = uiFactory.createView(R.id.fragment_mypage_home$topbar); //
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), FragmentMyPageProfile.class));
                FragmentManager.getInstance().setFragmentContent(FragmentMyPageProfile.newInstance());
            }
        });
        recyclerView = uiFactory.createView(R.id.fragment_mypage_home$RV);
//        feedListAdapter = new FeedListAdapter(DummyDataFactory.createFeedItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedListAdapter);
        TV_noItem = uiFactory.createView(R.id.fragment_mypage_home$TV_noitem);
        FontManager.getInstance().setFont(TV_noItem, "NotoSansCJKkr-Regular.otf");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                CityModel model = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);

            }
        }
    }
}