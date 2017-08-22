package com.developer.hare.tworaveler.Net;

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
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor())
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
}
