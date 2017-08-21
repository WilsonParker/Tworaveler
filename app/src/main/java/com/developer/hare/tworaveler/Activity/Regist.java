package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Date.DateManager;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
                                    ImageManager.getInstance().loadImage(Regist.this, fileData.getFile(), IV_cover, ImageManager.PICTURE_TYPE);
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
                                    RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, fileData.getFile(), ImageManager.PICTURE_TYPE).centerCrop();
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

        menuTopTitle = uiFactory.createView(R.id.activity_regist$menuToptitle);
        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist.this.onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onRegister();
                ScheduleModel model = new ScheduleModel(0, 0, "country", TV_citySearch.getText().toString(), ET_tripName.getText().toString(), TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), "trip_pic_url", "tripName", "", "");
                Intent intent = new Intent(Regist.this, RegistDetail.class);
                intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, model);
                startActivity(intent);
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

//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "init", "TV_citySearch text : " + TV_citySearch.getText().toString());
    }

    public void searchCity(View view) {
        Intent intent = new Intent(Regist.this, SearchCity.class);
        startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
    }

    private void onRegister() {
        if (!checkValidation())
            return;
        ScheduleModel model = new ScheduleModel(0, 0, "country", TV_citySearch.getText().toString(), ET_tripName.getText().toString(), TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), "trip_pic_url", "tripName", "", "");
        Call<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> res = Net.getInstance().getFactoryIm().insertSchedule(model);
        res.enqueue(new Callback<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> call, Response<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> response) {
                if (response.isSuccessful()) {
                    ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel> result = response.body();
                    if (result.getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                        Intent intent = new Intent(Regist.this, RegistDetail.class);
                        intent.putExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL, model);
                        startActivity(intent);
                    } else
                        netFail();
                } else
                    netFail();
            }

            @Override
            public void onFailure(Call<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> call, Throwable t) {
                netFail();
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
                        RequestCreator requestCreator = imageManager.createRequestCreator(Regist.this, model.getMain_pic_thumbnail_url(), ImageManager.PICTURE_TYPE).centerCrop();
                        imageManager.loadImage(requestCreator, IV_camera);
                        IV_camera.setVisibility(View.INVISIBLE);
                    }

                }
            }
        }
    }

    private void netFail() {
        AlertManager.getInstance().showNetFailAlert(Regist.this, R.string.regist_alert_title_fail, R.string.regist_alert_content_fail);
    }

}
