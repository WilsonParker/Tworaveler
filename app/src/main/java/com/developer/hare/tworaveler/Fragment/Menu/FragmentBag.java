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
import com.developer.hare.tworaveler.Activity.SignIn;
import com.developer.hare.tworaveler.Adapter.BagListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.CustomNavigationView;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.LogManager;
import com.developer.hare.tworaveler.Util.Parser.RetrofitBodyParser;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Key.KEY_CATEGORY_THEME;
import static com.developer.hare.tworaveler.Data.DataDefinition.Key.KEY_USER_NO;

public class FragmentBag extends BaseFragment {
    private final int imageCount = 3;
    private static FragmentBag instance = new FragmentBag();
    private CustomNavigationView customNavigationBagView;

    private UIFactory uiFactory;
    private TextView textView, TV_bag;
    private ImageView IV_noimage, IV_nologin;
    private RecyclerView RV_list;
    private BagListAdapter bagListAdapter;
    private LinearLayout linearLayout, LL_noLogin, LL_login;
    private MenuTopTitle menuTopTitle;
    private ArrayList<BagModel> items;
    private String theme;
    private UserModel userModel;

    public static FragmentBag newInstance() {
        return  new FragmentBag();
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
                SessionManager.getInstance().actionAfterSessionCheck(getActivity(), new SessionManager.OnActionAfterSessionCheckListener() {
                    @Override
                    public void action() {
                        startActivity(new Intent(getActivity(), BagDelete.class));
                    }
                });
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhoto();
            }
        });
        TV_bag = uiFactory.createView(R.id.fragment_bag$TV_bag);
        FontManager.getInstance().setFont(TV_bag, "NotoSansCJKkr-Regular.otf");
        textView = uiFactory.createView(R.id.fragment_bag$TV_noItem);
        FontManager.getInstance().setFont(textView, "NotoSansCJKkr-Regular.otf");
        linearLayout = uiFactory.createView(R.id.fragment_bag$LL_empty);
        LL_noLogin = uiFactory.createView(R.id.fragment_bag$LL_noLogin);
        LL_login = uiFactory.createView(R.id.fragment_bag$LL_login);
        IV_nologin = uiFactory.createView(R.id.fragment_bag$IV_nologin);
        IV_nologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SignIn.class));
            }
        });
        customNavigationBagView = uiFactory.createView(R.id.fragment_bag$BN_navigation);

        createNavigationBagView();
    }

    @Override
    public void onResume() {
        super.onResume();
        itemLoginCheck();
        itemEmptyCheck(items);
        setList(theme);
    }

    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }

    public void onPhoto() {
        SessionManager.getInstance().actionAfterSessionCheck(getActivity(), new SessionManager.OnActionAfterSessionCheckListener() {
            @Override
            public void action() {
                PhotoManager.getInstance().onGallerySingleSelect(getActivity(), new OnPhotoBindListener() {
                    @Override
                    public void bindData(FileData fileData) {
                        addBag(fileData.getFile());
                    }
                });
            }
        });
    }

    private void itemLoginCheck() {
        if (SessionManager.getInstance().isLogin()) {
            LL_login.setVisibility(View.VISIBLE);
            LL_noLogin.setVisibility(View.INVISIBLE);
        } else {
            LL_login.setVisibility(View.INVISIBLE);
            LL_noLogin.setVisibility(View.VISIBLE);
        }
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

    private void addBag(File file) {
        Map<String, RequestBody> map = new HashMap<>();
        RetrofitBodyParser retrofitBodyParser = RetrofitBodyParser.getInstance();
        map.put(KEY_USER_NO, retrofitBodyParser.createRequestBody(userModel.getUser_no()));
        map.put(KEY_CATEGORY_THEME, retrofitBodyParser.createRequestBody(theme));

        MultipartBody.Part part = retrofitBodyParser.createImageMultipartBodyPart(DataDefinition.Key.KEY_USER_FILE, file);

        Net.getInstance().getFactoryIm().insertBack(part, map).enqueue(new Callback<ResponseModel<BagModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<BagModel>> call, Response<ResponseModel<BagModel>> response) {
                if (response.isSuccessful()) {
//                    LogManager.log(FragmentBag.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                    ResponseModel<BagModel> rbag = response.body();
                    switch (response.body().getSuccess()) {
                        case DataDefinition.Network.CODE_SUCCESS:
                            items.add(rbag.getResult());
                            itemEmptyCheck(items);
                            bagListAdapter.notifyDataSetChanged();
                            break;
                        case DataDefinition.Network.CODE_BAG_ITEM_FIND_FAIL:
                            AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail_2);
                            break;
                        case DataDefinition.Network.CODE_NOT_LOGIN:
                            AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentBag_alert_title_fail_2, R.string.alert_content_not_login);
                            break;
                    }
                } else {
                    netFailAlert(R.string.fragmentBag_alert_title_fail_2, R.string.fragmentBag_alert_content_fail_3);
                    LogManager.log(LogManager.LOG_INFO, FragmentBag.class, "onResponse", "onResponse is not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<BagModel>> call, Throwable t) {
                LogManager.log(FragmentBag.class, "onFailure()", "addBag err : ", t);
                netFailAlert(R.string.fragmentBag_alert_title_fail_2, R.string.fragmentBag_alert_content_fail_3);
            }
        });
    }

    private void setList(String theme) {
        if (!sessionCheck()){
            AlertManager.getInstance().showNotLoginAlert(getActivity(), R.string.fragmentBag_alert_title_fail);
            return;
        }

        Net.getInstance().getFactoryIm().selectBagList(userModel.getUser_no(), theme).enqueue(new Callback<ResponseArrayModel<BagModel>>() {
            @Override
            public void onResponse(Call<ResponseArrayModel<BagModel>> call, Response<ResponseArrayModel<BagModel>> response) {
                if (response.isSuccessful()) {
//                    LogManager.log(FragmentBag.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                    switch (response.body().getSuccess()) {
                        case DataDefinition.Network.CODE_SUCCESS:
                            HandlerManager.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    ResponseArrayModel<BagModel> rbag = response.body();
                                    items = rbag.getResult();
                                    if (items == null)
                                        items = new ArrayList<BagModel>();
                                    itemEmptyCheck(items);
                                    bagListAdapter = new BagListAdapter(items, getActivity());
                                    RV_list.setAdapter(bagListAdapter);
                                    bagListAdapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case DataDefinition.Network.CODE_BAG_ITEM_FIND_FAIL:
                            AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail_2);
                            break;
                    }

                } else {
                    netFailAlert(R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail);
                    LogManager.log(LogManager.LOG_INFO, FragmentBag.class, "onResponse", "onResponse is not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayModel<BagModel>> call, Throwable t) {
                netFailAlert(R.string.fragmentBag_alert_title_fail, R.string.fragmentBag_alert_content_fail);
                LogManager.log(FragmentBag.class, "onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t)", t);
            }
        });
    }

    private void netFailAlert(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(getActivity(), title, content);
    }

    private void createNavigationBagView() {
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
