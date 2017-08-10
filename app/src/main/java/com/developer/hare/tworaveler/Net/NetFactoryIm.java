package com.developer.hare.tworaveler.Net;


import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.ProfileModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.UserSignInModel;
import com.developer.hare.tworaveler.Model.Response.UserSignUpModel;
import com.developer.hare.tworaveler.Model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetFactoryIm {

    @POST("/users/sign_up")
    Call<RequestModel<UserModel>> userSignUp(@Body UserSignUpModel model);

    @POST("/users/email_login")
    Call<RequestModel<UserModel>> userSignIn(@Body UserSignInModel model);

//    아마존주소:3001/bagList
    @GET("/bagList")
    Call<RequestArrayModel<BagModel>> getBagList();

    @GET("/search/city")
    Call<RequestArrayModel<CityModel>> searchCity(@Query("q") String q);

    @GET("/profileSet")
    Call<RequestArrayModel<ProfileModel>> getProfile();


}

