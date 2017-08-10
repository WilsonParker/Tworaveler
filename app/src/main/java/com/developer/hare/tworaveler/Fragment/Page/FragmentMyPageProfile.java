package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Activity.ProfileSet;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;

public class FragmentMyPageProfile extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;

    public FragmentMyPageProfile() {
        // Required empty public constructor
    }
    public static FragmentMyPageProfile newInstance() {
        FragmentMyPageProfile fragment = new FragmentMyPageProfile();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page_profile, null);
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(getActivity());
        menuTopTitle = uiFactory.createView(R.id.fragment_mypage_profile$topbar);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileSet.class));
            }
        });
    }
}