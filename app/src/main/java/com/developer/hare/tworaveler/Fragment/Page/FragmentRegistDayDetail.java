package com.developer.hare.tworaveler.Fragment.Page;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.hare.tworaveler.Activity.RegistDayDetail;
import com.developer.hare.tworaveler.Activity.SearchCity;
import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Fragment.BaseFragment;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentRegistDayDetail extends BaseFragment {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private String startDate, endDate;
    private DateManager dateManager;
    private Bundle bundle;

    private com.prolificinteractive.materialcalendarview.MaterialCalendarView meterialCalendarView;
    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover;
//    private CalendarView calendar;

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_regist$TV_start:
                    dateManager.getDateTime(getActivity(), TV_dateStart);
                    break;
                case R.id.fragment_regist$TV_end:
                    dateManager.getDateTime(getActivity(), TV_dateEnd);
                    break;
                case R.id.fragment_regist$TV_citySearch:
                    Intent intent = new Intent(getActivity(), SearchCity.class);
                    startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
                    break;
                case R.id.fragment_regist$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.ic_camera_alt_black_24dp, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(getActivity(), new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager.getInstance().loadImage(getActivity(), fileData.getFile(), IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                }
                            });
                        }
                    }));
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.ic_filter_black_24dp, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onGallerySingleSelect(getActivity(), new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager.getInstance().loadImage(getActivity(), fileData.getFile(), IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                }
                            });
                        }
                    }));
                    AlertManager.getInstance().showAlertSelectionMode(getActivity(), "등록 방법 선택", 3, AlertSelectionItemModels).show();
                    break;
            }
        }
    };


    public static FragmentRegistDayDetail newInstance(String startDate, String endDate) {
        FragmentRegistDayDetail f = new FragmentRegistDayDetail();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString(DataDefinition.Bundle.KEY_STARTDATE, startDate);
        args.putString(DataDefinition.Bundle.KEY_ENDDATE, endDate);
        f.setArguments(args);
        return f;
    }


    public FragmentRegistDayDetail() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regist_detail, null);
    }

    @Override
    protected void init(View view) {
//        startDate = (String) getActivity().getIntent().getExtras().get(DataDefinition.Intent.KEY_STARTDATE);
//        endDate = (String) getActivity().getIntent().getExtras().get(DataDefinition.Intent.KEY_ENDDATE);
        bundle = getArguments();
        startDate = bundle.getString(DataDefinition.Bundle.KEY_STARTDATE);
        endDate = bundle.getString(DataDefinition.Bundle.KEY_ENDDATE);

        uiFactory = UIFactory.getInstance(view);
        dateManager = DateManager.getInstance();

        menuTopTitle = uiFactory.createView(R.id.fragment_regist$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegistDayDetail.class);
                startActivity(intent);
            }
        });

//        calendarPickerView = uiFactory.createView(R.id.fragment_regist_detail$calendar_view);
//        initCalendarPicker();
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
    }


   /* private void initCalendarPicker() {
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initCalendarPicker()", "startDate : " + startDate);
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initCalendarPicker()", "endDate : " + endDate);
        Date today = DateManager.getInstance().parseDate(startDate, DataDefinition.RegularExpression.FORMAT_DATE);
        Date nextDate = DateManager.getInstance().parseDate(endDate, DataDefinition.RegularExpression.FORMAT_DATE);
        nextDate.setTime(nextDate.getTime() + 100000000);
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initCalendarPicker()", "date :  " + nextDate.getTime());
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "initCalendarPicker()", "date :  " + DateManager.getInstance().formatDate(nextDate.getTime()+100000000, DataDefinition.RegularExpression.FORMAT_DATE));

        calendarPickerView.init(today, nextDate).withSelectedDate(today);
        calendarPickerView.init(today, nextDate).inMode(MULTIPLE);
        calendarPickerView.setCustomDayView(new DayViewAdapter() {
            @Override
            public void makeCellView(CalendarCellView parent) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.calendar_dayview, null);
                TextView TV_day = UIFactory.getInstance(view).createView(R.id.calendar_dayview$TV);
                parent.setDayOfMonthTextView(TV_day);
                parent.setBackgroundColor(Color.BLACK);
            }
        });

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
//                Log_HR.log(Log_HR.LOG_INFO, getClass(), "initCalendarPicker()", "onDateSelected(Date) " + DateManager.getInstance().formatDate(date.getTime(), DataDefinition.RegularExpression.FORMAT_DATE));
            }

            @Override
            public void onDateUnselected(Date date) {
//                Log_HR.log(Log_HR.LOG_INFO, getClass(), "initCalendarPicker()", "onDateUnselected(Date) ");

            }
        });

//        calendarPickerView.setAdapter(new );
//        calendarPickerView.setLayoutParams(new ViewGroup.LayoutParams(getActivity(), new Att));
    }
    */

    private void onRegist() {
//        getFragmentManager().beginTransaction().replace(R.id.main$FL_content, new FragmentPeed()).addToBackStack(null).commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                CityModel model = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
                TV_citySearch.setText(model.getCityName());
            }
        }
    }
}