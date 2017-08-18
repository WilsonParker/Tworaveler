package com.developer.hare.tworaveler.Fragment.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.hare.tworaveler.Activity.SearchCity;
import com.developer.hare.tworaveler.Adapter.FeedListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.FeedItemModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class FragmentFeed extends BaseFragment {
    private static FragmentFeed instance = new FragmentFeed();
    private FeedListAdapter feedListAdapter;
    private RecyclerView recyclerView;
    private MenuTopTitle menuTopTitle;

    private UIFactory uiFactory;
    private ArrayList<FeedItemModel> feedItemModels = new ArrayList<>();
    private ProgressManager progressManager;
    private int scrollCount = 0;
    private final int ItemsSize = 5;

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
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "init()", "init running");
        uiFactory = UIFactory.getInstance(view);
        progressManager = new ProgressManager(getActivity());
        menuTopTitle = uiFactory.createView(R.id.fragment_feed$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchCity.class);
                startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
            }
        });

        recyclerView = uiFactory.createView(R.id.fragment_feed$RV);
        feedListAdapter = new FeedListAdapter(feedItemModels, new OnListScrollListener() {
            @Override
            public void scrollEnd() {
                Log_HR.log(Log_HR.LOG_INFO, FragmentFeed.class, "scroolEnd()", "values : " + feedItemModels.size() + " : " + (ItemsSize * (scrollCount + 1)) + " : " + (feedItemModels.size() == ItemsSize * (scrollCount + 1)));
                if (feedItemModels.size() == ItemsSize * scrollCount) {
                    Log_HR.log(Log_HR.LOG_INFO, FragmentFeed.class, "scrollEnd()", "update");
                    updateList();
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedListAdapter);
        updateList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                if (data != null) {
                    CityModel model = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
                }
            }
        }
    }

    private void updateList() {
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                Call<RequestArrayModel<FeedItemModel>> result = Net.getInstance().getFactoryIm().selectFeedList(scrollCount);
                result.enqueue(new Callback<RequestArrayModel<FeedItemModel>>() {
                    @Override
                    public void onResponse(Call<RequestArrayModel<FeedItemModel>> call, Response<RequestArrayModel<FeedItemModel>> response) {
                        if (response.isSuccessful()) {
                            RequestArrayModel<FeedItemModel> model = response.body();
                            Log_HR.log(Log_HR.LOG_INFO, FragmentFeed.class, "onResponse(Call<RequestArrayModel<FeedItemModel>>, Response<RequestArrayModel<FeedItemModel>>)", "is Success?" + scrollCount + " : " + (model.getSuccess() == CODE_SUCCESS));
                            if (model.getSuccess() == CODE_SUCCESS) {
                                HandlerManager.getInstance().getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressManager.endRunning();
                                        feedItemModels.addAll(model.getResult());
                                        ++scrollCount;
                                        Log_HR.log(Log_HR.LOG_INFO, FragmentFeed.class, "onResponse(Call<RequestArrayModel<FeedItemModel>>, Response<RequestArrayModel<FeedItemModel>>)", "items size : " + feedItemModels.size());
                                        feedListAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                        } else {
                            Log_HR.log(Log_HR.LOG_ERROR, FragmentFeed.class, "onResponse(Call<RequestArrayModel<FeedItemModel>>, Response<RequestArrayModel<FeedItemModel>>)", "response is not Successful");
                            netFail();
                        }

                    }

                    @Override
                    public void onFailure(Call<RequestArrayModel<FeedItemModel>> call, Throwable t) {
                        Log_HR.log(FragmentFeed.class, "onFailure(Call<RequestArrayModel<FeedItemModel>> ,Throwable)", "Fail", t);
                        netFail();
                    }
                });
            }
        });
    }

    private void netFail() {
        AlertManager.getInstance().showNetFailAlert(getActivity(), R.string.fragmentFeed_fail_alert_title_fail, R.string.fragmentFeed_fail_alert_content_fail);
    }

}
