package com.developer.hare.tworaveler.FaceBook.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.developer.hare.tworaveler.Activity.Main;
import com.developer.hare.tworaveler.Data.SessionManager;
import com.developer.hare.tworaveler.Kakao.Util.KakaoSignManager;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.UserModel;
import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.Util.Log_HR;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Arrays.asList;

/**
 * Created by Hare on 2017-07-25.
 */

public class FaceBookLoginManager {
    private Class selfCls = getClass();
    private Activity activity;
    private CallbackManager callbackManager;
    private final String[] params = {"id", "first_name", "last_name", "email", "gender", "birthday", "location", "picture"};

    private LoginManager loginManager;
    private Button BT_fbLogin;
    FacebookCallback callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Toast.makeText(activity, "페이스북 로그인에 성공했습니다", Toast.LENGTH_SHORT).show();
            signWith(loginResult.getAccessToken().getUserId());
//            getUserInfo(loginResult);
        }

        @Override
        public void onCancel() {
            Log_HR.log(Log_HR.LOG_INFO, selfCls, " onCancel()", "onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log_HR.log(Log_HR.LOG_INFO, selfCls, " onError(FacebookException error)", "oNError : " + error.getMessage());
        }
    };

    public FaceBookLoginManager(Activity activity) {
        this.activity = activity;
        init();
    }

    public void init() {
        callbackManager = CallbackManager.Factory.create();

     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mainWebView, true);
        }*/

//        setLoginPermission(activity);
//        LoginButton loginButton = activity.findViewById(R.id.facebook_sign_in$login_button);
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, callback);
        checkSession();
    }

    private void checkSession() {
        if (AccessToken.getCurrentAccessToken() == null)
            return; // not login
        else
            setLoginPermission(activity);
    }

    public void onLoginClick() {
        setLoginPermission(activity);
    }

    public void setLoginPermission(Activity activity) {
        loginManager.logInWithReadPermissions(
                activity,
                asList("public_profile"));
    }

    public void getUserInfo(LoginResult loginResult) {
        com.facebook.AccessToken accessToken = loginResult.getAccessToken();
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserINfo(LoginResult)", "Token : " + accessToken.getToken());
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserINfo(LoginResult)", "ApplicationId : " + accessToken.getApplicationId());
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserINfo(LoginResult)", "UserId : " + accessToken.getUserId());
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserINfo(LoginResult)", "Source : " + accessToken.getSource());
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserINfo(LoginResult)", "toString : " + accessToken.toString());
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserINfo(LoginResult)", "Expires : " + accessToken.getExpires());

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log_HR.log(Log_HR.LOG_INFO, selfCls, "getUserInfo(LoginResult) onCompleted(JSONObject, GraphResponse)", response.toString());
                        // Get facebook data from login
//                        Bundle bFacebookData = getFacebookData(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location, picture"); // Parámetros que pedimos a facebook
//        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            for (String param : params) {
                String v = object.getString(param);
                if (!v.isEmpty())
                    Log_HR.log(Log_HR.LOG_INFO, selfCls, "getFacebookData(JSONObject)", param + " :  " + object.getString(param));
            }

            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            if (object.has("profile_pic"))
                bundle.putString("profile_pic", object.getString("profile_pic"));

            return bundle;
        } catch (JSONException e) {
            Log_HR.log(selfCls, "getFacebookData(JSONObject)", e);
        }
        return null;
    }

    private void signWith(String id) {
        Log_HR.log(Log_HR.LOG_INFO, selfCls, "signWith()", "signWith");

        Net.getInstance().getFactoryIm().facebookSignIn(id).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                Log_HR.log(KakaoSignManager.class, "onResponse(Call<ResponseModel<ScheduleModel>> call, Response<ResponseModel<ScheduleModel>> response)", response);
                if (response.isSuccessful()) {
                    SessionManager.getInstance().setUserModel(response.body().getResult());
                    Intent intent = new Intent(activity, Main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                    activity.finish();
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

    public void onLogout() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError() != null) {
                    Log_HR.log(FaceBookLoginManager.class, "getFacebookData(JSONObject)", graphResponse.getError().getException());
                }
                Log_HR.log(Log_HR.LOG_INFO, FaceBookLoginManager.class, "getFacebookData(JSONObject)", "facebook logout");
                LoginManager.getInstance().logOut();
                SessionManager.getInstance().logout(activity);
            }
        }).executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
