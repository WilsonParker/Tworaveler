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
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.FaceBook.Util.FaceBookLoginManager;
import com.developer.hare.tworaveler.Kakao.Util.KakaoSignManager;
import com.developer.hare.tworaveler.Listener.OnProgressAction;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.Request.UserResModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
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

import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_EMAIL_PW_INCORRECT;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SIGNOUT_USER;
import static com.developer.hare.tworaveler.Data.DataDefinition.Network.CODE_SUCCESS;

public class SignIn extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private Button BT_main, BT_facebook, BT_kakao;
    private TextView TV_signUp;
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

        TV_signUp = uiFactory.createView(R.id.login$BT_signUp);
        TV_signUp.setOnClickListener(new View.OnClickListener() {
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
                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signIn_alert_title_fail)), resourceManager.getResourceString((R.string.signIn_alert_content_faile))).show();
                }
            }
        });

//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "init()", "key hash :  " + KeyManager.getInstance().getKeyHash(this));
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(ET_email);
        textViews.add(ET_password);
        FontManager.getInstance().setFont(textViews, "Roboto-Medium.ttf");
        FontManager.getInstance().setFont(TV_signUp, "Roboto-MediumItalic.ttf");
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
//        kakaoSignInManager.setLoginButton();
        ET_email.setText("");
        ET_password.setText("");
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
        progressManager.actionWithState(new OnProgressAction() {
            @Override
            public void run() {
                UserResModel signIn = new UserResModel(ET_email.getText().toString(), ET_password.getText().toString());
                Call<ResponseModel<UserModel>> res = Net.getInstance().getFactoryIm().userSignIn(signIn);
                res.enqueue(new Callback<ResponseModel<UserModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                        if (response.isSuccessful()) {
                            progressManager.endRunning();
                            ResponseModel<UserModel> result = response.body();
//                            Log_HR.log(Log_HR.LOG_INFO, SignIn.class, "signIn - onResponse(Call, Response)", "body : " + result.getResult());
                            switch (result.getSuccess()) {
                                case CODE_SUCCESS:
                                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.SUCCESS_TYPE
                                            , resourceManager.getResourceString(R.string.signIn_alert_title_success)
                                            , resourceManager.getResourceString(R.string.signIn_alert_content_success), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                    AlertManager.getInstance().dismissAlertSelectionMode();
                                                    sweetAlertDialog.dismiss();
//                                                    startActivity(new Intent(SignIn.this, Main.class));
                                                    UserModel model = result.getResult();
                                                    if (model.getFollowees() == null)
                                                        model.setFollowees(new ArrayList<>());
                                                    if (model.getFollowers() == null)
                                                        model.setFollowers(new ArrayList<>());
                                                    SessionManager.getInstance().setUserModel(model);
                                                    onBackPressed();
                                                }
                                            }).show();
                                    break;
                                case CODE_EMAIL_PW_INCORRECT:
                                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.WARNING_TYPE
                                            , resourceManager.getResourceString(R.string.signIn_alert_title_fail)
                                            , resourceManager.getResourceString(R.string.signIn_alert_content_fail2), "확인", new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismiss();
                                                }
                                            }).show();
                                    break;
                                case CODE_SIGNOUT_USER:
                                    AlertManager.getInstance().createAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE
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
                            Log_HR.log(Log_HR.LOG_INFO, SignIn.class, "signIn - onResponse(Call, Response)", "isFail");
                            netFail();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                        netFail();
                    }
                });
            }
        });
    }

    private void netFail() {
        progressManager.endRunning();
        AlertManager.getInstance().showNetFailAlert(this, R.string.signIn_alert_title_fail, R.string.signIn_alert_content_fail4);
    }
}
