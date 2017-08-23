package com.developer.hare.tworaveler.Net;

import com.developer.hare.tworaveler.Util.File.FileManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tacademy on 2017-02-23.
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orgRequest = chain.request();
        Request request = orgRequest.newBuilder()
//                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(orgRequest.method(), orgRequest.body())
                .build();

        Response originalResponse = chain.proceed(request);

//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "intercept(Chain)", "Set-Cookie is empty? " + (originalResponse.headers("Set-Cookie")));
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "intercept(Chain)", (originalResponse.headers().toString()));
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "intercept(Chain)", "Set-Cookie is empty? " + (originalResponse.headers("set-cookie")));
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "intercept(Chain)", "Cookie is empty? " + (originalResponse.headers("Cookie")));
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "intercept(Chain)", "Cookies is empty? " + (originalResponse.headers("Cookies")));
//        Log_HR.log(Log_HR.LOG_INFO, getClass(), "intercept(Chain)", "connect.sid is empty? " + (originalResponse.headers("connect.sid")));

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            // Preference에 cookies를 넣어주는 작업을 수행
            FileManager.getInstance().savePreferences(FileManager.KEY_SESSION, cookies);
        }
        return originalResponse;
    }
}
