package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.BagDelete;
import com.developer.hare.tworaveler.Adapter.BagListAdapter;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.Net.NetFactoryIm;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.CustomNavigationView;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBag extends BaseFragment {
    private final int imageCount = 3;
    private static FragmentBag instance = new FragmentBag();
    private CustomNavigationView customNavigationBagView;

    private UIFactory uiFactory;
    private TextView textView;
    private ImageView IV_noimage;
    private RecyclerView RV_list;
    private BagListAdapter bagListAdapter;
    private LinearLayout linearLayout;
    private MenuTopTitle menuTopTitle;

    private ArrayList<BagModel> items;
//
    public FragmentBag() {
        // Required empty public constructor
    }

    public static FragmentBag newInstance() {
        return instance;
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

        textView = uiFactory.createView(R.id.fragment_bag$TV_noItem);
        FontManager.getInstance().setFont(textView, "NotoSansCJKkr-Regular.otf");
        linearLayout = uiFactory.createView(R.id.fragment_bag$LL_empty);

        createNavigationBagView();
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
        Call<RequestArrayModel<BagModel>> result = im.selectBagList();
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
    private void createNavigationBagView() {
        customNavigationBagView = uiFactory.createView(R.id.fragment_bag$BN_navigation);
        ArrayList<CustomNavigationView.NavigationItem> items = new ArrayList<>();
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_ticket_click, R.drawable.icon_ticket_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setList();
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_map_click, R.drawable.icon_map_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setList();
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_subway_click, R.drawable.icon_subway_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
              setList();
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_shop_click, R.drawable.icon_shop_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setList();
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_sale_click, R.drawable.icon_sale_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                setList();
            }
        }));
        customNavigationBagView.bindItemView(getActivity(), items);
        customNavigationBagView.setFirstClickItem(0);

    }
}
