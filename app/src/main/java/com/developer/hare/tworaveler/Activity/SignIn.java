package com.developer.hare.tworaveler.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.developer.hare.tworaveler.Data.DataDefinition;
import com.developer.hare.tworaveler.Data.ResourceManager;
import com.developer.hare.tworaveler.FaceBook.Util.FaceBookLoginManager;
import com.developer.hare.tworaveler.Kakao.Util.KakaoSignManager;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.UserSignInModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.R;
import com.developer.hare.tworaveler.UI.AlertManager;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.KeyManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class SignIn extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    private Button BT_main, BT_signUp;
    private EditText ET_email, ET_password;
    private ImageButton IV_signIn;
    private com.kakao.usermgmt.LoginButton BT_kakaoLogin;
    private com.facebook.login.widget.LoginButton BT_facebookLogin;
    private KakaoSignManager kakaoSignInManager;
    private FaceBookLoginManager faceBookLoginManager;
    private UIFactory uiFactory;
    private ResourceManager resourceManager;


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
        faceBookLoginManager = new FaceBookLoginManager(SignIn.this);
        kakaoSignInManager = new KakaoSignManager(SignIn.this);

        BT_main = uiFactory.createView(R.id.login$BT_main);
        BT_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), Main.class));
            }
        });
        BT_signUp = uiFactory.createView(R.id.login$BT_signUp);
        BT_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), SignUp.class));
            }
        });
        BT_facebookLogin = uiFactory.createView(R.id.facebook_sign_in$login_button);
        BT_facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onFacebookLogin) {
                    faceBookLoginManager.init();
                    Log_HR.log(Log_HR.LOG_INFO, SignIn.class, "BT_facebookLogin.onClick(View)", "facebook SignIn");
                }
            }
        });

        ET_email = uiFactory.createView(R.id.activty_login$ET_email);
        ET_password = uiFactory.createView(R.id.activty_login$ET_password);
        IV_signIn = uiFactory.createView(R.id.activity_login$IV_signIn);
        IV_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validCheck()) {
                    signIn();
                } else {
                    ResourceManager resourceManager = ResourceManager.getInstance();
                    AlertManager.getInstance().showAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signIn_fail_alert_title_fail)), resourceManager.getResourceString((R.string.signIn_fail_alert_content_faile)));
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

    private String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log_HR.log(SignIn.class, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
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

    private void signIn() {
        UserSignInModel signIn = new UserSignInModel(ET_email.getText().toString(), ET_password.getText().toString());
        Call<RequestModel<UserModel>> res = Net.getInstance().getFactoryIm().userSignIn(signIn);
        res.enqueue(new Callback<RequestModel<UserModel>>() {
            @Override
            public void onResponse(Call<RequestModel<UserModel>> call, Response<RequestModel<UserModel>> response) {
                UserModel result = response.body().getResult();
                Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signIn - onResponse(Call, Response)", "body : " + result);
                if (response.isSuccessful()) {
                    Log_HR.log(Log_HR.LOG_INFO, SignUp.class, "signIn - onResponse(Call, Response)", "isSuccess ");
                    startActivity(new Intent(getBaseContext(), Main.class));
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
        AlertManager.getInstance().showAlert(SignIn.this, SweetAlertDialog.ERROR_TYPE, resourceManager.getResourceString((R.string.signUp_fail_alert_title_fail)), resourceManager.getResourceString((R.string.signUp_fail_alert_content_fail2)));
    }
}
