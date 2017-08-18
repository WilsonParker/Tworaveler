package com.developer.hare.tworaveler.Net;


import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.FeedItemModel;
import com.developer.hare.tworaveler.Model.ProfileModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.Request.SceduleResModel;
import com.developer.hare.tworaveler.Model.Request.UserResModel;
import com.developer.hare.tworaveler.Model.Request.UserSignUpModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetFactoryIm {

    // #############################################################################################
    // INSERT
    // #############################################################################################

    // 회원 가입
    @POST("/users/sign_up")
    Call<ResponseModel<UserModel>> userSignUp(@Body UserSignUpModel model);

    // 일정 등록
    @POST("/trips/insert_trip")
    Call<ResponseModel<ScheduleModel>> insertSchedule(@Body SceduleResModel model);

    @Multipart
    @POST("upload")
    Call<ResponseArrayModel> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    // #############################################################################################
    // MODIFY
    // #############################################################################################

    // 프로필 정보 수정
    @POST("/users/profile/modify")
    Call<ResponseModel<ProfileModel>> modifyProfile();

    // 일정 수정
    @POST("/trips/update_trip")
    Call<ResponseModel<ScheduleModel>> modifySchedule(@Body SceduleResModel model);


    // #############################################################################################
    // SELECT
    // #############################################################################################

    // 로그인
    /*
     * 200 : success
     * 201 : email or password incorrect
     * 202 : user was signed out
     */
    @POST("/users/email_login")
    Call<ResponseModel<UserModel>> userSignIn(@Body UserResModel model);

    // 여행 가방 목록
    @GET("/backpack/get_category_backpack")
    Call<ResponseArrayModel<BagModel>> selectBagList(@Query("user_no") int user_no, @Query("category_theme") String category_theme);

    // 도시 검색
    @GET("/search/city")
    Call<ResponseArrayModel<CityModel>> searchCity(@Query("q") String q);

    // 여행 별 일정 조회
    @GET("/trips/find_trip")
    Call<ResponseModel<ScheduleModel>> selectSchedule(@Query("trip_no") int trip_no);

    // 프로필 정보 얻기
    @GET("/profileSet")
    Call<ResponseArrayModel<ProfileModel>> getProfile();

    // 프로필 정보 조회
    @GET("/users/profile")
    Call<ResponseModel<ProfileModel>> selectProfile(@Query("user_no") int user_no);

    // 내 여행 목록 조회
    @GET("/trips/mytrip/{user_no}")
    Call<ResponseModel<ScheduleModel>> selectMyScheduleList(@Path("user_no") int user_no);

    // 피드 정보 조회
    @GET("/feed/:scrollCount")
    Call<ResponseArrayModel<FeedItemModel>> selectFeedList(@Query("scrollCount") int scrollCount);

    // #############################################################################################
    // DELETE
    // #############################################################################################

    // 로그 아웃
    @POST("/users/logout")
    Call<ResponseModel<String>> userLogout();

    // 회원 탈퇴
    @POST("/users/sign_out")
    Call<ResponseModel<String>> userSignOut(@Body UserResModel model);

}

/*

    // 프로필 정보 얻기
    @GET("/profileSet/{idx}")
    Call<ResponseArrayModel<ProfileModel>> getProfile(@Path("idx") int idx);
 */
