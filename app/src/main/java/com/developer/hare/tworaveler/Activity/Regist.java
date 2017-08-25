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
import com.google.gson.Gson;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private EditText ET_tripName;
    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover, IV_camera;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case activity_regist$TV_start:
                    dateManager.getDateTime(Regist.this, TV_dateStart);
                    break;
                case R.id.activity_regist$TV_end:
                    dateManager.getDateTime(Regist.this, TV_dateEnd);
                    break;
                case R.id.activity_regist$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.button_left, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(Regist.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    imageFile = fileData.getFile();
                                    ImageManager.getInstance().loadImage(Regist.this, fileData.getFile(), IV_cover, ImageManager.FIT_TYPE);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                    IV_camera.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }));
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.button_left, new View.OnClickListener() {
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
                    AlertManager.getInstance().showAlertSelectionMode(Regist.this, "등록 방법 선택", 3, AlertSelectionItemModels).show();
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
        userModel = SessionManager.getInstance().getUserModel();

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
        TV_citySearch.setOnClickListener(onClickListener);
        TV_dateStart = uiFactory.createView(activity_regist$TV_start);
        TV_dateStart.setOnClickListener(onClickListener);
        TV_dateEnd = uiFactory.createView(R.id.activity_regist$TV_end);
        TV_dateEnd.setOnClickListener(onClickListener);
        IV_cover = uiFactory.createView(R.id.activity_regist$IV_cover);
        IV_cover.setOnClickListener(onClickListener);
        IV_camera = uiFactory.createView(R.id.activity_regist$IV_camera);

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
        ScheduleModel model = new ScheduleModel(userModel.getUser_no(), userModel.getNickname(), userModel.getStatus_message(), "country", TV_citySearch.getText().toString(), TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), userModel.getProfile_pic_url_thumbnail(), "", ET_tripName.getText().toString());
        String json = new Gson().toJson(model);
        RequestBody requestBody = RetrofitBodyParser.getInstance().createRequestBody(json);
        MultipartBody.Part multipart = RetrofitBodyParser.getInstance().createImageMultipartBodyPart(DataDefinition.Key.KEY_USER_FILE, imageFile);

        Net.getInstance().getFactoryIm().insertSchedule(multipart, requestBody).enqueue(new Callback<ResponseModel<ScheduleModel>>() {
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
        } else if (DateManager.getInstance().compareDate(starDate, endDate, DataDefinition.RegularExpression.FORMAT_DATE)) {
            result = true;
        }
        return result;
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
                        TV_citySearch.setText(model.getCity());
                        RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, model.getMain_pic_url(), ImageManager.PICTURE_TYPE).centerCrop();
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
