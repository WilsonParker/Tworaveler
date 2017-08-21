package com.developer.hare.tworaveler.Fragment.Page;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.MypageDetailAdapter;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentFeed;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.Model.ScheduleDayRootModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.ResourceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMypageDetail extends BaseFragment {

    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private RecyclerView recyclerView;
    private TextView TV_noItem;
    private MypageDetailAdapter mypageDetailAdapter;
    private ResourceManager resourceManager;
    private ProgressManager progressManager;
    private ArrayList<ScheduleDayModel> items = new ArrayList<>();
    private static ScheduleModel scheduleModel;

    public FragmentMypageDetail() {
    }

    public static FragmentMypageDetail newInstance(ScheduleModel model) {
        FragmentMypageDetail fragment = new FragmentMypageDetail();
        scheduleModel = model;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mypage_detail, container, false);
    }
    @Override
    protected void init(View view) {
        resourceManager = ResourceManager.getInstance();
        progressManager = new ProgressManager(getActivity());
        uiFactory = UIFactory.getInstance(view);

        menuTopTitle = uiFactory.createView(R.id.fragment_mypage_detail$topbar); //
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager.getInstance().setFragmentContent(FragmentMyPageSchedule.newInstance());

            }
        });
        recyclerView = uiFactory.createView(R.id.fragment_mypage_detail$RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mypageDetailAdapter);

        TV_noItem = uiFactory.createView(R.id.fragment_mypage_detail$TV_noitem);
        FontManager.getInstance().setFont(TV_noItem, "NotoSansCJKkr-Regular.otf");
        updateList();
    }
    private void updateList(){
           progressManager.actionWithState(new OnProgressAction() {
               @Override
               public void run() {
                   Call<ResponseArrayModel<ScheduleDayRootModel>> result = Net.getInstance().getFactoryIm().selectDetailSchedule(SessionManager.getInstance().getUserModel().getUser_no());
                   result.enqueue(new Callback<ResponseArrayModel<ScheduleDayRootModel>>() {
                       @Override
                       public void onResponse(Call<ResponseArrayModel<ScheduleDayRootModel>> call, Response<ResponseArrayModel<ScheduleDayRootModel>> response) {
                            if(response.isSuccessful()){
                                progressManager.endRunning();
                                ResponseArrayModel<ScheduleDayRootModel> model = response.body();
                           }else
                           {
                               Log_HR.log(Log_HR.LOG_ERROR, FragmentMypageDetail.class, "onResponse(Call<ResponseArrayModel<ScheduleDayRootModel>>, Response<ResponseArrayModel<ScheduleDayRootModel>>)", "response is not Successful");
                               netFail();
                           }
                        }
                       @Override
                       public void onFailure(Call<ResponseArrayModel<ScheduleDayRootModel>> call, Throwable t) {
                           Log_HR.log(FragmentFeed.class, "onFailure(Call<ResponseArrayModel<ScheduleDayRootModel>> ,Throwable)", "Fail", t);
                           netFail();
                       }
                   });
               }
           });
    }
    private void netFail() {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentMyPageDetail_alert_title_fail, R.string.fragmentMyPageDetail_alert_content_fail);
    }
}
