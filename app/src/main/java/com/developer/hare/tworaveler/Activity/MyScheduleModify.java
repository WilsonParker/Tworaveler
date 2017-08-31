package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.Parser.RetrofitBodyParser;
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyScheduleModify extends AppCompatActivity {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private DateManager dateManager;
    private ResourceManager resourceManager;

    private UserModel userModel;
    private CityModel cityModel;
    private EditText ET_tripName;
    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover, IV_camera;
    private ScheduleModel scheduleModel;
    private File imageFile;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_myschedule_modify$TV_start:
                    dateManager.getDateYMD(MyScheduleModify.this, TV_dateStart);
                    break;
                case R.id.activity_myschedule_modify$TV_end:
                    dateManager.getDateYMD(MyScheduleModify.this, TV_dateEnd);
                    break;
                case R.id.activity_myschedule_modify$TV_citySearch:
                    Intent intent = new Intent(MyScheduleModify.this, SearchCity.class);
                    startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
                    break;
                case R.id.activity_myschedule_modify$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.icon_camera_2, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(MyScheduleModify.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    bindImage(fileData.getFile());
                                }
                            });
                        }
                    }));
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.icon_gallery, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onGallerySingleSelect(MyScheduleModify.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    bindImage(fileData.getFile());
                                }
                            });
                        }
                    }));
                    AlertManager.getInstance().showAlertSelectionMode(MyScheduleModify.this, "등록 방법 선택", 2, AlertSelectionItemModels).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myschedule_modify);
        init();
    }

    protected void init() {
        scheduleModel = (ScheduleModel) getIntent().getSerializableExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL);
        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();
        resourceManager = ResourceManager.getInstance();

        sessionCheck();
        menuTopTitle = uiFactory.createView(R.id.activity_myschedule_modify$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyScheduleModify.this.onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onModify();
            }
        });
        ET_tripName = uiFactory.createView(R.id.activity_myschedule_modify$TV_tripName);
        TV_citySearch = uiFactory.createView(R.id.activity_myschedule_modify$TV_citySearch);
        TV_citySearch.setOnClickListener(onClickListener);
        TV_dateStart = uiFactory.createView(R.id.activity_myschedule_modify$TV_start);
        TV_dateStart.setOnClickListener(onClickListener);
        TV_dateEnd = uiFactory.createView(R.id.activity_myschedule_modify$TV_end);
        TV_dateEnd.setOnClickListener(onClickListener);
        IV_cover = uiFactory.createView(R.id.activity_myschedule_modify$IV_cover);
        IV_cover.setOnClickListener(onClickListener);
        IV_camera = uiFactory.createView(R.id.activity_myschedule_modify$IV_camera);

        ImageManager.getInstance().loadImage(this, scheduleModel.getTrip_pic_url(), IV_cover, ImageManager.FIT_TYPE);
        ET_tripName.setText(scheduleModel.getTripName());
        TV_citySearch.setText(scheduleModel.getCity());
        TV_dateStart.setText(scheduleModel.getStart_date());
        TV_dateEnd.setText(scheduleModel.getEnd_date());

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.activity_myschedule_modify$TV_txt_1));
        textViews.add(uiFactory.createView(R.id.activity_myschedule_modify$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.activity_myschedule_modify$TV_txt_3));
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
        textViews.clear();
        textViews.add(ET_tripName);
        textViews.add(TV_citySearch);
        textViews.add(TV_dateStart);
        textViews.add(TV_dateEnd);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
    }

    private void onModify() {
        if (!checkValidation())
            return;

        ScheduleModel model;
        if (cityModel != null)
            model = new ScheduleModel(userModel, cityModel, scheduleModel.getTrip_no(), TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), ET_tripName.getText().toString());
        else
            model = new ScheduleModel(userModel, scheduleModel.getTrip_no(), scheduleModel.getCountry(), scheduleModel.getCity(), TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), ET_tripName.getText().toString());
        if (imageFile != null) {
            RetrofitBodyParser retrofitBodyParser = RetrofitBodyParser.getInstance();
            Net.getInstance().getFactoryIm().modifySchedule(retrofitBodyParser.createImageMultipartBodyPart(DataDefinition.Key.KEY_USER_FILE, imageFile), retrofitBodyParser.parseMapRequestBody(model)).enqueue(new Callback<ResponseModel<ScheduleModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response) {
                    Log_HR.log(Log_HR.LOG_INFO, MyScheduleModify.class, "onResponse", "body : " + response.body().getSuccess());
                    Log_HR.log(Log_HR.LOG_INFO, MyScheduleModify.class, "onResponse", "body : " + response.body().getMessage());
                    if (response.isSuccessful()) {
                        ResponseModel<ScheduleModel> result = response.body();
                        switch (result.getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                onBackPressed();
                                break;
                            case DataDefinition.Network.CODE_ERROR:
                                netFail(R.string.myschedule_modify_fail_alert_title_fail, R.string.myschedule_modify_fail_alert_content_fail);
                                break;
                        }
                    } else
                        netFail(R.string.myschedule_modify_fail_alert_title_fail, R.string.myschedule_modify_fail_alert_content_fail);
                }

                @Override
                public void onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t) {
                    netFail(R.string.myschedule_modify_fail_alert_title_fail, R.string.myschedule_modify_fail_alert_content_fail2);
                }
            });
        } else {
            Net.getInstance().getFactoryIm().modifySchedule(model).enqueue(new Callback<ResponseModel<ScheduleModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response) {
                    Log_HR.log(Log_HR.LOG_INFO, MyScheduleModify.class, "onResponse", "body : " + response.body().getSuccess());
                    Log_HR.log(Log_HR.LOG_INFO, MyScheduleModify.class, "onResponse", "body : " + response.body().getMessage());
                    if (response.isSuccessful()) {
                        ResponseModel<ScheduleModel> result = response.body();
                        switch (result.getSuccess()) {
                            case DataDefinition.Network.CODE_SUCCESS:
                                onBackPressed();
                                break;
                            case DataDefinition.Network.CODE_ERROR:
                                netFail(R.string.myschedule_modify_fail_alert_title_fail, R.string.myschedule_modify_fail_alert_content_fail);
                                break;
                        }
                    } else
                        netFail(R.string.myschedule_modify_fail_alert_title_fail, R.string.myschedule_modify_fail_alert_content_fail);
                }

                @Override
                public void onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t) {
                    netFail(R.string.myschedule_modify_fail_alert_title_fail, R.string.myschedule_modify_fail_alert_content_fail2);
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                if (data != null) {
                    cityModel = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
                    if (cityModel != null)
                        TV_citySearch.setText(cityModel.getCity());
                }
            }
        }
    }

    private void netFail(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(MyScheduleModify.this, title, content);
    }

    private boolean checkValidation() {
        boolean result = false;
        String starDate = TV_dateStart.getText().toString();
        String endDate = TV_dateEnd.getText().toString();

        if (ET_tripName.getText().toString().isEmpty()) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_2)).show();
        } else if (TV_citySearch.getText().toString().isEmpty()) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_3)).show();
        } else if (!(starDate.matches(DataDefinition.RegularExpression.REG_DATE) && endDate.matches(DataDefinition.RegularExpression.REG_DATE))) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_4)).show();
        } else if (!DateManager.getInstance().compareDate(starDate, endDate, DataDefinition.RegularExpression.FORMAT_DATE)) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_alert_title_fail), resourceManager.getResourceString(R.string.regist_alert_content_fail_6)).show();
        } else if (!SessionManager.getInstance().isLogin()) {
            netFail(R.string.regist_alert_title_fail, R.string.alert_content_not_login);
        } else if (DateManager.getInstance().compareDate(starDate, endDate, DataDefinition.RegularExpression.FORMAT_DATE)) {
            result = true;
        }
        return result;
    }

    private boolean sessionCheck() {
        userModel = SessionManager.getInstance().getUserModel();
        return SessionManager.getInstance().isLogin();
    }

    private void bindImage(File file) {
        imageFile = file;
        ImageManager imageManager = ImageManager.getInstance();
        RequestCreator requestCreator = imageManager.createRequestCreator(MyScheduleModify.this, file, ImageManager.THUMBNAIL_TYPE).centerCrop();
        imageManager.loadImage(requestCreator, IV_cover);
        AlertManager.getInstance().dismissAlertSelectionMode();
        IV_camera.setVisibility(View.INVISIBLE);
    }
}