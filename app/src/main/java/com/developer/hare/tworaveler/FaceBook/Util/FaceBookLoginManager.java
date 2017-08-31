package com.developer.hare.tworaveler.FaceBook.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.developer.hare.tworaveler.Util.Log_HR;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Hare on 2017-07-25.
 */

public class FaceBookLoginManager {
    private Activity activity;
    private CallbackManager callbackManager;
    FacebookCallback callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Toast.makeText(activity, "페이스북 로그인에 성공했습니다", Toast.LENGTH_SHORT).show();
            getUserInfo(loginResult);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    private LoginManager loginManager;
    private Button BT_fbLogin;

    public FaceBookLoginManager(Activity activity) {
        this.activity = activity;
        init();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
//        loginButton.registerCallback(callback);
    }

    public void onLoginClick() {
        setLoginPermission(activity);

    }

    public void setLoginPermission(Activity activity) {
        loginManager.logInWithReadPermissions(
                activity,
                Arrays.asList("public_profile"));
    }

    public void getUserInfo(LoginResult loginResult) {
        com.facebook.AccessToken accessToken = loginResult.getAccessToken();
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserINfo(LoginResult)", "Token : " + accessToken.getToken());
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserINfo(LoginResult)", "ApplicationId : " + accessToken.getApplicationId());
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserINfo(LoginResult)", "UserId : " + accessToken.getUserId());
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserINfo(LoginResult)", "Source : " + accessToken.getSource());
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserINfo(LoginResult)", "toString : " + accessToken.toString());
        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserINfo(LoginResult)", "Expires : " + accessToken.getExpires());

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log_HR.log(Log_HR.LOG_INFO, getClass(), "getUserInfo(LoginResult) onCompleted(JSONObject, GraphResponse)", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location, picture"); // Parámetros que pedimos a facebook
//        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private Bundle getFacebookData(JSONObject object) {
        String[] params = {"id", "first_name", "last_name", "email", "gender", "birthday", "location", "picture"};
        try {
            for (String param : params) {
                String v = object.getString(param);
                if (!v.isEmpty())
                    Log_HR.log(Log_HR.LOG_INFO, getClass(), "getFacebookData(JSONObject)", param + " :  " + object.getString(param));
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
            Log_HR.log(getClass(), "getFacebookData(JSONObject)", e);
        }
        return null;
    }

    public void onDestroyed() {

    }
}
