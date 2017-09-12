package com.developer.hare.tworaveler.Util;

import android.app.Activity;

import com.developer.hare.tworaveler.Activity.SignIn;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Listener.OnItemDataChangeListener;
import com.developer.hare.tworaveler.Model.Request.UserReqModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_EMAIL_PW_INCORRECT;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SIGNOUT_USER;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

/**
 * Created by Hare on 2017-09-05.
 */

public class TestManager {
    private boolean isLogin = false;
    private Activity activity;
    private OnItemDataChangeListener onItemDataChangeListener;

    public TestManager(Activity activity, OnItemDataChangeListener onItemDataChangeListener) {
        this.activity = activity;
        this.onItemDataChangeListener = onItemDataChangeListener;
    }

    public void testLogin2() {

    }
    public void testLogin() {
        if(isLogin)
            return;
        isLogin = true;
        ResourceManager resourceManager = ResourceManager.getInstance();
        UserReqModel signIn = new UserReqModel("tworaveler@gmail.com", "00000000");
        Net.getInstance().getFactoryIm().userSignIn(signIn).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    ResponseModel<UserModel> result = response.body();
                    switch (result.getSuccess()) {
                        case CODE_SUCCESS:
                            AlertManager.getInstance().createAlert(activity, SweetAlertDialog.SUCCESS_TYPE
                                    , resourceManager.getResourceString(R.string.signIn_alert_title_success)
                                    , resourceManager.getResourceString(R.string.signIn_alert_content_success), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            UserModel model = result.getResult();
                                            if (model.getFollowees() == null)
                                                model.setFollowees(new ArrayList<>());
                                            if (model.getFollowers() == null)
                                                model.setFollowers(new ArrayList<>());
                                            SessionManager.getInstance().setUserModel(model);
                                            onItemDataChangeListener.onChange();
//                                            activity.onBackPressed();
                                        }
                                    }).show();
                            break;
                        case CODE_EMAIL_PW_INCORRECT:
                            AlertManager.getInstance().createAlert(activity, SweetAlertDialog.WARNING_TYPE
                                    , resourceManager.getResourceString(R.string.signIn_alert_title_fail)
                                    , resourceManager.getResourceString(R.string.signIn_alert_content_fail2), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                        }
                                    }).show();
                            break;
                        case CODE_SIGNOUT_USER:
                            AlertManager.getInstance().createAlert(activity, SweetAlertDialog.ERROR_TYPE
                                    , resourceManager.getResourceString(R.string.signIn_alert_title_fail)
                                    , resourceManager.getResourceString(R.string.signIn_alert_content_fail3), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                        }
                                    }).show();
                            break;
                        default:
                            netFail();
                            break;
                    }


                } else {
                    LogManager.log(LogManager.LOG_INFO, SignIn.class, "signIn - onResponse(Call, Response)", "isFail");
                    netFail();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                netFail();
            }
        });
    }

    private void netFail() {
        AlertManager.getInstance().showNetFailAlert(activity, R.string.signIn_alert_title_fail, R.string.signIn_alert_content_fail4);
    }
}
