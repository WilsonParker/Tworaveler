package com.developer.hare.tworaveler.Net;

import com.developer.hare.tworaveler.Util.Log_HR;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hare on 2017-08-01.
 */

public class Net {
    private static final Net ourInstance = new Net();
    private static final String U = "http://13.124.128.125:3002";
    private static Retrofit retrofit;
    private NetFactoryIm netFactoryIm;

    public Net() {
        init();
    }

    private void init() {
        // init cookie manager
        CookieHandler cookieHandler = new CookieManager();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        cookieHandler.setDefault(cookieManager);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor())
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(U)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Net getInstance() {
        return ourInstance;
    }

    public static String getU() {
        return U;
    }

    public NetFactoryIm getFactoryIm() {
        if (netFactoryIm == null)
            netFactoryIm = retrofit.create(NetFactoryIm.class);
        return netFactoryIm;
    }

    private void urlConnect() {
        URL url;
        try {
            url = new URL("http://13.124.128.125:3002/users/email_login");
            String params = "email=a7@naver.com&pw=aaaa1111";
            String result = "";
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

            httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDefaultUseCaches(false);

            OutputStream outputStream = httpUrlConnection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();

            InputStream inputStream = httpUrlConnection.getInputStream();
            String buffer = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((buffer = bufferedReader.readLine()) != null) {
                result += buffer;
            }
            bufferedReader.close();

            Log_HR.log(Log_HR.LOG_INFO, getClass(), "urlConnect()", "result : " + result);

            Map<String, List<String>> resMap = httpUrlConnection.getHeaderFields();
            for (String key : resMap.keySet()) {
                Log_HR.log(Log_HR.LOG_INFO, getClass(), "urlConnect()", "key :  : " + key + " / " + resMap.get(key));
            }

        } catch (Exception e) {
            Log_HR.log(getClass(), "urlConnect()", e);
        }
    }
}
