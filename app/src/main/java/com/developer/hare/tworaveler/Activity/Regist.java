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
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.R.id.activity_regist$TV_start;

public class Regist extends AppCompatActivity {
    private UIFactory uiFactory;
    private MenuTopTitle menuTopTitle;
    private DateManager dateManager;
    private ResourceManager resourceManager;
    private ImageManager imageManager;
    private File imageFile;

    private UserModel userModel;
    private CityModel cityModel;
    private EditText ET_tripName;
    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover, IV_camera;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case activity_regist$TV_start:
                    dateManager.getDateYMD(Regist.this, TV_dateStart, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TV_dateEnd.callOnClick();
                        }
                    });
                    break;
                case R.id.activity_regist$TV_end:
                    dateManager.getDateYMD(Regist.this, TV_dateEnd);
                    break;
                case R.id.activity_regist$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.icon_camera, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(Regist.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    imageFile = fileData.getFile();
                                    RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, fileData.getFile(), ImageManager.FIT_TYPE).centerCrop();
                                    imageManager.loadImage(requestCreator, IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                    IV_camera.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }));
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.icon_gallery, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onGallerySingleSelect(Regist.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    imageFile = fileData.getFile();
                                    RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, fileData.getFile(), ImageManager.FIT_TYPE).centerCrop();
                                    imageManager.loadImage(requestCreator, IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                    IV_camera.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }));
                    AlertManager.getInstance().showAlertSelectionMode(Regist.this, "등록 방법 선택", 2, AlertSelectionItemModels).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
    }

    protected void init() {
        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();
        resourceManager = ResourceManager.getInstance();
        imageManager = ImageManager.getInstance();
        sessionCheck();

        menuTopTitle = uiFactory.createView(R.id.activity_regist$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegister();
            }
        });
        ET_tripName = uiFactory.createView(R.id.activity_regist$TV_tripName);
        TV_citySearch = uiFactory.createView(R.id.activity_regist$TV_citySearch);
        TV_dateStart = uiFactory.createView(activity_regist$TV_start);
        TV_dateStart.setOnClickListener(onClickListener);
        TV_dateEnd = uiFactory.createView(R.id.activity_regist$TV_end);
        TV_dateEnd.setOnClickListener(onClickListener);
        IV_cover = uiFactory.createView(R.id.activity_regist$IV_cover);
        IV_cover.setOnClickListener(onClickListener);
        IV_camera = uiFactory.createView(R.id.activity_regist$IV_camera);

        ET_tripName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_NEXT){
                    TV_dateStart.callOnClick();
                }
                return true;
            }
        });

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.activity_regist$TV_txt_1));
        textViews.add(uiFactory.createView(R.id.activity_regist$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.activity_regist$TV_txt_3));
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
        textViews.clear();
        textViews.add(ET_tripName);
        textViews.add(TV_citySearch);
        textViews.add(TV_dateStart);
        textViews.add(TV_dateEnd);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");

    }

    public void searchCity(View view) {
        Intent intent = new Intent(Regist.this, SearchCity.class);
        startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
    }

    private void onRegister() {
        if (!checkValidation())
            return;

        ScheduleModel model = new ScheduleModel(userModel, cityModel, TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), ET_tripName.getText().toString());
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
                                Intent intent = new Intent(Regist.this, RegistDetail.class);
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
                                Intent intent = new Intent(Regist.this, RegistDetail.class);
                                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, result.getResult());
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
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == DataDefinition.Intent.RESULT_CODE_SEARCH_CITY) {
            // Make sure the request was successful
            if (resultCode == DataDefinition.Intent.RESULT_CODE_SUCCESS) {
                if (data != null) {
                    cityModel = (CityModel) data.getSerializableExtra(DataDefinition.Intent.KEY_CITYMODEL);
                    if (cityModel != null) {
                        TV_citySearch.setText(cityModel.getCountry() + " " + cityModel.getCity());
                        RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, cityModel.getMain_pic_url(), ImageManager.PICTURE_TYPE).centerCrop();
                        imageManager.loadImage(requestCreator, IV_cover);
                        IV_camera.setVisibility(View.INVISIBLE);
                    }

                }
            }
        }
    }

    private void netFail(int title, int content) {
        AlertManager.getInstance().showNetFailAlert(Regist.this, title, content);
    }

}
