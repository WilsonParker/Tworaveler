package com.developer.hare.tworaveler.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Util.ResourceManager;
import com.developer.hare.tworaveler.FaceBook.Util.FaceBookLoginManager;
import com.developer.hare.tworaveler.Kakao.Util.KakaoSignManager;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.UserSignInModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.ProgressManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.KeyManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_INCORRECT;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SIGNED_OUT;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class SignIn extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private Button BT_main, BT_facebook, BT_kakao;
    private TextView BT_signUp;
    private EditText ET_email, ET_password;
    private ImageButton IV_signIn;

    //    private com.kakao.usermgmt.LoginButton BT_kakaoLogin;
//    private com.facebook.login.widget.LoginButton BT_facebookLogin;
    private KakaoSignManager kakaoSignInManager;
    private FaceBookLoginManager faceBookLoginManager;

    private UIFactory uiFactory;
    private ResourceManager resourceManager;
    private ProgressManager progressManager;

    private boolean onFacebookLogin;
    private boolean onKakaoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
    }

    private void init() {
//        String key = getKeyHash(getBaseContext());
//        Log.i(TAG, "##########  key : "+key);
        uiFactory = UIFactory.getInstance(this);
        resourceManager = ResourceManager.getInstance();
        progressManager = new ProgressManager(this);
        faceBookLoginManager = new FaceBookLoginManager(SignIn.this);
        kakaoSignInManager = new KakaoSignManager(SignIn.this);

        BT_signUp = uiFactory.createView(R.id.login$BT_signUp);
        BT_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        // init Facebook
        BT_facebook = uiFactory.createView(R.id.activity_login$BT_facebook);
        BT_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceBookLoginManager.onLoginClick();
            }
        });

        // init Kakao
        BT_kakao = uiFactory.createView(R.id.activity_login$BT_kakao);
        BT_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakaoSignInManager.onLoginClick();
            }
        });

        ET_email = uiFactory.createView(R.id.activty_login$ET_email);
        ET_email.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf"));
        ET_password = uiFactory.createView(R.id.activty_login$ET_password);

        IV_signIn = uiFactory.createView(R.id.activity_login$IV_signIn);
        IV_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validCheck()) {
                    signIn();
                } else {
                    ResourceManager resourceManager = ResourceManager.getInstance();
                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signIn_fail_alert_title_fail)), resourceManager.getResourceString((R.string.signIn_fail_alert_content_faile))).show();
                }
            }
        });

        Log_HR.log(Log_HR.LOG_INFO, getClass(), "init()", "key hash :  " + KeyManager.getInstance().getKeyHash(this));
    }


    private boolean validCheck() {
        String email = ET_email.getText().toString();
        String password = ET_password.getText().toString();
        if (email.isEmpty() || password.isEmpty())
            return false;
        boolean result = email.matches(DataDefinition.RegularExpression.REG_EMAIL) && password.matches(DataDefinition.RegularExpression.REG_PASSWORD);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (kakaoSignInManager != null) {
            kakaoSignInManager.onActivityResult(requestCode, resultCode, data);
            onKakaoLogin = true;
        }
        if (faceBookLoginManager != null) {
            faceBookLoginManager.onActivityResult(requestCode, resultCode, data);
            onFacebookLogin = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        kakaoSignInManager.setLoginButton();
    }

    @Override
    protected void onDestroy() {
        kakaoSignInManager.onDestroy();
//        faceBookLoginManager.onDestroy();
        super.onDestroy();
    }

    public void goToMain(View view) {
        onBackPressed();
    }

    private void signIn() {
        UserSignInModel signIn = new UserSignInModel(ET_email.getText().toString(), ET_password.getText().toString());
        Call<RequestModel<UserModel>> res = Net.getInstance().getFactoryIm().userSignIn(signIn);
        res.enqueue(new Callback<RequestModel<UserModel>>() {
            @Override
            public void onResponse(Call<RequestModel<UserModel>> call, Response<RequestModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    RequestModel<UserModel> result = response.body();
                    Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signIn - onResponse(Call, Response)", "isSuccess ");
                    Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signIn - onResponse(Call, Response)", "body : " + result);
                    switch (result.getSuccess()) {
                        case CODE_SUCCESS:
                            progressManager.action(new OnProgressAction() {
                                @Override
                                public void run() {
                                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.WARNING_TYPE
                                            , resourceManager.getResourceString(R.string.signIn_fail_alert_title_success)
                                            , resourceManager.getResourceString(R.string.signIn_fail_alert_content_success), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    startActivity(new Intent(SignIn.this, Main.class));
                                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                                }
                                            }).show();
                                }
                            });
                            break;
                        case CODE_INCORRECT:
                            progressManager.action(new OnProgressAction() {
                                @Override
                                public void run() {
                                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE
                                            , resourceManager.getResourceString(R.string.signIn_fail_alert_title_fail)
                                            , resourceManager.getResourceString(R.string.signIn_fail_alert_content_fail2), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                }
                                            }).show();
                                }
                            });
                            break;
                        case CODE_SIGNED_OUT:
                            progressManager.action(new OnProgressAction() {
                                @Override
                                public void run() {
                                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.SUCCESS_TYPE
                                            , resourceManager.getResourceString(R.string.signIn_fail_alert_title_fail)
                                            , resourceManager.getResourceString(R.string.signIn_fail_alert_content_fail3), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                }
                                            }).show();
                                }
                            });
                            break;
                    }


                } else {
                    Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signIn - onResponse(Call, Response)", "isFail");
                    netFail();
                }
            }

            @Override
            public void onFailure(Call<RequestModel<UserModel>> call, Throwable t) {
                netFail();
            }
        });
    }

    private void netFail() {
        AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signIn_fail_alert_title_fail)), resourceManager.getResourceString((R.string.signIn_fail_alert_content_fail2))).show();
    }
}
