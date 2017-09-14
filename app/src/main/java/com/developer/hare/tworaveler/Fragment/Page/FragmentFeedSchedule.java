package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.Comment;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Fragment.Menu.FragmentFeed;
import com.developer.hare.tworaveler.Model.FollowModel;
import com.developer.hare.tworaveler.Model.LikeModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Exception.NullChecker;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.LogManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_FEED;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_SCHEDULE_MODEL;

public class FragmentFeedSchedule extends BaseFragment {
    //    private static FragmentFeedSchedule fragment = new FragmentFeedSchedule();
    private static ScheduleModel scheduleModel;
    private UIFactory uiFactory;
    private ImageManager imageManager;
    private UserModel userModel;

    private MaterialCalendarView materialCalendarView;
    private MenuTopTitle menuTopTitle;
    private TextView TV_title, TV_date, TV_like, TV_comment, TV_nickname, TV_message;
    private ImageView IV_cover, IV_like, IV_follow;
    private View scheduleItem;
    private CircleImageView CV_profile;
    private LinearLayout LL_like, LL_comment, LL_profile;

    public static FragmentFeedSchedule newInstance(ScheduleModel scheduleModel) {
        FragmentFeedSchedule fragmentFeedSchedule = new FragmentFeedSchedule();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SCHEDULE_MODEL, scheduleModel);
        fragmentFeedSchedule.setArguments(bundle);
        FragmentFeedSchedule.scheduleModel = scheduleModel;
        return fragmentFeedSchedule;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_schedule, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        setInitArg(myInstance);
        setDatas();
    }

    @Override
    protected void init(View view) {
        Bundle bundle = getArguments();
        scheduleModel = (ScheduleModel) bundle.getSerializable(KEY_SCHEDULE_MODEL);
//        LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "scheduleModel", scheduleModel.toString());
        uiFactory = UIFactory.getInstance(getActivity());
        imageManager = ImageManager.getInstance();
        scheduleItem = uiFactory.createViewWithRateParams(R.id.fragment_feed_schedule$IC_schedul_item);
        CV_profile = uiFactory.createView(R.id.fragment_feed_schedule$CV_profile);
        LL_profile = uiFactory.createView(R.id.fragment_feed_schedule$LL_profile);
        LL_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentFeedProfile.newInstance(scheduleModel.getUser_no()));
            }
        });
        menuTopTitle = uiFactory.createView(R.id.fragment_feed_schedule$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentFeed.newInstance());
            }
        });
        uiFactory.setResource(scheduleItem);
        TV_title = uiFactory.createView(R.id.item_mypage$TV_title);
        TV_date = uiFactory.createView(R.id.item_mypage$TV_date);
        TV_like = uiFactory.createView(R.id.item_mypage$TV_like);
        TV_comment = uiFactory.createView(R.id.item_mypage$TV_comment);
        IV_cover = uiFactory.createView(R.id.item_mypage$IV_cover);
        LL_like = uiFactory.createView(R.id.item_mypage$LL_like);
        LL_comment = uiFactory.createView(R.id.item_mypage$LL_comment);
        IV_like = uiFactory.createView(R.id.item_mypage$IV_like);
        IV_follow = uiFactory.createView(R.id.fragment_feed_schedule$IV_follow);
        TV_nickname = uiFactory.createView(R.id.fragment_feed_schedule$TV_nickname);
        TV_message = uiFactory.createView(R.id.fragment_feed_schedule$TV_message);
        uiFactory.createView(R.id.item_mypage$IV_more).setVisibility(View.GONE);
        initMaterialCalendarView();
        setDatas();

        menuTopTitle.setTitleText(scheduleModel.getNickname());
        menuTopTitle.setTitleFont("NotoSansKR-Regular-Hestia.otf");

        ArrayList<TextView> textlist1 = new ArrayList<>();
        textlist1.add(TV_date);
//        textlist1.add(TV_nickname);
        textlist1.add(TV_like);
        textlist1.add(TV_comment);
        FontManager.getInstance().setFont(textlist1, "Roboto-Medium.ttf");
        FontManager.getInstance().setFont(TV_nickname, "Roboto-Bold.ttf");
        FontManager.getInstance().setFont(TV_message, "NotoSansKR-Medium-Hestia.otf");
        FontManager.getInstance().setFont(TV_title, "NotoSansKR-Bold-Hestia.otf");

        LL_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeClick(scheduleModel.isLike());
            }
        });
        changeLike(scheduleModel.isLike());
        LL_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Comment.class);
                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, scheduleModel);
                intent.putExtra(DataDefinition.Intent.KEY_STARTED_BY, KEY_FEED);
                startActivity(intent);
            }
        });
        if (sessionCheck()) {
            if (userModel.getUser_no() == scheduleModel.getUser_no()) {
                IV_follow.setVisibility(View.INVISIBLE);
            }
        }
        IV_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followSelect(scheduleModel.isFollow());
            }
        });
        changeFollow(scheduleModel.isFollow());
        TV_nickname.setText(scheduleModel.getNickname() + "");
        TV_message.setText(scheduleModel.getStatus_message() + "");
        TV_title.setText(scheduleModel.getTripName() + "");
        TV_like.setText(scheduleModel.getLikeCount() + "");
        TV_comment.setText(scheduleModel.getCommentCount() + "");
        TV_date.setText(scheduleModel.getStart_date() + " ~ " + scheduleModel.getEnd_date());
    }

    private void initMaterialCalendarView() {
        materialCalendarView = uiFactory.createView(R.id.fragment_feed_schedule$calendar);
        Date startDate = DateManager.getInstance().parseDate(scheduleModel.getStart_date(), DataDefinition.RegularExpression.FORMAT_DATE);
        Date endDate = DateManager.getInstance().parseDate(scheduleModel.getEnd_date(), DataDefinition.RegularExpression.FORMAT_DATE);
        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(DataDefinition.RegularExpression.FORMAT_DATE)));
        int[] startArr = DateManager.getInstance().getTimeArr(startDate);
        int[] endArr = DateManager.getInstance().getTimeArr(endDate);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONTH)
                .setMinimumDate(CalendarDay.from(startArr[0], startArr[1] - 1, startArr[2]))
                .setMaximumDate(CalendarDay.from(endArr[0], endArr[1] - 1, endArr[2]))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setCurrentDate(startDate);
        materialCalendarView.selectRange(CalendarDay.from(startDate), CalendarDay.from(endDate));
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String trip_date = DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE);
                FragmentManager.getInstance().setFragmentContent(FragmentFeedDetail.newInstance(scheduleModel, trip_date));
            }
        });
        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(DataDefinition.RegularExpression.FORMAT_DATE_REGIST_DETAIL)));
        materialCalendarView.setTopbarVisible(true);
    }

    private void setDatas() {
        TV_title.setText(scheduleModel.getTripName());
        TV_date.setText(scheduleModel.getStart_date() + " ~ " + scheduleModel.getEnd_date());
        TV_comment.setText(scheduleModel.getCommentCount() + "");
        if (!NullChecker.getInstance().nullCheck(scheduleModel.getTrip_pic_url()))
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getTrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
        if (!NullChecker.getInstance().nullCheck(scheduleModel.getProfile_pic_thumbnail_url()))
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getProfile_pic_thumbnail_url(), ImageManager.FIT_TYPE).placeholder(R.drawable.image_profile).centerCrop(), CV_profile);
        else
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.image_profile, ImageManager.BASIC_TYPE), CV_profile);
    }

    private void changeLike(boolean isLike) {
        if (isLike) {
            imageManager.loadImage(getActivity(), R.drawable.icon_heart_click, IV_like, ImageManager.FIT_TYPE);
        } else {
            imageManager.loadImage(getActivity(), R.drawable.icon_heart_unclick, IV_like, ImageManager.FIT_TYPE);
        }
    }

    private void likeClick(boolean isLike) {
        if (!sessionCheck()) {
            AlertManager.getInstance().showNotLoginAlert(getActivity(), R.string.fragmentFeedSchedule_alert_title_fail);
            return;
        }

        if (isLike) {
            Net.getInstance().getFactoryIm().modifyUnLike(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
//                    LogManager.log(FragmentFeedSchedule.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                    if (response.isSuccessful()) {
                        switch (response.body().getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                changeLike(false);
                                int likeCount = scheduleModel.getLikeCount() - 1;
                                TV_like.setText("" + likeCount);
                                scheduleModel.setLikeCount(likeCount);
                                break;

                        }
                    } else {
                        LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "onResponse is not successful");
                        netFail(R.string.fragmentFeed_schedule_alert_title_like_fail, R.string.fragmentFeed_schedule_alert_content_like_content_fail);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {
                    LogManager.log(FragmentFeedSchedule.class, "onFailure", t);
                    netFail(R.string.fragmentFeed_schedule_alert_title_like_fail, R.string.fragmentFeed_schedule_alert_content_like_content_fail);
                }
            });
        } else {
            Net.getInstance().getFactoryIm().modifyLike(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
                    //                    LogManager.log(FragmentFeedSchedule.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                    if (response.isSuccessful()) {
                        switch (response.body().getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                changeLike(true);
                                int likeCount = scheduleModel.getLikeCount() + 1;
                                TV_like.setText("" + likeCount);
                                scheduleModel.setLikeCount(likeCount);
                                break;
                        }
                    } else {
                        netFail(R.string.fragmentFeed_schedule_alert_title_like_fail, R.string.fragmentFeed_schedule_alert_content_like_content_fail);
                        LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "onResponse is not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {
                    netFail(R.string.fragmentFeed_schedule_alert_title_like_fail, R.string.fragmentFeed_schedule_alert_content_like_content_fail);
                    LogManager.log(FragmentFeedSchedule.class, "onFailure(Call<ResponseArrayModel<CommentModel>> call, Throwable t)", t);
                }
            });
        }
        scheduleModel.setLike(!scheduleModel.isLike());
    }

    public void changeFollow(boolean isFollow) {
        if (isFollow) {
            ImageManager.getInstance().loadImage(getActivity(), R.drawable.icon_follow_complete, IV_follow, ImageManager.FIT_TYPE);
        } else {
            ImageManager.getInstance().loadImage(getActivity(), R.drawable.icon_follow, IV_follow, ImageManager.FIT_TYPE);
        }
    }

    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
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
                    //                    LogManager.log(FragmentFeedSchedule.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                            LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "isunFollow : " + scheduleModel.isFollow());
                            changeFollow(false);
                        }
                    } else
                        LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "onResponse is not successful");
                }

                @Override
                public void onFailure(Call<ResponseModel<FollowModel>> call, Throwable t) {
                    LogManager.log(FragmentFeedSchedule.class, "onFailure", t);
                    netFail(R.string.fragmentFeed_schedule_alert_title_fail, R.string.fragmentFeed_schedule_alert_content_fail);
                }
            });
        } else {
            Net.getInstance().getFactoryIm().selectFollow(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getUser_no()).enqueue(new Callback<ResponseModel<FollowModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<FollowModel>> call, Response<ResponseModel<FollowModel>> response) {
                    //                    LogManager.log(FragmentFeedSchedule.class, "onResponse(Call<ResponseArrayModel<String>> call, Response<ResponseArrayModel<String>> response)", response);
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                            LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "isFollow : " + scheduleModel.isFollow());
                            changeFollow(true);
                        }
                    } else
                        LogManager.log(LogManager.LOG_INFO, FragmentFeedSchedule.class, "onResponse", "onResponse is not successful");
                }

                @Override
                public void onFailure(Call<ResponseModel<FollowModel>> call, Throwable t) {
                    LogManager.log(FragmentFeedSchedule.class, "onFailure", t);
                    netFail(R.string.fragmentFeed_schedule_alert_title_fail, R.string.fragmentFeed_schedule_alert_content_fail);
                }
            });
        }
        scheduleModel.setFollow(!scheduleModel.isFollow());
    }

    private void netFail(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(getActivity(), title, content);
    }

}
