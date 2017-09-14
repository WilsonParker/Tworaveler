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
import com.developer.hare.tworaveler.Activity.MyScheduleModify;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.LikeModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_MYPAGE;
import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_SCHEDULE_MODEL;

public class FragmentMyPageSchedule extends BaseFragment {
    private UIFactory uiFactory;
    private ImageManager imageManager;
    private ScheduleModel scheduleModel;
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView materialCalendarView;
    private MenuTopTitle menuTopTitle;
    private TextView TV_title, TV_date, TV_like, TV_comment, TV_nickname, TV_message;
    private LinearLayout LL_like, LL_comment;
    private ImageView IV_cover, IV_like;
    private View scheduleItem;

    public static FragmentMyPageSchedule newInstance(ScheduleModel scheduleModel) {
        FragmentMyPageSchedule fragment = new FragmentMyPageSchedule();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SCHEDULE_MODEL, scheduleModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_page_schedule, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void init(View view) {
        Bundle bundle = getArguments();
        scheduleModel = (ScheduleModel) bundle.getSerializable(KEY_SCHEDULE_MODEL);

        uiFactory = UIFactory.getInstance(getActivity());
        imageManager = ImageManager.getInstance();

        scheduleItem = uiFactory.createViewWithRateParams(R.id.fragment_mypage_schedule$IC_schedul_item);
        menuTopTitle = uiFactory.createView(R.id.fragment_mypage_schedule$menuToptitle);
        menuTopTitle.setIBLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentMyPageHome.newInstance());
            }
        });
        menuTopTitle.setIBRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyScheduleModify.class);
                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, scheduleModel);
                startActivity(intent);
            }
        });
        uiFactory.setResource(scheduleItem);
        TV_title = uiFactory.createView(R.id.item_mypage$TV_title);
        TV_date = uiFactory.createView(R.id.item_mypage$TV_date);
        TV_like = uiFactory.createView(R.id.item_mypage$TV_like);
        TV_comment = uiFactory.createView(R.id.item_mypage$TV_comment);
        IV_cover = uiFactory.createView(R.id.item_mypage$IV_cover);
        IV_like = uiFactory.createView(R.id.item_mypage$IV_like);

        LL_like = uiFactory.createView(R.id.item_mypage$LL_like);
        LL_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeClick(scheduleModel.isLike());
            }
        });

        LL_comment = uiFactory.createView(R.id.item_mypage$LL_comment);
        LL_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Comment.class);
                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, scheduleModel);
                intent.putExtra(DataDefinition.Intent.KEY_STARTED_BY, KEY_MYPAGE);
                startActivity(intent);
            }
        });
        changeLike(scheduleModel.isLike());
        uiFactory.createView(R.id.item_mypage$IV_more).setVisibility(View.GONE);
        initMaterialCalendarView();

        ArrayList<TextView> textlist1 = new ArrayList<>();
        textlist1.add(TV_date);
        textlist1.add(TV_like);
        textlist1.add(TV_comment);
        FontManager.getInstance().setFont(textlist1, "Roboto-Medium.ttf");
        FontManager.getInstance().setFont(TV_title, "NotoSansKR-Bold-Hestia.otf");
    }

    // 캘린더 초기 세팅
    private void initMaterialCalendarView() {
        materialCalendarView = uiFactory.createView(R.id.fragment_mypage_schedule$calendar);
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
                FragmentManager.getInstance().setFragmentContent(FragmentMypageDetail.newInstance(scheduleModel, DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE)));
            }
        });
        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(DataDefinition.RegularExpression.FORMAT_DATE_REGIST_DETAIL)));
        materialCalendarView.setTopbarVisible(true);
    }

    // 초기 및 데이터가 변경 될 경우 View 에 데이터 세팅
    private void setData() {
        menuTopTitle.getTV_title().setText(SessionManager.getInstance().getUserModel().getNickname());
        TV_title.setText(scheduleModel.getTripName());
        TV_date.setText(scheduleModel.getStart_date() + " ~ " + scheduleModel.getEnd_date());
        TV_like.setText(scheduleModel.getLikeCount() + "");
        TV_comment.setText(scheduleModel.getCommentCount() + "");
        imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getTrip_pic_url(), ImageManager.FIT_TYPE), IV_cover);
        imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getTrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
    }

    // 좋아요 상태에 따라 (true or false) 좋아요 이미지를 변경
    private void changeLike(boolean isLike) {
        if (isLike) {
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.icon_heart_click, ImageManager.FIT_TYPE).centerCrop(), IV_like);
        } else {
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.icon_heart_unclick, ImageManager.FIT_TYPE).centerCrop(), IV_like);
        }
    }

    // 좋아요 클릭 했을 경우 실행
    private void likeClick(boolean isLike) {
        if (isLike) {
            Net.getInstance().getFactoryIm().modifyUnLike(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
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
                        LogManager.log(LogManager.LOG_INFO, FragmentMyPageSchedule.class, "onResponse", "onResponse is not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {
                    LogManager.log(FragmentMyPageSchedule.class, "onFailure(Call<ResponseArrayModel<LikeModel>> call, Throwable t)", t);
                }
            });
        } else {
            Net.getInstance().getFactoryIm().modifyLike(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
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
                        LogManager.log(LogManager.LOG_INFO, FragmentMyPageSchedule.class, "onResponse", "onResponse is not successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {
                    LogManager.log(FragmentMyPageSchedule.class, "onFailure(Call<ResponseArrayModel<LikeModel>> call, Throwable t)", t);
                }
            });
        }
        scheduleModel.setLike(!scheduleModel.isLike());
    }

}
