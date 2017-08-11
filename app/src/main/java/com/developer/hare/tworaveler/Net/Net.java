package com.developer.hare.tworaveler.Net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hare on 2017-08-01.
 */

public class Net {
    private static final Net ourInstance = new Net();
//        private static final String U = "http://13.124.128.125:3002";
//        private static final String U = "http://ec2-13-124-174-193.ap-northeast-2.compute.amazonaws.com:3002";
    private static final String U = "http://ec2-52-79-188-143.ap-northeast-2.compute.amazonaws.com:3001"; //TH
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(U)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private NetFactoryIm netFactoryIm;

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
