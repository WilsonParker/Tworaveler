package com.developer.hare.tworaveler.Fragment.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Activity.RegistDayDetail;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.FragmentManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentRegistDetail extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private String startDate, endDate;
    private final String title_format = "yy - MM - dd";
    private DateManager dateManager;
    private Bundle bundle;

    private com.prolificinteractive.materialcalendarview.MaterialCalendarView meterialCalendarView;
    private TextView TV_title, TV_date;
    private ImageView IV_cover;

    public static FragmentRegistDetail newInstance(String startDate, String endDate) {
        FragmentRegistDetail f = new FragmentRegistDetail();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString(DataDefinition.Bundle.KEY_STARTDATE, startDate);
        args.putString(DataDefinition.Bundle.KEY_ENDDATE, endDate);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regist_detail, null);
    }

    @Override
    protected void init(View view) {
        bundle = getArguments();
        startDate = bundle.getString(DataDefinition.Bundle.KEY_STARTDATE);
        endDate = bundle.getString(DataDefinition.Bundle.KEY_ENDDATE);

        uiFactory = UIFactory.getInstance(view);
        dateManager = DateManager.getInstance();

        TV_title = uiFactory.createView(R.id.fragment_regist_detail$TV_title);
        TV_date = uiFactory.createView(R.id.fragment_regist_detail$TV_date);
        IV_cover = uiFactory.createView(R.id.fragment_regist_detail$IV_cover);

        menuTopTitle = uiFactory.createView(R.id.fragment_regist_detail$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegistDayDetail.class);
                startActivity(intent);
            }
        });

        initMaterialCalendarView();
    }

    private void initMaterialCalendarView() {
        meterialCalendarView = uiFactory.createView(R.id.fragment_regist_detail$calendar);
        Date startDate = DateManager.getInstance().parseDate(this.startDate, DataDefinition.RegularExpression.FORMAT_DATE);
        Date endDate = DateManager.getInstance().parseDate(this.endDate, DataDefinition.RegularExpression.FORMAT_DATE);
        meterialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(DataDefinition.RegularExpression.FORMAT_DATE)));
        int[] startArr = DateManager.getInstance().getTimeArr(startDate);
        int[] endArr = DateManager.getInstance().getTimeArr(endDate);
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initMaterialCalendarView()", "startArr: " + startArr[0]+" :  "+startArr[1]+" : "+startArr[2]);
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initMaterialCalendarView()", "endArr: " + endArr[0]+" :  "+endArr[1]+" : "+endArr[2]);

        meterialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONTH)
                .setMinimumDate(CalendarDay.from(startArr[0], startArr[1], startArr[2]))
                .setMaximumDate(CalendarDay.from(endArr[0], endArr[1], endArr[2]))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        meterialCalendarView.setCurrentDate(startDate);
        meterialCalendarView.selectRange(CalendarDay.from(startDate), CalendarDay.from(endDate));
        meterialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log_HR.log(Log_HR.LOG_INFO, FragmentRegistDetail.class, "initMaterialCalendarView()", "onClick: " + DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE));
                int[] arr = DateManager.getInstance().getTimeArr(date.getDate());
                Log_HR.log(Log_HR.LOG_INFO, FragmentRegistDetail.class, "initMaterialCalendarView()", "onClick: " + arr[0] + " : " + arr[1] + " : " + arr[2]);
                FragmentRegistDayList fragment = FragmentRegistDayList.newInstance(DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE));
                FragmentManager.getInstance().setFragmentContent(fragment);
            }
        });
        meterialCalendarView.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat(title_format)));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
//                CityModel model = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
            }
        }
    }
}