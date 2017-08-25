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
import com.developer.hare.tworaveler.Model.Request.LikeModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Intent.KEY_SCHEDULE_MODEL;

public class FragmentMyPageSchedule extends BaseFragment {
//    private static FragmentMyPageSchedule fragment = new FragmentMyPageSchedule();
    private UIFactory uiFactory;
    private ImageManager imageManager;
    private DateManager dateManager;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page_schedule, container, false);
    }

    @Override
    protected void init(View view) {
        Bundle bundle = getArguments();
        scheduleModel = (ScheduleModel) bundle.getSerializable(KEY_SCHEDULE_MODEL);

        uiFactory = UIFactory.getInstance(getActivity());
        dateManager = DateManager.getInstance();
        imageManager = ImageManager.getInstance();

        scheduleItem = uiFactory.createView(R.id.fragment_mypage_schedule$IC_schedul_item);
        menuTopTitle = uiFactory.createView(R.id.fragment_mypage_schedule$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager.getInstance().setFragmentContent(FragmentMyPageHome.newInstance());
            }
        });
        uiFactory.setResource(scheduleItem);
        TV_title = uiFactory.createView(R.id.item_mypage$TV_title);
        TV_date = uiFactory.createView(R.id.item_mypage$TV_date);
        TV_like = uiFactory.createView(R.id.item_mypage$TV_like);
        TV_comment = uiFactory.createView(R.id.item_mypage$TV_comment);
        IV_cover = uiFactory.createView(R.id.item_mypage$IV_cover);
        IV_like = uiFactory.createView(R.id.item_mypage$IV_like);

        if (scheduleModel.isLike()) {
            IV_like.setImageResource(R.drawable.icon_heart_click);
        }
        TV_like.setText(scheduleModel.getLikeCount() + "");
        LL_like = uiFactory.createView(R.id.item_mypage$LL_like);
        LL_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeClick(scheduleModel.isLike());
            }
        });
        changeLike(scheduleModel.isLike());
        LL_comment = uiFactory.createView(R.id.item_mypage$LL_comment);
        LL_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Comment.class);
                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, scheduleModel);
                startActivity(intent);
            }
        });

        uiFactory.createView(R.id.item_mypage$IV_more).setVisibility(View.GONE);
        initMaterialCalendarView();
        setDatas();

        ImageManager imageManager = ImageManager.getInstance();
        imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getTrip_pic_url(), ImageManager.FIT_TYPE).centerCrop(), IV_cover);
        TV_title.setText(scheduleModel.getTripName()+"");
        TV_like.setText(scheduleModel.getLikeCount()+"");
        TV_comment.setText(scheduleModel.getCommentCount()+"");
        TV_date.setText(scheduleModel.getStart_date() + " ~ " + scheduleModel.getEnd_date());
    }

    private void initMaterialCalendarView() {
        materialCalendarView = uiFactory.createView(R.id.fragment_mypage_schedule$calendar);
        Date startDate = DateManager.getInstance().parseDate(scheduleModel.getStart_date(), DataDefinition.RegularExpression.FORMAT_DATE);
        Date endDate = DateManager.getInstance().parseDate(scheduleModel.getEnd_date(), DataDefinition.RegularExpression.FORMAT_DATE);
        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(DataDefinition.RegularExpression.FORMAT_DATE)));
        int[] startArr = DateManager.getInstance().getTimeArr(startDate);
        int[] endArr = DateManager.getInstance().getTimeArr(endDate);
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initMaterialCalendarView()", "startArr: " + startArr[0]+" :  "+startArr[1]+" : "+startArr[2]);
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initMaterialCalendarView()", "endArr: " + endArr[0]+" :  "+endArr[1]+" : "+endArr[2]);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONTH)
                .setMinimumDate(CalendarDay.from(startArr[0], startArr[1], startArr[2]))
                .setMaximumDate(CalendarDay.from(endArr[0], endArr[1], endArr[2]))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setCurrentDate(startDate);
        materialCalendarView.selectRange(CalendarDay.from(startDate), CalendarDay.from(endDate));
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                /*Intent intent = new Intent(FragmentMyPageSchedule.this, FragmentMypageDetail.class);
                intent.putExtra(DataDefinition.Intent.KEY_DATE,DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE));
                startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_REGIST_DAY_LIST);*/
                FragmentManager.getInstance().setFragmentContent(FragmentMypageDetail.newInstance(scheduleModel, DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE)));
            }
        });
        materialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(DataDefinition.RegularExpression.FORMAT_DATE_REGIST_DETAIL)));
        materialCalendarView.setTopbarVisible(true);
//        materialCalendarView.setBackgroundColor(Color.BLUE);
//        materialCalendarView.setBackgroundResource(R.drawable.background_materialcalendar);
    }

    private void setDatas() {
//        menuTopTitle.getTV_title().setText("");
        TV_title.setText(scheduleModel.getTripName());
        TV_date.setText(scheduleModel.getStart_date() + " ~ " + scheduleModel.getEnd_date());
        imageManager.loadImage(imageManager.createRequestCreator(getActivity(), scheduleModel.getTrip_pic_url(), ImageManager.FIT_TYPE), IV_cover);
    }

    private void changeLike(boolean isLike) {
        if (isLike) {
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.icon_heart_click, ImageManager.FIT_TYPE).centerCrop(), IV_like);
        } else {
            imageManager.loadImage(imageManager.createRequestCreator(getActivity(), R.drawable.icon_heart_unclick, ImageManager.FIT_TYPE).centerCrop(), IV_like);
        }
    }

    private void likeClick(boolean isLike) {
        if (isLike) {
            Net.getInstance().getFactoryIm().modifyUnLike(SessionManager.getInstance().getUserModel().getUser_no(), scheduleModel.getTrip_no()).enqueue(new Callback<ResponseModel<LikeModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<LikeModel>> call, Response<ResponseModel<LikeModel>> response) {
                    if (response.isSuccessful()) {
                        switch (response.body().getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                changeLike(false);
//                                    imageManager.loadImage(context, R.drawable.icon_heart_unclick, IV_like, ImageManager.FIT_TYPE);
//                                    imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.icon_heart_unclick, ImageManager.FIT_TYPE) .centerCrop(), IV_like);
                                int likeCount = scheduleModel.getLikeCount() - 1;
                                TV_like.setText("" + likeCount);
                                scheduleModel.setLikeCount(likeCount);
                                break;
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {

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
//                                    imageManager.loadImage(context, R.drawable.icon_heart_click, IV_like, ImageManager.FIT_TYPE);
//                                    imageManager.loadImage(imageManager.createRequestCreator(context, R.drawable.icon_heart_click, ImageManager.FIT_TYPE), IV_like);
                                int likeCount = scheduleModel.getLikeCount() + 1;
                                TV_like.setText("" + likeCount);
                                scheduleModel.setLikeCount(likeCount);
                                break;
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<LikeModel>> call, Throwable t) {

                }
            });
        }
        scheduleModel.setLike(!scheduleModel.isLike());
    }

}
