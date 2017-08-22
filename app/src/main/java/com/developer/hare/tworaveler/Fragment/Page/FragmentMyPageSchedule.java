package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.MyScheduleModify;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Model.ScheduleModel;
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

public class FragmentMyPageSchedule extends BaseFragment {
    private UIFactory uiFactory;
    private ImageManager imageManager;
    private DateManager dateManager;
    private ScheduleModel scheduleModel;
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView materialCalendarView;
    private MenuTopTitle menuTopTitle;
    private TextView TV_title, TV_date, TV_like, TV_comment;
    private ImageView IV_cover;
    private View scheduleItem;

    public static FragmentMyPageSchedule newInstance(ScheduleModel model) {
        FragmentMyPageSchedule fragment = new FragmentMyPageSchedule(model);
        return fragment;
    }

    public FragmentMyPageSchedule(ScheduleModel scheduleModel) {
        this.scheduleModel = scheduleModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page_schedule, container, false);
    }

    @Override
    protected void init(View view) {
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
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyScheduleModify.class));
            }
        });
        uiFactory.setResource(scheduleItem);
        TV_title = uiFactory.createView(R.id.item_mypage$TV_title);
        TV_date = uiFactory.createView(R.id.item_mypage$TV_date);
        TV_like = uiFactory.createView(R.id.item_mypage$TV_date);
        TV_comment = uiFactory.createView(R.id.item_mypage$TV_date);
        IV_cover = uiFactory.createView(R.id.item_mypage$IV_cover);
        uiFactory.createView(R.id.item_mypage$IV_more).setVisibility(View.GONE);
        initMaterialCalendarView();
        setDatas();
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

}
