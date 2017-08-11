package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.developer.hare.tworaveler.Activity.SignIn;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Page.FragmentMyPageHome;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;

public class FragmentMyPage extends BaseFragment {
    private static FragmentMyPage instance = new FragmentMyPage();
    private ImageView IV_login;
    private MenuTopTitle menuTopTitle;

    public FragmentMyPage() {
        // Required empty public constructor
    }

    public static FragmentMyPage newInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page, null);
    }

    @Override
    protected void init(View view) {
        UIFactory uiFactory = UIFactory.getInstance(getActivity());
        IV_login = uiFactory.createView(R.id.fragment_mypage$IV_nologin);
        IV_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SignIn.class));
            }
        });
        menuTopTitle = uiFactory.createView(R.id.fragment_mypage$topbar);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SignIn.class));
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.main$FL_content, FragmentMyPageHome.newInstance()).addToBackStack(null).commit();
            }
        });
    }

}