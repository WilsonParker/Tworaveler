package com.developer.hare.tworaveler.Fragment.Menu;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.developer.hare.tworaveler.Activity.BagDelete;
import com.developer.hare.tworaveler.Adapter.BagListAdapter;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.Net.NetFactoryIm;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBag extends BaseFragment {
    private final int imageCount = 3;

    private UIFactory uiFactory;
    private Fragment default_fragment;
    private ImageView IV_noimage;
    private Button BT_image;
    private RecyclerView RV_list;
    private BagListAdapter bagListAdapter;
    private LinearLayout linearLayout;
    private MenuTopTitle menuTopTitle;

    private ArrayList<BagModel> items;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          /*  boolean result;
            items = new ArrayList<>();
            switch (item.getItemId()) {
                case R.id.navigation_bag$ticket:
                    // item 데이터 추가
                    result = true;
                    break;
                case R.id.navigation_bag$sale:
                    result = true;
                    break;
                case R.id.navigation_bag$root:
                    result = true;
                    break;
                case R.id.navigation_bag$map:
                    result = true;
                    break;
                case R.id.navigation_bag$shopping:
                default:
                    result = true;
                    break;
            }*/

            setList();
            return true;
        }

    };

    public FragmentBag() {
        // Required empty public constructor
    }

    public static FragmentBag newInstance() {
        FragmentBag fragment = new FragmentBag();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bag, container, false);
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);
        RV_list = uiFactory.createView(R.id.fragment_bag$RV);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), imageCount);
        RV_list.setLayoutManager(gridLayoutManager);

        items = new ArrayList<>();
        bagListAdapter = new BagListAdapter(items, getActivity());

        IV_noimage = uiFactory.createView(R.id.fragment_bag$noimage);
        IV_noimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhoto();
            }
        });

        menuTopTitle = uiFactory.createView(R.id.fragment_bag$topbar);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BagDelete.class));
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhoto();
            }
        });

        linearLayout = uiFactory.createView(R.id.fragment_bag$LL_empty);

        BottomNavigationViewEx navigation = uiFactory.createView(R.id.fragment_bag$BN_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setCurrentItem(0);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);

        itemEmptyCheck(items);
    }

    public void onPhoto() {
        PhotoManager.getInstance().onGallerySingleSelect(getActivity(), new OnPhotoBindListener() {
            @Override
            public void bindData(FileData fileData) {
                addData(new BagModel(
                        fileData.getFile()
                        , ""));
            }
        });
    }

    private void itemEmptyCheck(ArrayList<BagModel> items) {
        if (items.size() >= 1) {
            linearLayout.setVisibility(View.GONE);
            RV_list.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            RV_list.setVisibility(View.GONE);
        }
    }

    private void addData(BagModel model) {
        items.add(model);
        bagListAdapter.notifyDataSetChanged();
        itemEmptyCheck(items);
    }

    private void setList() {
        NetFactoryIm im = Net.getInstance().getFactoryIm();
        Call<RequestArrayModel<BagModel>> result = im.getBagList();
        result.enqueue(new Callback<RequestArrayModel<BagModel>>() {
            @Override
            public void onResponse(Call<RequestArrayModel<BagModel>> call, Response<RequestArrayModel<BagModel>> response) {
                if (response.isSuccessful()) {
                    // 성공했을 경우

                    RequestArrayModel<BagModel> rbag = response.body();
                    items = rbag.getResult();

                    itemEmptyCheck(items);
                    bagListAdapter = new BagListAdapter(items, getActivity());
                    RV_list.setAdapter(bagListAdapter);
                    bagListAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<RequestArrayModel<BagModel>> call, Throwable t) {

            }
        });
    }
}
