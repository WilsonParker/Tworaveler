package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.ProfileSet;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;

import java.util.ArrayList;

public class FragmentMyPageProfile extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private TextView TV_follower, TV_cntfollower, TV_following, TV_cntfollowing, TV_nickname, TV_message;

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
        TV_follower = uiFactory.createView(R.id.fragment_mypage_profile$TV_follower);
        TV_cntfollower = uiFactory.createView(R.id.fragment_mypage_profile$TV_cntfollower);
        TV_following = uiFactory.createView(R.id.fragment_mypage_profile$TV_following);
        TV_cntfollowing = uiFactory.createView(R.id.fragment_mypage_profile$TV_cntfollowing);
        TV_nickname = uiFactory.createView(R.id.fragment_mypage_profile$TV_nickname);
        TV_message = uiFactory.createView(R.id.fragment_mypage_profile$TV_message);
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(TV_follower);
        textViews.add(TV_following);
        textViews.add(TV_nickname);
        textViews.add(TV_message);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
        FontManager.getInstance().setFont(TV_cntfollower, "NotoSansCJKkr-Bold.otf");
        FontManager.getInstance().setFont(TV_cntfollowing, "NotoSansCJKkr-Bold.otf");
    }
}