package com.developer.hare.tworaveler.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Listener.OnInputAlertClickListener;
import com.developer.hare.tworaveler.Listener.OnPhotoBindListener;
import com.developer.hare.tworaveler.Model.AlertSelectionItemModel;
import com.developer.hare.tworaveler.Model.Request.UserReqModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.Layout.MenuTopTitle;
import com.developer.hare.tworaveler.UI.PhotoManager;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.File.FileManager;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData;

import java.util.ArrayList;
import java.util.HashSet;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class MyProfileSet extends AppCompatActivity {

    private EditText ET_nickname, ET_message;
    private CircleImageView circleImageView;
    private MenuTopTitle menuTopTitle;

    private UIFactory uiFactory;
    private UserModel userModel;
    private ResourceManager resourceManager;
    private ProgressManager progressManager;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);
        init();
    }

    private void init() {
        activity = this;
        uiFactory = UIFactory.getInstance(this);
        resourceManager = ResourceManager.getInstance();
        progressManager = new ProgressManager(this);

        ET_nickname = uiFactory.createView(R.id.profile_set$ET_nickname);
        ET_message = uiFactory.createView(R.id.profile_set$ET_message);
        menuTopTitle = uiFactory.createView(R.id.profile_set$topbar);
        circleImageView = uiFactory.createView(R.id.profile_setIV_profile);

        menuTopTitle.getIB_left().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        menuTopTitle.getIB_right().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyData();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<AlertSelectionItemModel> AlertSelectionItemModels = new ArrayList<>();
                AlertSelectionItemModels.add(new AlertSelectionItemModel("사진 촬영", R.drawable.ic_camera_alt_black_24dp, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotoManager.getInstance().onCameraSelect(MyProfileSet.this, new OnPhotoBindListener() {
                            @Override
                            public void bindData(FileData fileData) {
                                ImageManager.getInstance().loadImage(MyProfileSet.this, fileData.getFile(), circleImageView, ImageManager.THUMBNAIL_TYPE);
                                AlertManager.getInstance().dismissAlertSelectionMode();
                            }
                        });
                    }
                }));
                AlertSelectionItemModels.add(new AlertSelectionItemModel("갤러리", R.drawable.ic_filter_black_24dp, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhotoManager.getInstance().onGallerySingleSelect(MyProfileSet.this, new OnPhotoBindListener() {
                            @Override
                            public void bindData(FileData fileData) {
                                ImageManager.getInstance().loadImage(MyProfileSet.this, fileData.getFile(), circleImageView, ImageManager.THUMBNAIL_TYPE);
                                AlertManager.getInstance().dismissAlertSelectionMode();
                            }
                        });
                    }

                }));
                AlertManager.getInstance().showAlertSelectionMode(MyProfileSet.this, "등록 방법 선택", 2, AlertSelectionItemModels).show();

            }
        });
        ET_nickname = uiFactory.createView(R.id.profile_set$ET_nickname);
        ET_message = uiFactory.createView(R.id.profile_set$ET_message);
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(ET_nickname);
        textViews.add(ET_message);
        FontManager.getInstance().setFont(textViews, "NotoSansCJKkr-Medium.otf");
    }

    public void onLogout(View view) {
        AlertManager.getInstance().showPopup(activity,
                resourceManager.getResourceString(R.string.profileSet_logout_alert_title),
                resourceManager.getResourceString(R.string.profileSet_logout_alert_content),
                "취소",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                },
                "확인",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Net.getInstance().getFactoryIm().userLogout().enqueue(new Callback<ResponseModel<String>>() {
                            @Override
                            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                                if (response.isSuccessful()) {
                                    switch (response.body().getSuccess()) {
                                        case CODE_SUCCESS:
                                            SessionManager.getInstance().setUserModel(null);
                                            Intent intent = new Intent(activity, Main.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            break;
                                    }
                                } else {
                                    netFail(R.string.profileSet_logout_fail_alert_title, R.string.profileSet_logout_fail_alert_content);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                                netFail(R.string.profileSet_logout_fail_alert_title, R.string.profileSet_logout_fail_alert_content);
                            }
                        });
                    }
                }
        );
    }

    public void onWithdrawal(View view) {
        AlertManager.getInstance().showPopup(activity,
                resourceManager.getResourceString(R.string.profileSet_signOut_alert_title),
                resourceManager.getResourceString(R.string.profileSet_signOut_fail_alert_content),
                "취소",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                },
                "확인",
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        AlertManager.getInstance().showInputAlert(activity, R.string.profileSet_signOut_alert_title, R.string.profileSet_signOut_alert_input_messae, new OnInputAlertClickListener() {
                            @Override
                            public void onConfirmClick(String input) {
//                                String cookie = SessionManager.getInstance().getUserModel().getCookie();
                                String cookie = FileManager.getInstance().getPreference().getStringSet(FileManager.KEY_SESSION, new HashSet<>()).iterator().next();
                                Log_HR.log(Log_HR.LOG_INFO, MyProfileSet.class, "onResponse()", "Cookie : " + cookie);
                                Net.getInstance().getFactoryIm().userSignOut(new UserReqModel(userModel.getEmail(), input)).enqueue(new Callback<ResponseModel<ResponseModel<String>>>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel<ResponseModel<String>>> call, Response<ResponseModel<ResponseModel<String>>> response) {
                                        ResponseModel<ResponseModel<String>> resModel = response.body();
                                        Log_HR.log(Log_HR.LOG_INFO, MyProfileSet.class, "onResponse()", "body : " + resModel);
                                        int successCode;
                                        if (response.isSuccessful()) {
                                            progressManager.endRunning();
                                            if (resModel.getResult() == null) {
                                                successCode = resModel.getSuccess();
                                            } else {
                                                successCode = resModel.getResult().getSuccess();
                                            }
                                            switch (successCode) {
                                                case DataDefinition.Network.CODE_SUCCESS:
                                                    AlertManager.getInstance().createAlert(activity, SweetAlertDialog.SUCCESS_TYPE, resourceManager.getResourceString(R.string.profileSet_signOut_alert_title), resourceManager.getResourceString(R.string.profileSet_signOut_fail_alert_content3), new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismiss();
                                                            startActivity(new Intent(activity, SignIn.class));
                                                            finish();
                                                        }
                                                    }).show();
                                                    break;
                                                case DataDefinition.Network.CODE_NOT_LOGIN:
                                                    netFail(R.string.profileSet_signOut_fail_alert_title, R.string.alert_content_not_login);
                                                    break;
                                                case DataDefinition.Network.CODE_EMAIL_INCORRECT:
                                                    netFail(R.string.profileSet_signOut_fail_alert_title, R.string.profileSet_signOut_fail_alert_content6);
                                                    break;
                                                case DataDefinition.Network.CODE_PW_INCORRECT:
                                                    netFail(R.string.profileSet_signOut_fail_alert_title, R.string.profileSet_signOut_fail_alert_content5);
                                                    break;
                                            }
                                        } else {
                                            netFail(R.string.profileSet_signOut_fail_alert_title, R.string.profileSet_signOut_fail_alert_content2);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel<ResponseModel<String>>> call, Throwable t) {
                                        netFail(R.string.profileSet_signOut_fail_alert_title, R.string.profileSet_signOut_fail_alert_content2);
                                    }
                                });
                            }
                        });
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        userModel = SessionManager.getInstance().getUserModel();
        ET_nickname.setText(userModel.getNickname());
        ET_message.setText(userModel.getStatus_message());
        ImageManager imageManager = ImageManager.getInstance();
        imageManager.loadImage(imageManager.createRequestCreator(this, userModel.getProfile_pic_url_thumbnail(), ImageManager.FIT_TYPE).placeholder(R.drawable.image_profile), circleImageView);
    }

    private void modifyData() {
        UserReqModel userReqModel = new UserReqModel(userModel.getUser_no(), userModel.getNickname(), ET_nickname.getText().toString(), ET_message.getText().toString());
        Net.getInstance().getFactoryIm().modifyProfile(userReqModel).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    ResponseModel<UserModel> result = response.body();
                    switch (result.getSuccess()) {
                        case DataDefinition.Network.CODE_SUCCESS:
                            UserModel model = result.getResult();
                            SessionManager.getInstance().setUserModel(model);
                            finish();
                            break;
                        case DataDefinition.Network.CODE_NOT_LOGIN:
                            netFail(R.string.profileSet_mod_fail_alert_title, R.string.alert_content_not_login);
                            break;
                        case DataDefinition.Network.CODE_NICKNAME_CONFLICT:
                            netFail(R.string.profileSet_mod_fail_alert_title, R.string.profileSet_mod_fail_alert_content_3);
                            break;
                    }

                } else {
                    netFail(R.string.profileSet_mod_fail_alert_title, R.string.profileSet_mod_fail_alert_content);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                netFail(R.string.profileSet_mod_fail_alert_title, R.string.profileSet_mod_fail_alert_content);
            }
        });
    }

    private void netFail(int title, int content) {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(this, title, content);
    }
}
