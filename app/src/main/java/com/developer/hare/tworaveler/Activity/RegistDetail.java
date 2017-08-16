package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistDetail extends AppCompatActivity {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private String startDate, endDate;
    private final String title_format = "yy - MM - dd";
    private DateManager dateManager;
    private Intent intent;

    private com.prolificinteractive.materialcalendarview.MaterialCalendarView meterialCalendarView;
    private TextView TV_title, TV_date;
    private ImageView IV_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_detail);
        init();
    }

    protected void init() {
        intent = getIntent();
        startDate = intent.getExtras().getString(DataDefinition.Intent.KEY_STARTDATE);
        endDate = intent.getExtras().getString(DataDefinition.Intent.KEY_ENDDATE);

        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();

        TV_title = uiFactory.createView(R.id.activity_regist_detail$TV_title);
        TV_date = uiFactory.createView(R.id.activity_regist_detail$TV_date);
        IV_cover = uiFactory.createView(R.id.activity_regist_detail$IV_cover);

        menuTopTitle = uiFactory.createView(R.id.activity_regist_detail$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistDetail.this, RegistDayDetail.class);
                startActivity(intent);
            }
        });

        initMaterialCalendarView();
    }

    private void initMaterialCalendarView() {
        meterialCalendarView = uiFactory.createView(R.id.activity_regist_detail$calendar);
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
//                int[] arr = DateManager.getInstance().getTimeArr(date.getDate());
                Intent intent = new Intent(RegistDetail.this, RegistDayList.class);
                intent.putExtra(DataDefinition.Intent.KEY_DATE,DateManager.getInstance().formatDate(date.getDate().getTime(), DataDefinition.RegularExpression.FORMAT_DATE));
                startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_REGIST_DAY_LIST);
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
