package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Activity.SearchFeed;
import com.developer.hare.tworaveler.Adapter.FeedListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Page.FragmentFeedFilter;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
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
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_CITYMODEL;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.RESULT_CODE_CITY_MODEL;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.RESULT_CODE_SCHEDULE_MODEL;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.RESULT_CODE_SEARCH_CITY;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;
import static com.developer.hare.tworaveler.Data.DataDefinition.Size.SIZE_FEED_LIST_COUNT;

public class FragmentFeed extends BaseFragment {
    private static FragmentFeed instance = new FragmentFeed();
    private FeedListAdapter feedListAdapter;
    private RecyclerView recyclerView;
    private MenuTopTitle menuTopTitle;

    private UIFactory uiFactory;
    private ArrayList<ScheduleModel> feedItemModels = new ArrayList<>();
    private ProgressManager progressManager;
    private int scrollCount = 0;

    public static FragmentFeed newInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        return view;
    }

    @Override
    protected void init(View view) {
        uiFactory = UIFactory.getInstance(view);
        progressManager = new ProgressManager(getActivity());
        menuTopTitle = uiFactory.createView(R.id.fragment_feed$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchFeed.class);
                startActivityForResult(intent, RESULT_CODE_SEARCH_CITY);
            }
        });
        menuTopTitle.getIB_left().setVisibility(View.INVISIBLE);
        recyclerView = uiFactory.createView(R.id.fragment_feed$RV);
        feedListAdapter = new FeedListAdapter(feedItemModels, new OnListScrollListener() {
            @Override
            public void scrollEnd() {
                if (feedItemModels.size() == SIZE_FEED_LIST_COUNT * scrollCount) {
                    updateList();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedListAdapter);

        loginFilter();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        int user_no = SessionManager.getInstance().isLogin() ? SessionManager.getInstance().getUserModel().getUser_no() : -1;
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                Call<ResponseArrayModel<ScheduleModel>> result = Net.getInstance().getFactoryIm().selectFeedList(user_no, scrollCount);
                result.enqueue(new Callback<ResponseArrayModel<ScheduleModel>>() {
                    @Override
                    public void onResponse(Call<ResponseArrayModel<ScheduleModel>> call, Response<ResponseArrayModel<ScheduleModel>> response) {
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            ResponseArrayModel<ScheduleModel> model = response.body();
//                            Log_HR.log(Log_HR.LOG_INFO, FragmentFeed.class, "onResponse(Call<ResponseArrayModel<ScheduleModel>>, Response<ResponseArrayModel<ScheduleModel>>)", "is Success?" + scrollCount + " : " + (model.getSuccess() == CODE_SUCCESS));
                            if (model.getSuccess() == CODE_SUCCESS) {
                                HandlerManager.getInstance().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        feedItemModels.addAll(model.getResult());
                                        feedListAdapter.notifyDataSetChanged();
                                        ++scrollCount;
                                    }
                                });
                            }

                        } else {
                            Log_HR.log(Log_HR.LOG_ERROR, FragmentFeed.class, "onResponse(Call<ResponseArrayModel<ScheduleModel>>, Response<ResponseArrayModel<ScheduleModel>>)", "response is not Successful");
                            netFail();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseArrayModel<ScheduleModel>> call, Throwable t) {
                        Log_HR.log(FragmentFeed.class, "onFailure(Call<ResponseArrayModel<ScheduleModel>> ,Throwable)", "Fail", t);
                        netFail();
                    }
                });
            }
        });
    }

    private void netFail() {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentFeed_alert_title_fail, R.string.fragmentFeed_alert_content_fail);
    }

    @Override
    public void onStop() {
        super.onStop();
        scrollCount = 0;
        feedItemModels.clear();
    }

    private void loginFilter() {
        if (SessionManager.getInstance().isLogin()) {
            menuTopTitle.getIB_right().setVisibility(View.VISIBLE);
        } else {
            menuTopTitle.getIB_right().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Serializable serializable = null;
        int type = 0;
        if (requestCode == RESULT_CODE_SEARCH_CITY) {
            if (resultCode == RESULT_CODE_CITY_MODEL) {
                serializable = data.getSerializableExtra(KEY_CITYMODEL);
                type = FragmentFeedFilter.TYPE_CITY;
                FragmentManager.getInstance().setFragmentContent(FragmentFeedFilter.newInstance(type, serializable));
            } else if (resultCode == RESULT_CODE_SCHEDULE_MODEL) {
//                serializable = data.getSerializableExtra(KEY_SCHEDULE_MODEL);
                String nickName = data.getStringExtra(DataDefinition.Intent.KEY_NICKNAME);
                type = FragmentFeedFilter.TYPE_NICKNAME;
                FragmentManager.getInstance().setFragmentContent(FragmentFeedFilter.newInstance(type, nickName));
            }
        }
//        FragmentManager.getInstance().setFragmentContent(FragmentFeedFilter.newInstance(type, serializable));
    }
}
