package com.developer.hare.tworaveler.Data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.developer.hare.tworaveler.Activity.SignIn;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.Util.ResourceManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hare on 2017-08-17.
 */

public class SessionManager {
    private UserModel USER_MODEL;
    private ResourceManager resourceManager;
    private OnActionAfterSessionCheckListener onActionAfterSessionCheckListener;
    private static SessionManager sessionManager = new SessionManager();

    {
        resourceManager = ResourceManager.getInstance();
    }

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public boolean isLogin() {
        if (USER_MODEL != null)
            return true;
        else
            return false;
    }

    public void logout(Activity activity){
        USER_MODEL = null;
        activity.startActivity(new Intent(activity, SignIn.class));
    }

    public UserModel getUserModel() {
        return this.USER_MODEL;
    }

    public void setUserModel(UserModel userModel) {
        this.USER_MODEL = userModel;
    }

    public void actionAfterSessoinCheck(Context context, OnActionAfterSessionCheckListener onActionAfterSessionCheckListener) {
        if (isLogin())
            onActionAfterSessionCheckListener.action();
        else {
            AlertManager.getInstance().createAlert(context, SweetAlertDialog.WARNING_TYPE, resourceManager.getResourceString(R.string.not_login_action_title), resourceManager.getResourceString(R.string.not_login_action_content), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    context.startActivity(new Intent(context, SignIn.class));
                }
            }).show();
        }
    }

    public interface OnActionAfterSessionCheckListener {
        void action();
    }

}
