package com.developer.hare.tworaveler.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.developer.hare.tworaveler.Data.ItemFactory;
import com.developer.hare.tworaveler.Fragment.Page.FragmentMyPageHome;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.UIFactory;

public class Main extends AppCompatActivity {
    private FrameLayout FL_content;
    private Fragment default_fragment = new FragmentMyPageHome();
    private UIFactory uiFactory;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            /* switch (item.getItemId()) {
                case R.id.navigation$peed:
                    fragment = new FragmentPeed();
                    result = true;
                    break;
                case R.id.navigation$regist:
                    fragment = new FragmentRegist();
                    result = true;
                    break;
                case R.id.navigation$alram:
                    fragment = new FragmentAlarm();
                    result = true;
                    break;
                case R.id.navigation$bag:
                    fragment = new FragmentBag();
                    result = true;
                    break;
                case R.id.navigation$home:
                default:
                    fragment = new FragmentMyPage();
                    result = true;
                    break;
            }*/
            Fragment fragment = ItemFactory.getMainNavigationFragment(item.getItemId());
            setFragmentContent(fragment);
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initFnc();
    }

    private void init() {
        FragmentManager.setActivity(this);
        uiFactory = UIFactory.getInstance(this);

        FL_content = uiFactory.createView(R.id.main$FL_content);
        BottomNavigationView navigation = uiFactory.createView(R.id.main$BN_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initFnc() {
        setFragmentContent(default_fragment);
    }

    private void setFragmentContent(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.main$FL_content, fragment).addToBackStack(null).commit();
    }

}
