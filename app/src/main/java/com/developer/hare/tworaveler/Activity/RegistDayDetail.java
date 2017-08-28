package com.developer.hare.tworaveler.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.ResourceManager;

import java.util.ArrayList;

public class RegistDayDetail extends AppCompatActivity {
    private UIFactory uiFactory;
    private DateManager dateManager;
    private ResourceManager resourceManager;
    private String strDate;
    private Intent intent;
    private UserModel userModel;

    private MenuTopTitle menuTopTitle;
    private TextView TV_locationName, TV_locationSearch, TV_startTime, TV_endTime;
    private EditText ET_memo;

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_day_detail);
        init();
    }

    protected void init() {
        intent = getIntent();
        strDate = intent.getExtras().getString(DataDefinition.Intent.KEY_DATE);

        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();
        resourceManager = ResourceManager.getInstance();

        TV_locationName = uiFactory.createView(R.id.activity_regist_day_detail$TV_locationName);
        TV_locationSearch = uiFactory.createView(R.id.activity_regist_day_detail$TV_locationSearch);
        TV_startTime = uiFactory.createView(R.id.activity_regist_day_detail$TV_start);
        TV_endTime = uiFactory.createView(R.id.activity_regist_day_detail$TV_end);
        ET_memo = uiFactory.createView(R.id.activity_regist_day_detail$ET_meno);
        menuTopTitle = uiFactory.createView(R.id.activity_regist_day_detail$menuToptitle);
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_1));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_3));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_4));
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
        textViews.clear();
        textViews.add(TV_locationName);
        textViews.add(TV_locationSearch);
        textViews.add(TV_startTime);
        textViews.add(TV_endTime);
        textViews.add(ET_memo);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
    }

    private void onRegister() {
       /* if (!checkValidation())
            return;

        ScheduleDayModel model = new ScheduleDayModel("");
        if (imageFile != null) {
            MultipartBody.Part multipart = RetrofitBodyParser.getInstance().createImageMultipartBodyPart(DataDefinition.Key.KEY_USER_FILE, imageFile);

            Net.getInstance().getFactoryIm().insertSchedule(multipart, RetrofitBodyParser.getInstance().parseMapRequestBody(model)).enqueue(new Callback<ResponseModel<ScheduleModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response) {
                    if (response.isSuccessful()) {
                        ResponseModel<ScheduleModel> result = response.body();
                        Log_HR.log(Log_HR.LOG_INFO, Regist.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", "body : " + result.getSuccess());
                        Log_HR.log(Log_HR.LOG_INFO, Regist.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", "body : " + result.getMessage());
                        Log_HR.log(Log_HR.LOG_INFO, Regist.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", "body : " + result.getResult());
                        switch (result.getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                Intent intent = new Intent(getBaseContext(), RegistDetail.class);
                                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, result.getResult());
                                startActivity(intent);
                                break;
                        }
                    } else
                        netFail(R.string.regist_alert_title_fail, R.string.regist_alert_content_fail);
                }

                @Override
                public void onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t) {
                    Log_HR.log(Regist.class, "onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t)", t);
                    netFail(R.string.regist_alert_title_fail, R.string.regist_alert_content_fail_5);
                }
            });
        } else {
            Net.getInstance().getFactoryIm().insertSchedule(model).enqueue(new Callback<ResponseModel<ScheduleModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response) {
                    if (response.isSuccessful()) {
                        ResponseModel<ScheduleModel> result = response.body();
                        Log_HR.log(Log_HR.LOG_INFO, Regist.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", "body : " + result.getSuccess());
                        Log_HR.log(Log_HR.LOG_INFO, Regist.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", "body : " + result.getMessage());
                        Log_HR.log(Log_HR.LOG_INFO, Regist.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", "body : " + result.getResult());
                        switch (result.getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                Intent intent = new Intent(getBaseContext(), RegistDetail.class);
                                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, model);
                                startActivity(intent);
                                break;
                        }
                    } else
                        netFail(R.string.regist_alert_title_fail, R.string.regist_alert_content_fail);
                }

                @Override
                public void onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t) {
                    netFail(R.string.regist_alert_title_fail, R.string.regist_alert_content_fail_5);
                }
            });
        }*/
    }

 /*   private boolean checkValidation() {
        if(sessionCheck()){
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_3)).show();
            return false;
        }

        boolean result = false;
        String starDate = TV_dateStart.getText().toString();
        String endDate = TV_dateEnd.getText().toString();
        if (ET_tripName.getText().toString().isEmpty()) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_2)).show();
        } else if (TV_citySearch.getText().toString().isEmpty()) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_3)).show();
        } else if (!(starDate.matches(DataDefinition.RegularExpression.REG_DATE) && endDate.matches(DataDefinition.RegularExpression.REG_DATE))) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_4)).show();
        } else if (DateManager.getInstance().compareDate(starDate, endDate, DataDefinition.RegularExpression.REG_DATE)) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_4)).show();
        } else if (DateManager.getInstance().compareDate(starDate, endDate, DataDefinition.RegularExpression.FORMAT_DATE)) {
            result = true;
        }
        return result;
    }*/

    private boolean sessionCheck(){
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }

    public void searchCity(View view) {
        Intent intent = new Intent(this, SearchCity.class);
        startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
    }

    private void netFail(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(getBaseContext(), title, content);
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
                    if (model != null) {
//                        TV_citySearch.setText(model.getCountry() + " " + model.getCity());
//                        RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, model.getMain_pic_url(), ImageManager.PICTURE_TYPE).centerCrop();
//                        imageManager.loadImage(requestCreator, IV_cover);
//                        IV_camera.setVisibility(View.INVISIBLE);
                    }

                }
            }
        }
    }
}
