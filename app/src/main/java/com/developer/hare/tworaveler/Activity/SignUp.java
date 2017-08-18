package com.developer.hare.tworaveler.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.UserSignUpModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.Net.NetFactoryIm;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.FontManager;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.developer.hare.tworaveler.Util.ResourceManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private EditText ET_email, ET_password, ET_nickName;
    private Button BT_signUp;

    private NetFactoryIm netFactoryIm;
    private ResourceManager resourceManager;
    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init() {
        UIFactory uiFactory = UIFactory.getInstance(this);
        resourceManager = ResourceManager.getInstance();
        progressManager = new ProgressManager(this);

        netFactoryIm = Net.getInstance().getFactoryIm();

        ET_email = uiFactory.createView(R.id.signUp$ET_email);
        ET_password = uiFactory.createView(R.id.signUp$ET_password);
        ET_nickName = uiFactory.createView(R.id.signUp$ET_nickname);
        BT_signUp = uiFactory.createView(R.id.signUp$BT_signUp);
        BT_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validCheck()) {
                    signUp();
                } else {
//                    Log_HR.log(Log_HR.LOG_INFO,getClass(), "onClick(View)","getString : "+resourceManager.getResourceString((R.string.signUp_fail_alert_title)));
                    AlertManager.getInstance().createAlert(SignUp.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signUp_alert_title_fail)), resourceManager.getResourceString((R.string.signUp_alert_content_fail))).show();
                }
            }
        });

        netFactoryIm = Net.getInstance().getFactoryIm();
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(ET_email);
        textViews.add(ET_password);
        FontManager.getInstance().setFont(textViews, "Roboto-Medium.ttf");
        FontManager.getInstance().setFont(ET_nickName, "NotoSansCJKkr-Medium.ttf");
    }

    private boolean validCheck() {
        String email = ET_email.getText().toString(), password = ET_password.getText().toString(), nickName = ET_nickName.getText().toString();
        if (email.isEmpty() | password.isEmpty() | nickName.isEmpty())
            return false;
        boolean result = email.matches(DataDefinition.RegularExpression.REG_EMAIL) && password.matches(DataDefinition.RegularExpression.REG_PASSWORD) && nickName.matches(DataDefinition.RegularExpression.REG_NICKNAME);
        return result;
    }

    private void signUp() {
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                UserSignUpModel model = new UserSignUpModel(ET_email.getText().toString(), ET_password.getText().toString(), ET_nickName.getText().toString());
                Call<RequestModel<UserModel>> res = netFactoryIm.userSignUp(model);
                res.enqueue(new Callback<RequestModel<UserModel>>() {
                    @Override
                    public void onResponse(Call<RequestModel<UserModel>> call, Response<RequestModel<UserModel>> response) {
                        UserModel result = response.body().getResult();
                        Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signUp - onResponse(Call, Response)", "body : " + result);
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signUp - onResponse(Call, Response)", "isSuccess ");
                            AlertManager.getInstance().createAlert(SignUp.this, SweetAlertDialog.SUCCESS_TYPE
                                    , resourceManager.getResourceString(R.string.signUp_alert_title_success)
                                    , resourceManager.getResourceString(R.string.signUp_alert_content_success), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    startActivity(new Intent(getBaseContext(), SignIn.class));
                                            finish();
                                        }
                                    }).show();
                        } else {
                            Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signUp - onResponse(Call, Response)", "isFail");
                            netFail();
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestModel<UserModel>> call, Throwable t) {
                        Log_HR.log(SignUp.class, "onFailure()", t);
                        netFail();
                    }
                });
            }
        });
    }

    private void netFail() {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(this, R.string.signUp_alert_title_fail, R.string.signUp_alert_content_fail2);
    }

}
