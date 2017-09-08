package com.developer.hare.tworaveler.Kakao.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.developer.hare.tworaveler.Activity.Main;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.UI.UIFactory;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.lang.reflect.Method;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hare on 2017-07-26.
 */

public class KakaoSignManager {
    private SessionCallback callback;
    private Activity activity;
    private UIFactory uiFactory;
    //    private com.kakao.usermgmt.LoginButton BT_kakaoLogin;
    private KakaoLoginButton BT_kakaoLogin;
    private Button BT_kakaoLogout;
    private Class selfCls = getClass();

    public KakaoSignManager(Activity activity) {
        this.activity = activity;
        init();
    }

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     */

    public void init() {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        uiFactory = UIFactory.getInstance(activity);
        BT_kakaoLogin = new KakaoLoginButton(activity);
        requestMe();
    }

    public void onLoginClick() {
        BT_kakaoLogin.call();
    }

    public void onLogOut() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log_HR.log(Log_HR.LOG_INFO, selfCls, "onCompleteLogout()", "kakao logout");
                SessionManager.getInstance().logout(activity);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String accessToken, refreshToken, thirdParty;
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            accessToken = Session.getCurrentSession().getAccessToken();
            refreshToken = Session.getCurrentSession().getRefreshToken();
            thirdParty = "K";
            Log_HR.log(Log_HR.LOG_INFO, selfCls, "onActivityResult(int requestCode, int resultCode, Intent data)", String.format("%s , %s, %s", accessToken, refreshToken, thirdParty));
//            requestMe();
            //setSignInInfo();
        }
    }

    public void onDestroy() {
        Session.getCurrentSession().removeCallback(callback);
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(activity, Main.class);
//        activity.startActivity(intent);
//        activity.finish();
    }

    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    Log_HR.log(selfCls, "request() onFailure(ErrorResult)", message, errorResult.getException());
//                    activity.finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log_HR.log(Log_HR.LOG_INFO, selfCls, "onSessionClosed()", "onSessionClosed");
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                Log_HR.log(Log_HR.LOG_INFO, selfCls, "onNotSignedUp()", "onNotSignedUp");
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
//                Log_HR.log(Log_HR.LOG_INFO, KakaoSignManager.class, "onSuccess(userProfile)", "UserProfile : " + userProfile);
//                Log_HR.log(Log_HR.LOG_INFO, KakaoSignManager.class, "onSuccess(userProfile)", String.format("%s, %s",userProfile.getId(),userProfile.getUUID()));
                String accessToken, refreshToken = "", thirdParty;
                accessToken = Session.getCurrentSession().getAccessToken();
//                refreshToken = Session.getCurrentSession().getRefreshToken();
                thirdParty = "K";
//                Log_HR.log(Log_HR.LOG_INFO, getClass(), "onSuccess(UserProfile userProfile)", String.format("%s , %s, %s", accessToken, refreshToken, thirdParty));
//                redirectMainActivity(); // 로그인 성공시 MainActivity로
                Toast.makeText(activity, "카카오톡  로그인에 성공했습니다", Toast.LENGTH_SHORT).show();
                signWith(userProfile.getId() + "");
            }
        });
    }

    private void signWith(String id) {
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "signWith()", "signWith");

        Net.getInstance().getFactoryIm().kakaoSignIn(id).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                Log_HR.log(KakaoSignManager.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", response);
                if (response.isSuccessful()) {
                    SessionManager.getInstance().setUserModel(response.body().getResult());
                } else {
                    Log_HR.log(Log_HR.LOG_INFO, selfCls, "onResponse(Call<ResponseModel<ScheduleModel>> call", "response is not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                Log_HR.log(selfCls, "onFailure(Call<ResponseModel<UserModel>> call, Throwable t)", t);
            }
        });
    }

    private void redirectMainActivity() {
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "redirectMainActivity()", "redirectMainActivity");
//        final Intent intent = new Intent(activity, Main.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        activity.startActivity(intent);
//        activity.finish();
        Intent intent = new Intent(activity, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    protected void redirectLoginActivity() {
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "redirectLoginActivity()", "redirectLoginActivity");
        /*final Intent intent = new Intent(activity, SignIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        activity.finish();*/
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log_HR.log(Log_HR.LOG_INFO, selfCls, "onSessionOpened()", "sessionOpen");
//            redirectSignupActivity();
            requestMe();
            redirectMainActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log_HR.log(Log_HR.LOG_INFO, selfCls, "onSessionOpened(KakaoException)", "sessionOpen");
            if (exception != null) {
                Log_HR.log(getClass(), "onSessionOpenFailed(KaKaoException)", exception);
            }
        }
    }

    protected class KakaoLoginButton extends LoginButton {

        public KakaoLoginButton(Context context) {
            super(context);
        }

        public void call() {
            try {
                Method methodGetAuthTypes = LoginButton.class.getDeclaredMethod("getAuthTypes");
                methodGetAuthTypes.setAccessible(true);
                final List<AuthType> authTypes = (List<AuthType>) methodGetAuthTypes.invoke(this);
                if (authTypes.size() == 1) {
                    Session.getCurrentSession().open(authTypes.get(0), (Activity) getContext());
                } else {
                    Method methodOnClickLoginButton = LoginButton.class.getDeclaredMethod("onClickLoginButton", List.class);
                    methodOnClickLoginButton.setAccessible(true);
                    methodOnClickLoginButton.invoke(this, authTypes);
                }
            } catch (Exception e) {
                Session.getCurrentSession().open(AuthType.KAKAO_ACCOUNT, (Activity) getContext());
            }
        }
    }
}
