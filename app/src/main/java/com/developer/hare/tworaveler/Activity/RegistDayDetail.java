package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleDayModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.UI.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.Parser.RetrofitBodyParser;
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.R.id.activity_regist_day_detail$TV_start;

public class RegistDayDetail extends AppCompatActivity {
    private UIFactory uiFactory;
    private DateManager dateManager;
    private ResourceManager resourceManager;
    private ProgressManager progressManager;
    private ImageManager imageManager;
    private SessionManager sessionManager;
    private Intent intent;
    private UserModel userModel;
    private String selected_date;
    private ScheduleModel scheduleModel;
    private File imageFile;

    private MenuTopTitle menuTopTitle;
    private ImageView IV_cover, IV_camera;
    private TextView TV_locationName, TV_locationSearch, TV_startTime, TV_endTime;
    private EditText ET_memo;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case activity_regist_day_detail$TV_start:
                    dateManager.getDateTime(RegistDayDetail.this, TV_startTime, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TV_endTime.callOnClick();
                        }
                    });
                    break;
                case R.id.activity_regist_day_detail$TV_end:
                    dateManager.getDateTime(RegistDayDetail.this, TV_endTime, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ET_memo.requestFocus();
                        }
                    });
                    break;
                case R.id.activity_regist_day_detail$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.icon_camera_2, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(RegistDayDetail.this, new OnPhotoBindListener() {
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
                            PhotoManager.getInstance().onGallerySingleSelect(RegistDayDetail.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    bindImage(fileData.getFile());
                                }
                            });
                        }
                    }));
                    AlertManager.getInstance().showAlertSelectionMode(RegistDayDetail.this, "등록 방법 선택", 2, AlertSelectionItemModels).show();
                    break;
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
        selected_date = intent.getExtras().getString(DataDefinition.Intent.KEY_DATE);
        scheduleModel = (ScheduleModel) intent.getSerializableExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL);
        sessionManager = SessionManager.getInstance();
        progressManager = new ProgressManager(this);

        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();
        imageManager = ImageManager.getInstance();
        resourceManager = ResourceManager.getInstance();

        TV_locationName = uiFactory.createView(R.id.activity_regist_day_detail$TV_locationName);
        TV_locationSearch = uiFactory.createView(R.id.activity_regist_day_detail$TV_locationSearch);
        TV_startTime = uiFactory.createView(activity_regist_day_detail$TV_start);
        TV_startTime.setOnClickListener(onClickListener);
        TV_endTime = uiFactory.createView(R.id.activity_regist_day_detail$TV_end);
        TV_endTime.setOnClickListener(onClickListener);
        ET_memo = uiFactory.createView(R.id.activity_regist_day_detail$ET_meno);
        IV_cover = uiFactory.createView(R.id.activity_regist_day_detail$IV_cover);
        IV_cover.setOnClickListener(onClickListener);
        IV_camera = uiFactory.createView(R.id.activity_regist_day_detail$IV_camera);
        menuTopTitle = uiFactory.createView(R.id.activity_regist_day_detail$menuToptitle);
        menuTopTitle.setIBRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        menuTopTitle.setIBLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TV_locationName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    TV_startTime.callOnClick();
                }
                return true;
            }
        });

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_1));
//        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_3));
        textViews.add(uiFactory.createView(R.id.activity_regist_day_detail$TV_txt_4));
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
        textViews.clear();
        textViews.add(TV_locationName);
//        textViews.add(TV_locationSearch);
        textViews.add(TV_startTime);
        textViews.add(TV_endTime);
        textViews.add(ET_memo);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
    }

    // 세부 일정 등록
    private void onRegister() {
        if (!checkValidation())
            return;

        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {

                ScheduleDayModel model = new ScheduleDayModel(scheduleModel, 0, 0, TV_startTime.getText().toString(), TV_endTime.getText().toString(), ET_memo.getText().toString(), TV_locationName.getText().toString(), selected_date);
                if (imageFile != null) {
                    MultipartBody.Part multipart = RetrofitBodyParser.getInstance().createImageMultipartBodyPart(DataDefinition.Key.KEY_USER_FILE, imageFile);

                    Net.getInstance().getFactoryIm().insertDaySchedule(multipart, RetrofitBodyParser.getInstance().parseMapRequestBody(model)).enqueue(new Callback<ResponseModel<ScheduleDayModel>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<ScheduleDayModel>> call, Response<ResponseModel<ScheduleDayModel>> response) {
                            Log_HR.log(RegistDayDetail.class, "onResponse", response);
                            if (response.isSuccessful()) {
                                progressManager.endRunning();
                                ResponseModel<ScheduleDayModel> result = response.body();
                                switch (result.getSuccess()) {
                                    case DataDefinition.Network.CODE_SUCCESS:
                                        AlertManager.getInstance().createAlert(RegistDayDetail.this, SweetAlertDialog.SUCCESS_TYPE, R.string.regist_day_detail_alert_title_success, R.string.regist_day_detail_alert_content_success, new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                finish();
                                            }
                                        }).show();
                                        break;
                                }
                            } else {
                                netFail(R.string.regist_day_detail_alert_title_fail, R.string.regist_day_detail_alert_content_fail);
                                Log_HR.log(Log_HR.LOG_WARN, RegistDayDetail.class, "onResponse", "response is not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<ScheduleDayModel>> call, Throwable t) {
                            Log_HR.log(RegistDayDetail.class, "onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t)", t);
                            netFail(R.string.regist_day_detail_alert_title_fail, R.string.regist_day_detail_alert_content_fail_5);
                        }
                    });
                } else {
                    Net.getInstance().getFactoryIm().insertDaySchedule(model).enqueue(new Callback<ResponseModel<ScheduleDayModel>>() {
                        @Override
                        public void onResponse(Call<ResponseModel<ScheduleDayModel>> call, Response<ResponseModel<ScheduleDayModel>> response) {
                            Log_HR.log(RegistDayDetail.class, "onResponse", response);
                            if (response.isSuccessful()) {
                                progressManager.endRunning();
                                ResponseModel<ScheduleDayModel> result = response.body();
                                switch (result.getSuccess()) {
                                    case DataDefinition.Network.CODE_SUCCESS:
                                        AlertManager.getInstance().createAlert(RegistDayDetail.this, SweetAlertDialog.SUCCESS_TYPE, R.string.regist_day_detail_alert_title_success, R.string.regist_day_detail_alert_content_success, new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                finish();
                                            }
                                        }).show();
                                        break;
                                }
                            } else {
                                netFail(R.string.regist_day_detail_alert_title_fail, R.string.regist_day_detail_alert_content_fail);
                                Log_HR.log(Log_HR.LOG_WARN, RegistDayDetail.class, "onResponse", "response is not successful");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel<ScheduleDayModel>> call, Throwable t) {
                            netFail(R.string.regist_day_detail_alert_title_fail, R.string.regist_day_detail_alert_content_fail_5);
                            Log_HR.log(RegistDayDetail.class, "onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t)", t);
                        }
                    });
                }
            }
        });
    }

    // 일정 등록 전 유효성 검사
    private boolean checkValidation() {
        if (!sessionCheck()) {
            AlertManager.getInstance().showNotLoginAlert(this, R.string.regist_day_detail_alert_title_fail);
            return false;
        }

        boolean result = false;
        String starDate = TV_startTime.getText().toString();
        String endDate = TV_endTime.getText().toString();
        if (TV_locationName.getText().toString().isEmpty()) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_day_detail_alert_title_fail), resourceManager.getResourceString(R.string.regist_day_detail_alert_content_fail_2)).show();
//        } else if (TV_locationSearch.getText().toString().isEmpty()) {
//            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_day_detail_alert_title_fail), resourceManager.getResourceString(R.string.regist_day_detail_alert_content_fail_3)).show();
        } else if (ET_memo.getText().toString().isEmpty()) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_day_detail_alert_title_fail), resourceManager.getResourceString(R.string.regist_day_detail_alert_content_fail_6)).show();
        } else if (!(starDate.matches(DataDefinition.RegularExpression.REG_TIME) && endDate.matches(DataDefinition.RegularExpression.REG_TIME))) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_day_detail_alert_title_fail), resourceManager.getResourceString(R.string.regist_day_detail_alert_content_fail_4)).show();
        } else if (!DateManager.getInstance().compareDate(starDate, endDate, DataDefinition.RegularExpression.FORMAT_DATE_TIME)) {
            AlertManager.getInstance().createAlert(this, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.regist_day_detail_alert_title_fail), resourceManager.getResourceString(R.string.regist_day_detail_alert_content_fail_7)).show();
        } else {
            result = true;
        }
        return result;
    }

    // Session Check & get UserModel in Memory
    private boolean sessionCheck() {
        userModel = sessionManager.getUserModel();
        return sessionManager.isLogin();
    }

    // 도시 검색
    public void searchCity(View view) {
        Intent intent = new Intent(this, SearchCity.class);
        startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
    }

    // Network 실패 경고창 show
    private void netFail(int title, int content) {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(RegistDayDetail.this, title, content);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                if (data != null) {

                }
            }
        }
    }

    private void bindImage(File file) {
        imageFile = file;
        ImageManager imageManager = ImageManager.getInstance();
        RequestCreator requestCreator = imageManager.createRequestCreator(RegistDayDetail.this, file, ImageManager.THUMBNAIL_TYPE).centerCrop();
        imageManager.loadImage(requestCreator, IV_cover);
        AlertManager.getInstance().dismissAlertSelectionMode();
        IV_camera.setVisibility(View.INVISIBLE);
    }
}
