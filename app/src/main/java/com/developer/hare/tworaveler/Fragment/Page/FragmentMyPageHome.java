package com.developer.hare.tworaveler.Fragment.Page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.HomeListAdapter;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.ResourceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class FragmentMyPageHome extends BaseFragment {
    private static FragmentMyPageHome fragment = new FragmentMyPageHome();
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private RecyclerView recyclerView;
    private TextView TV_noItem;

    private ResourceManager resourceManager;
    private HomeListAdapter homeListAdapter;
    private ProgressManager progressManager;
    private ArrayList<ScheduleModel> items = new ArrayList<>();

    public static FragmentMyPageHome newInstance() {
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page_home, null);
    }

    @Override
    protected void init(View view) {
        resourceManager = ResourceManager.getInstance();
        progressManager = new ProgressManager(getActivity());
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
        homeListAdapter = new HomeListAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(homeListAdapter);
        TV_noItem = uiFactory.createView(R.id.fragment_mypage_home$TV_noitem);
        FontManager.getInstance().setFont(TV_noItem, "NotoSansCJKkr-Regular.otf");

    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                Net.getInstance().getFactoryIm().selectMyScheduleList(SessionManager.getInstance().getUserModel().getUser_no()).enqueue(new Callback<ResponseArrayModel<ScheduleModel>>() {
                    @Override
                    public void onResponse(Call<ResponseArrayModel<ScheduleModel>> call, Response<ResponseArrayModel<ScheduleModel>> response) {
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            ResponseArrayModel<ScheduleModel> model = response.body();
                            if (model.getSuccess() == CODE_SUCCESS) {
                                HandlerManager.getInstance().getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        items = model.getResult();
                                        homeListAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                        } else {
                            Log_HR.log(Log_HR.LOG_ERROR, FragmentMyPageHome.class, "onResponse(Call<ResponseArrayModel<ScheduleModel>>, Response<ResponseArrayModel<ScheduleModel>>)", "response is not Successful");
                            netFail();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseArrayModel<ScheduleModel>> call, Throwable t) {
                        Log_HR.log(FragmentMyPageHome.class, "onFailure(Call<ResponseArrayModel<ScheduleModel>> ,Throwable)", "Fail", t);
                        netFail();
                    }
                });
            }
        });
    }

    private void netFail() {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentMyPageHome_alert_title_fail, R.string.fragmentMyPageHome_alert_content_fail);
    }
}