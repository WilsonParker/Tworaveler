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
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(orgRequest.method(), orgRequest.body())
                .build();

        Response originalResponse = chain.proceed(request);

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
