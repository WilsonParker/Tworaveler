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
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.CustomNavigationView;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Util.Log_HR.LOG_INFO;

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
    private String theme;
    private ResourceManager resourceManager;
    private UserModel userModel;
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
        resourceManager = ResourceManager.getInstance();
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
        sessionCheck();
    }

    @Override
    public void onResume() {
        super.onResume();
        sessionCheck();
    }

    private void sessionCheck() {
        Log_HR.log(LOG_INFO, getClass(), "onResume()", "onResume : "+userModel);
        userModel = SessionManager.getInstance().getUserModel();
    }

    public void onPhoto() {
        PhotoManager.getInstance().onGallerySingleSelect(getActivity(), new OnPhotoBindListener() {
            @Override
            public void bindData(FileData fileData) {
                addData(new BagModel(userModel.getUser_no(),
                        fileData.getFile()
                        , theme));
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

    private void setList(String theme) {
        sessionCheck();
        if (!SessionManager.getInstance().isLogin()) {
            return;
        }
        Log_HR.log(LOG_INFO, getClass(), "setList(String)", "userModel : "+userModel);
        Call<RequestArrayModel<BagModel>> result = Net.getInstance().getFactoryIm().selectBagList(userModel.getUser_no(), theme);
        result.enqueue(new Callback<RequestArrayModel<BagModel>>() {
            @Override
            public void onResponse(Call<RequestArrayModel<BagModel>> call, Response<RequestArrayModel<BagModel>> response) {
                if (response.isSuccessful()) {
                    // 성공했을 경우
                    RequestArrayModel<BagModel> rbag = response.body();
                    items = rbag.getResult();
                    if (items == null) {
                        items = new ArrayList<BagModel>();
                        String url = "http://mblogthumb1.phinf.naver.net/20160506_140/l0o8l1i4_1462510133978p11ro_JPEG/%AA%AA%AA%EB%AA%C1%AA%E5%AA%D0%AA%F3%AB%A8%AB%D3%AA%C1%AA%E5_%F0%AF24%FC%A5_%28DVD_x264_1024x768%29-%AA%AB%AA%DF%AA%D2%AA%B3%AA%A6%AA%AD.avi_20160506_134321.718.jpg?type=w2";
                        items.add(new BagModel(userModel.getUser_no() + "", url, url));
                    }

                    itemEmptyCheck(items);
                    bagListAdapter = new BagListAdapter(items, getActivity());
                    RV_list.setAdapter(bagListAdapter);
                    bagListAdapter.notifyDataSetChanged();
                } else {
                    netFailAlert();
                }
            }

            @Override
            public void onFailure(Call<RequestArrayModel<BagModel>> call, Throwable t) {
                netFailAlert();
            }
        });
    }

    private void netFailAlert() {
        AlertManager.getInstance().createAlert(getActivity(), SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.fragmentBag_alert_title_fail)), resourceManager.getResourceString((R.string.fragmentBag_alert_content_fail))).show();
    }

    private void createNavigationBagView() {
        customNavigationBagView = uiFactory.createView(R.id.fragment_bag$BN_navigation);
        ArrayList<CustomNavigationView.NavigationItem> items = new ArrayList<>();
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_ticket_click, R.drawable.icon_ticket_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                theme = DataDefinition.Bag.CATEGORY_TICKET;
                setList(theme);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_map_click, R.drawable.icon_map_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                theme = DataDefinition.Bag.CATEGORY_MAP;
                setList(theme);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_subway_click, R.drawable.icon_subway_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                theme = DataDefinition.Bag.CATEGORY_SUBWAY;
                setList(theme);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_shop_click, R.drawable.icon_shop_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                theme = DataDefinition.Bag.CATEGORY_SHOP;
                setList(theme);
            }
        }));
        items.add(customNavigationBagView.new NavigationItem(R.drawable.icon_sale_click, R.drawable.icon_sale_unclick, new CustomNavigationView.NavigationOnClickListener() {
            @Override
            public void onClick() {
                theme = DataDefinition.Bag.CATEGORY_SALE;
                setList(theme);
            }
        }));
        customNavigationBagView.bindItemView(getActivity(), items);
        customNavigationBagView.setFirstClickItem(0);

    }
}
