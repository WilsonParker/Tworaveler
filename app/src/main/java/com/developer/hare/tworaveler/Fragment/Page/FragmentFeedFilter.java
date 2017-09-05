package com.developer.hare.tworaveler.Fragment.Page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Adapter.FeedCityListAdapter;
import com.developer.hare.tworaveler.Adapter.FeedNicknameListAdapter;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentFeed;
import com.developer.hare.tworaveler.Listener.OnListScrollListener;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.FollowModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Exception.NullChecker;
import com.developer.hare.tworaveler.Util.HandlerManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private ScheduleModel scheduleModel;
    private UserModel userModel;

    private CircleImageView CV_profile;
    private TextView TV_nickname, TV_message;
    private ImageView IV_follow;
    private LinearLayout LL_info, LL_feed;

    private UIFactory uiFactory;
    private ArrayList<ScheduleModel> feedICityModels = new ArrayList<>();
    private ProgressManager progressManager;
    private int scrollCount = 0, type;
    public static final int TYPE_CITY = 0x0001;
    public static final int TYPE_NICKNAME = 0x0010;


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

        if (type == TYPE_CITY) {
            cityModel = (CityModel) getArguments().getSerializable(KEY_SERIALIZABLE);
        } else if (type == TYPE_NICKNAME) {
            scheduleModel = (ScheduleModel) getArguments().getSerializable(KEY_SERIALIZABLE);
        }else{
            FragmentManager.getInstance().setFragmentContent(FragmentFeed.newInstance());
        }

        uiFactory = UIFactory.getInstance(view);
        progressManager = new ProgressManager(getActivity());
        LL_feed = uiFactory.createView(R.id.fragment_feed$LL_feed);
        LL_feed.setVisibility(View.GONE);
        menuTopTitle = uiFactory.createView(R.id.fragment_feed$menuToptitle);
        menuTopTitle.setVisibility(View.VISIBLE);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentFeed.newInstance());
            }
        });
        menuTopTitle.getIB_right().setVisibility(View.INVISIBLE);
        recyclerView = uiFactory.createView(R.id.fragment_feed$RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        LL_info = uiFactory.createView(R.id.fragment_feed$LL_info);
        CV_profile = uiFactory.createView(R.id.fragment_feed$CV_profile);
        TV_nickname = uiFactory.createView(R.id.fragment_feed$TV_nickname);
        TV_message = uiFactory.createView(R.id.fragment_feed$TV_message);
        IV_follow = uiFactory.createView(R.id.fragment_feed$IV_follow);
        FontManager.getInstance().setFont(TV_nickname, "Roboto-Bold.ttf");
        FontManager.getInstance().setFont(TV_message, "NotoSansCJKkr-Medium.otf");

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
        if (!sessionCheck()){
            AlertManager.getInstance().showNotLoginAlert(getActivity(), R.string.fragmentFeed_alert_title_fail);
            return;
        }
        int user_no = userModel.getUser_no();
        switch (type) {
            case TYPE_CITY:
                menuTopTitle.getTV_title().setText(cityModel.getCity());
                progressManager.actionWithState(new OnProgressAction() {
                    @Override
                    public void run() {
                        Net.getInstance().getFactoryIm().searchFeedCity(user_no, cityModel.getCity()).enqueue(new Callback<ResponseArrayModel<ScheduleModel>>() {
                            @Override
                            public void onResponse(Call<ResponseArrayModel<ScheduleModel>> call, Response<ResponseArrayModel<ScheduleModel>> response) {
                                if (response.isSuccessful()) {
//                                   Log_HR.log(FragmentFeedFilter.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
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
            case TYPE_NICKNAME:
                LL_info.setVisibility(View.VISIBLE);
                TV_nickname.setText(scheduleModel.getNickname() + "");
                TV_message.setText(scheduleModel.getStatus_message() + "");
                Log_HR.log(Log_HR.LOG_INFO, FragmentFeedFilter.class, "데이터 확인", "nickname :" + scheduleModel.toString());
                ImageManager imageManager = ImageManager.getInstance();
                if (!NullChecker.getInstance().nullCheck(scheduleModel.getProfile_pic_thumbnail_url()))
                    imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getProfile_pic_thumbnail_url(), ImageManager.FIT_TYPE).placeholder(R.drawable.image_profile).centerCrop(), CV_profile);
                else
                    imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.image_profile, ImageManager.BASIC_TYPE), CV_profile);
                changeFollow(scheduleModel.isFollow());
                IV_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followSelect(scheduleModel.isFollow());
                    }
                });
                menuTopTitle.getTV_title().setText(scheduleModel.getNickname());
                progressManager.actionWithState(new OnProgressAction() {
                    @Override
                    public void run() {
                        Net.getInstance().getFactoryIm().searchFeedNickname(user_no, scheduleModel.getUser_no()).enqueue(new Callback<ResponseArrayModel<ScheduleModel>>() {
                            @Override
                            public void onResponse(Call<ResponseArrayModel<ScheduleModel>> call, Response<ResponseArrayModel<ScheduleModel>> response) {
                            Log_HR.log(Log_HR.LOG_INFO, FragmentFeedFilter.class, "데이터 확인", "nickname :" + scheduleModel.toString());
                                // Log_HR.log(FragmentFeedFilter.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                                if (response.isSuccessful()) {
                                    progressManager.endRunning();
                                    ResponseArrayModel<ScheduleModel> model = response.body();
                                    if (model.getSuccess() == CODE_SUCCESS) {
                                        nicknameListAdapter = new FeedNicknameListAdapter(model.getResult(), getActivity());
                                        recyclerView.setAdapter(nicknameListAdapter);
                                    } else {
                                        Log_HR.log(Log_HR.LOG_ERROR, FragmentFeedFilter.class, "model.getSuccess() != CODE_SUCCESS", "response is not Successful");
                                        netFail();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseArrayModel<ScheduleModel>> call, Throwable t) {
                                Log_HR.log(Log_HR.LOG_ERROR, FragmentFeedFilter.class, "onFailure", "통신 실패" + t);
                                netFail();
                            }
                        });
                    }
                });
                break;
        }
    }
    public void changeFollow(boolean isFollow) {
        if (isFollow) {
            ImageManager.getInstance().loadImage(getActivity(), R.drawable.icon_follow_complete, IV_follow, ImageManager.FIT_TYPE);
        } else {
            ImageManager.getInstance().loadImage(getActivity(), R.drawable.icon_follow, IV_follow, ImageManager.FIT_TYPE);
        }
    }
    public void followSelect(boolean isFollow) {
        if (!sessionCheck()) {
            AlertManager.getInstance().showNotLoginAlert(getActivity(), R.string.fragmentFeedSchedule_alert_title_fail);
            return;
        }
        if (isFollow) {
            Net.getInstance().getFactoryIm().selectUnFollow(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getUser_no()).enqueue(new Callback<ResponseModel<FollowModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<FollowModel>> call, Response<ResponseModel<FollowModel>> response) {
                    //                    Log_HR.log(FragmentFeedSchedule.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                            Log_HR.log(Log_HR.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "isunFollow : " + scheduleModel.isFollow());
                            changeFollow(false);
                        }
                    } else
                        Log_HR.log(Log_HR.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "onResponse is not successful");
                }

                @Override
                public void onFailure(Call<ResponseModel<FollowModel>> call, Throwable t) {
                    Log_HR.log(FragmentFeedSchedule.class, "onFailure", t);
                    netFail(R.string.fragmentFeed_schedule_alert_title_fail, R.string.fragmentFeed_schedule_alert_content_fail);
                }
            });
        } else {
            Net.getInstance().getFactoryIm().selectFollow(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getUser_no()).enqueue(new Callback<ResponseModel<FollowModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<FollowModel>> call, Response<ResponseModel<FollowModel>> response) {
                    //                    Log_HR.log(FragmentFeedSchedule.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                            Log_HR.log(Log_HR.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "isFollow : " + scheduleModel.isFollow());
                            changeFollow(true);
                        }
                    } else
                        Log_HR.log(Log_HR.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "onResponse is not successful");
                }

                @Override
                public void onFailure(Call<ResponseModel<FollowModel>> call, Throwable t) {
                    Log_HR.log(FragmentFeedSchedule.class, "onFailure", t);
                    netFail(R.string.fragmentFeed_schedule_alert_title_fail, R.string.fragmentFeed_schedule_alert_content_fail);
                }
            });
        }
        scheduleModel.setFollow(!scheduleModel.isFollow());
    }

    private void netFail(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(getActivity(), title, content);
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
