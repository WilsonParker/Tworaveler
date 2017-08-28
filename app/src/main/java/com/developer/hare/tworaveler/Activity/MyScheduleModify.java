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
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;
import com.squareup.picasso.RequestCreator;

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
    private EditText ET_tripName;
    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover, IV_camera;
    private ScheduleModel scheduleModel;

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
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.button_left, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(MyScheduleModify.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager.getInstance().loadImage(MyScheduleModify.this, fileData.getFile(), IV_cover, ImageManager.THUMBNAIL_TYPE);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                    IV_camera.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }));
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.button_left, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onGallerySingleSelect(MyScheduleModify.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager imageManager =ImageManager.getInstance();
                                    RequestCreator requestCreator = imageManager.createRequestCreator(MyScheduleModify.this, fileData.getFile(), ImageManager.THUMBNAIL_TYPE).centerCrop();
                                    imageManager.loadImage(requestCreator,IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                    IV_camera.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }));
                    AlertManager.getInstance().showAlertSelectionMode(MyScheduleModify.this, "등록 방법 선택", 3, AlertSelectionItemModels).show();
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
        scheduleModel = (ScheduleModel)getIntent().getSerializableExtra(DataDefinition.Intent.KEY_SCHEDULE_MODEL);
        uiFactory = UIFactory.getInstance(this);
        dateManager = DateManager.getInstance();
        resourceManager = ResourceManager.getInstance();

        userModel = SessionManager.getInstance().getUserModel();
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
        IV_camera= uiFactory.createView(R.id.activity_myschedule_modify$IV_camera);

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
//        textViews.clear();
        ArrayList<TextView> textViews2 = new ArrayList<>();
        textViews2.add(ET_tripName);
        textViews2.add(TV_citySearch);
        textViews2.add(TV_dateStart);
        textViews2.add(TV_dateEnd);
        FontManager.getInstance().setFont(textViews2, "NotoSansCJKkr-Medium.otf");
    }

    private void onModify() {
        ScheduleModel model = new ScheduleModel(userModel.getUser_no(), userModel.getNickname(), userModel.getStatus_message(), "country", TV_citySearch.getText().toString(), TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(),ET_tripName.getText().toString());
        Net.getInstance().getFactoryIm().modifySchedule(model).enqueue(new Callback<ResponseModel<ScheduleModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response) {
                if (response.isSuccessful()) {
                    ResponseModel<ScheduleModel> result = response.body();
                    if (result.getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                        onBackPressed();
                    } else
                        netFail();
                } else
                    netFail();
            }

            @Override
            public void onFailure(Call<ResponseModel<ScheduleModel>> call, Throwable t) {
                netFail();
            }
        });


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
                    if (model != null)
                        TV_citySearch.setText(model.getCity());
                }
            }
        }
    }

    private void netFail() {
        AlertManager.getInstance().createAlert(MyScheduleModify.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.myschedule_modify_fail_alert_title_fail)), resourceManager.getResourceString((R.string.myschedule_modify_fail_alert_content_fail2))).show();
    }

}