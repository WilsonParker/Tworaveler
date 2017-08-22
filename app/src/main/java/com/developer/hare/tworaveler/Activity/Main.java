package com.developer.hare.tworaveler.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentAlarm;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentBag;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentFeed;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentMyPage;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.CustomNavigationView;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.BackClickManager;

import java.util.ArrayList;

public class Main extends AppCompatActivity {
    private FrameLayout FL_content;
    private Fragment default_fragment = FragmentMyPage.newInstance();
    private UIFactory uiFactory;
    private CustomNavigationView customNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initFnc();
    }

    private void init() {
        FragmentManager.getInstance().setActivity(this);
        uiFactory = UIFactory.getInstance(this);

        FL_content = uiFactory.createView(R.id.main$FL_content);
        createNavigationView();
    }

    private void initFnc() {
        FragmentManager.getInstance().setFragmentContent(default_fragment);
    }

    private void createNavigationView() {
        ArrayList<CustomNavigationView.NavigationItem> items = new ArrayList<>();
        customNavigationView = uiFactory.createView(R.id.main$BN_navigation);
        items.add(customNavigationView.new NavigationItem(R.drawable.icon_home_click, R.drawable.icon_home_unclcik, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                FragmentManager.getInstance().setFragmentContent(FragmentMyPage.newInstance());
            }
        }));
        items.add(customNavigationView.new NavigationItem(R.drawable.icon_home_click, R.drawable.icon_feed_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                FragmentManager.getInstance().setFragmentContent(FragmentFeed.newInstance());
            }
        }));
        items.add(customNavigationView.new NavigationItem(R.drawable.icon_add_click, R.drawable.icon_add_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                SessionManager.getInstance().actionAfterSessoinCheck(Main.this, new SessionManager.OnActionAfterSessionCheckListener() {
                    @Override
                    public void action() {
                        startActivity(new Intent(Main.this, Regist.class));
                    }
                });
            }
        }, false));
        items.add(customNavigationView.new NavigationItem(R.drawable.icon_alarm, R.drawable.icon_alarm_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                FragmentManager.getInstance().setFragmentContent(FragmentAlarm.newInstance());
            }
        }));
        items.add(customNavigationView.new NavigationItem(R.drawable.icon_bag_click, R.drawable.icon_bag_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                FragmentManager.getInstance().setFragmentContent(FragmentBag.newInstance());
            }
        }));
        customNavigationView.bindItemView(this, items);
        customNavigationView.setFirstClickItem(0);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        BackClickManager.getInstance().onBackPressed(this);
    }

}
