package com.developer.hare.tworaveler.Fragment.Page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Adapter.FeedCityListAdapter;
import com.developer.hare.tworaveler.Adapter.FeedNicknameListAdapter;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentFeed;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Bundle.KEY_FILTER_TYPE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Bundle.KEY_SERIALIZABLE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;
import static com.developer.hare.tworaveler.Data.DataDefinition.Size.SIZE_FEED_LIST_COUNT;

public class FragmentFeedFilter extends BaseFragment {
    private FeedCityListAdapter cityListAdapter;
    private FeedNicknameListAdapter nicknameListAdapter;
    private RecyclerView recyclerView;
    private MenuTopTitle menuTopTitle;
    private CityModel cityModel;
    private UserModel userModel;

    private UIFactory uiFactory;
    private ArrayList<ScheduleModel> feedICityModels = new ArrayList<>();
    private ProgressManager progressManager;
    private int scrollCount = 0,  type;
    public static final int TYPE_CITY = 0x0001;
    public static final int TYPE_NICKNAME= 0x0010;


    public static FragmentFeedFilter newInstance(int type, Serializable serializable) {
        FragmentFeedFilter fragment = new FragmentFeedFilter();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FILTER_TYPE, type);
        bundle.putSerializable(KEY_SERIALIZABLE, serializable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        return view;
    }

    @Override
    protected void init(View view) {
        type = (int) getArguments().get(KEY_FILTER_TYPE);

        if(type == TYPE_CITY){
            cityModel = (CityModel) getArguments().getSerializable(KEY_SERIALIZABLE);
        }

        uiFactory = UIFactory.getInstance(view);
        progressManager = new ProgressManager(getActivity());
        menuTopTitle = uiFactory.createView(R.id.fragment_feed$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentFeed.newInstance());
            }
        });
        menuTopTitle.getIB_right().setVisibility(View.INVISIBLE);
        recyclerView = uiFactory.createView(R.id.fragment_feed$RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onResume() {
        super.onResume();
        updateList(type);
    }
    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }

    private void updateList(int type) {
        if(!sessionCheck()){
            return;
        }
        int user_no = userModel.getUser_no();
        switch (type){
           case TYPE_CITY :
               menuTopTitle.getTV_title().setText(cityModel.getCity());
               progressManager.actionWithState(new OnProgressAction() {
                   @Override
                   public void run() {
                       Net.getInstance().getFactoryIm().searchFeedCity(user_no, cityModel.getCity()).enqueue(new Callback<ResponseArrayModel<ScheduleModel>>() {
                           @Override
                           public void onResponse(Call<ResponseArrayModel<ScheduleModel>> call, Response<ResponseArrayModel<ScheduleModel>> response) {
                               if (response.isSuccessful()) {
                                   Log_HR.log(Log_HR.LOG_INFO, FragmentFeedFilter.class, "onResponse", "is Success?" + response.isSuccessful());
                                   Log_HR.log(Log_HR.LOG_INFO, FragmentFeedFilter.class, "onResponse", "is Success?" + response.message());
                                   Log_HR.log(Log_HR.LOG_INFO, FragmentFeedFilter.class, "onResponse", "is Success?" + response.body());
                                   progressManager.endRunning();
                                   ResponseArrayModel<ScheduleModel> model = response.body();
                                   if (model.getSuccess() == CODE_SUCCESS) {
                                       HandlerManager.getInstance().getHandler().post(new Runnable() {
                                           @Override
                                           public void run() {
                                               cityListAdapter = new FeedCityListAdapter(model.getResult(), new OnListScrollListener() {
                                                   @Override
                                                   public void scrollEnd() {
                                                       if (feedICityModels.size() == SIZE_FEED_LIST_COUNT * scrollCount) {
                                                       }
                                                   }
                                               });
                                               ++scrollCount;
                                               recyclerView.setAdapter(cityListAdapter);
                                           }
                                       });
                                   }
                               } else {
                                   Log_HR.log(Log_HR.LOG_ERROR, FragmentFeedFilter.class, "onResponse(Call<ResponseArrayModel<ScheduleModel>>, Response<ResponseArrayModel<ScheduleModel>>)", "response is not Successful");
                                   netFail();
                               }
                           }
                           @Override
                           public void onFailure(Call<ResponseArrayModel<ScheduleModel>> call, Throwable t) {
                               Log_HR.log(FragmentFeedFilter.class, "onFailure(Call<ResponseArrayModel<ScheduleModel>> ,Throwable)", "Fail", t);
                               netFail();
                           }
                       });
                   }
               });
               break;
           case TYPE_NICKNAME :

               break;
       }
    }

    private void netFail() {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentFeed_alert_title_fail, R.string.fragmentFeed_alert_content_fail);
    }

    @Override
    public void onStop() {
        super.onStop();
        scrollCount = 0;
    }
}
