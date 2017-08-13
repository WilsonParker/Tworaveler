package com.developer.hare.tworaveler.Kakao.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.developer.hare.tworaveler.Activity.Main;
import com.developer.hare.tworaveler.Activity.SignIn;
import com.developer.hare.tworaveler.R;
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

/**
 * Created by Hare on 2017-07-26.
 */

public class KakaoSignManager {
    private SessionCallback callback;
    private Activity activity;
    private UIFactory uiFactory;
//    private com.kakao.usermgmt.LoginButton BT_kakaoLogin;
    private KakaoLoginButton BT_kakaoLogin;
    private Button BT_logout;

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
//        BT_kakaoLogin = uiFactory.createView(R.id.activity_login$BT_kakao);
        BT_kakaoLogin = new KakaoLoginButton(activity);
        BT_logout = uiFactory.createView(R.id.login$com_kakao_logout);
        BT_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log_HR.log(Log_HR.LOG_INFO, getClass(), "BT_logout.onCompleteLogout()", "logout");
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                setLoginButton();
                                Intent intent = new Intent(activity, SignIn.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        });
                    }
                });
            }
        });
        setLoginButton();
    }

    public void onLoginClick() {
//        return BT_kakaoLogin.callOnClick();
        BT_kakaoLogin.call();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
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

    public void setLoginButton() {
        if (Session.getCurrentSession().isClosed()) {
//            BT_kakaoLogin.setVisibility(View.VISIBLE);
            BT_logout.setVisibility(View.INVISIBLE);
        } else {
//            BT_kakaoLogin.setVisibility(View.INVISIBLE);
            BT_logout.setVisibility(View.VISIBLE);
            requestMe();
        }
    }

    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log_HR.log(getClass(), "requesetMe() onFailure(ErrorResult)", message, errorResult.getException());

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    activity.finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Log_HR.log(Log_HR.LOG_INFO, KakaoSignManager.class, "onSuccess(userProfile)", "UserProfile : " + userProfile);
                Log_HR.log(Log_HR.LOG_INFO, KakaoSignManager.class, "onSuccess(userProfile)", "UserProfile : " + userProfile.toString());
//                redirectMainActivity(); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void redirectMainActivity() {
        activity.startActivity(new Intent(activity, Main.class));
        activity.finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(activity, SignIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        activity.finish();
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log_HR.log(Log_HR.LOG_INFO, getClass(), "onSessionOpened()", "sessionOpen");
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log_HR.log(Log_HR.LOG_INFO, getClass(), "onSessionOpened(KakaoException)", "sessionOpen");
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
                if(authTypes.size() == 1){
                    Session.getCurrentSession().open(authTypes.get(0), (Activity) getContext());
                } else {
                    Method methodOnClickLoginButton = LoginButton.class.getDeclaredMethod("onClickLoginButton",List.class);
                    methodOnClickLoginButton.setAccessible(true);
                    methodOnClickLoginButton.invoke(this,authTypes);
                }
            } catch (Exception e) {
                Session.getCurrentSession().open(AuthType.KAKAO_ACCOUNT, (Activity) getContext());
            }
        }
    }
}
