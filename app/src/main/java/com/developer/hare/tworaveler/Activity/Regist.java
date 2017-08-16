package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.ResourceManager;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.SceduleRegistModel;
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
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

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

    private TextView TV_citySearch, TV_dateStart, TV_dateEnd;
    private ImageView IV_cover;

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
                case R.id.activity_regist$TV_citySearch:
                    Intent intent = new Intent(Regist.this, SearchCity.class);
                    startActivityForResult(intent, DataDefinition.Intent.RESULT_CODE_SEARCH_CITY);
                    break;
                case R.id.activity_regist$IV_cover:
                    ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                    AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.button_left, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PhotoManager.getInstance().onCameraSelect(Regist.this, new OnPhotoBindListener() {
                                @Override
                                public void bindData(FileData fileData) {
                                    ImageManager.getInstance().loadImage(Regist.this, fileData.getFile(), IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
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
                                    ImageManager.getInstance().loadImage(Regist.this, fileData.getFile(), IV_cover);
                                    AlertManager.getInstance().dismissAlertSelectionMode();
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
                onRegist();
            }
        });
        TV_citySearch = uiFactory.createView(R.id.activity_regist$TV_citySearch);
        TV_citySearch.setOnClickListener(onClickListener);
        TV_dateStart = uiFactory.createView(activity_regist$TV_start);
        TV_dateStart.setOnClickListener(onClickListener);
        TV_dateEnd = uiFactory.createView(R.id.activity_regist$TV_end);
        TV_dateEnd.setOnClickListener(onClickListener);
        IV_cover = uiFactory.createView(R.id.activity_regist$IV_cover);
        IV_cover.setOnClickListener(onClickListener);

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(uiFactory.createView(R.id.activity_regist$TV_txt_1));
        textViews.add(uiFactory.createView(R.id.activity_regist$TV_txt_2));
        textViews.add(uiFactory.createView(R.id.activity_regist$TV_txt_3));
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Bold.otf");
        textViews.clear();
        textViews.add(TV_citySearch);
        textViews.add(TV_dateStart);
        textViews.add(TV_dateEnd);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
    }

    private void onRegist() {
        SceduleRegistModel model = new SceduleRegistModel(0, "country", "city", TV_dateStart.getText().toString(), TV_dateEnd.getText().toString(), "trip_pic_url", "tripName");
        Call<RequestModel<ScheduleModel>> res = Net.getInstance().getFactoryIm().registPlan(model);
        res.enqueue(new Callback<RequestModel<ScheduleModel>>() {
            @Override
            public void onResponse(Call<RequestModel<ScheduleModel>> call, Response<RequestModel<ScheduleModel>> response) {
                if (response.isSuccessful()) {
                    RequestModel<ScheduleModel> result = response.body();
                    if (result.getSuccess() == DataDefinition.Network.CODE_SUCCESS) {
                        Intent intent = new Intent(Regist.this, RegistDetail.class);
                        intent.putExtra(DataDefinition.Intent.KEY_STARTDATE, TV_dateStart.getText().toString());
                        intent.putExtra(DataDefinition.Intent.KEY_ENDDATE, TV_dateEnd.getText().toString());
                        startActivity(intent);
                    } else
                        netFail();
                } else
                    netFail();
            }

            @Override
            public void onFailure(Call<RequestModel<ScheduleModel>> call, Throwable t) {
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
                        TV_citySearch.setText(model.getCityName());
                }
            }
        }
    }

    private void netFail() {
        AlertManager.getInstance().createAlert(Regist.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signUp_fail_alert_title_fail)), resourceManager.getResourceString((R.string.signUp_fail_alert_content_fail2))).show();
    }

}
