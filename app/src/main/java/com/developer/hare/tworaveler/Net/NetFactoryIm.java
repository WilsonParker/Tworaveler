package com.developer.hare.tworaveler.Net;


import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.ProfileModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.SceduleRegistModel;
import com.developer.hare.tworaveler.Model.Response.UserSignInModel;
import com.developer.hare.tworaveler.Model.Response.UserSignUpModel;
import com.developer.hare.tworaveler.Model.SceduleModel;
import com.developer.hare.tworaveler.Model.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NetFactoryIm {

    // #############################################################################################
    // INSERT
    // #############################################################################################

    // 회원 가입
    @POST("/users/sign_up")
    Call<RequestModel<UserModel>> userSignUp(@Body UserSignUpModel model);

    // 일정 등록
    @POST("/trips/insert_trip")
    Call<RequestModel<SceduleModel>> registPlan(@Body SceduleRegistModel model);

    @Multipart
    @POST("upload")
    Call<RequestArrayModel> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    // #############################################################################################
    // MODIFY
    // #############################################################################################


    // #############################################################################################
    // SELECT
    // #############################################################################################

    // 로그인
    @POST("/users/email_login")
    Call<RequestModel<UserModel>> userSignIn(@Body UserSignInModel model);

    // 여행 가방 목록
    @GET("/bagList")
    Call<RequestArrayModel<BagModel>> getBagList();

    // 도시 검색
    @GET("/search/city")
    Call<RequestArrayModel<CityModel>> searchCity(@Query("q") String q);

    // 프로필 정보 얻기
    @GET("/profileSet")
    Call<RequestArrayModel<ProfileModel>> getProfile();

    // #############################################################################################
    // DELETE
    // #############################################################################################


}

