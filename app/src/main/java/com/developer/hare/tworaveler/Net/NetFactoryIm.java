package com.developer.hare.tworaveler.Net;


import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.FeedItemModel;
import com.developer.hare.tworaveler.Model.ProfileModel;
import com.developer.hare.tworaveler.Model.Request.RequestArrayModel;
import com.developer.hare.tworaveler.Model.Request.RequestModel;
import com.developer.hare.tworaveler.Model.Response.SceduleResModel;
import com.developer.hare.tworaveler.Model.Response.UserResModel;
import com.developer.hare.tworaveler.Model.Response.UserSignUpModel;
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
    Call<RequestModel<UserModel>> userSignUp(@Body UserSignUpModel model);

    // 일정 등록
    @POST("/trips/insert_trip")
    Call<RequestModel<ScheduleModel>> insertSchedule(@Body SceduleResModel model);

    @Multipart
    @POST("upload")
    Call<RequestArrayModel> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    // #############################################################################################
    // MODIFY
    // #############################################################################################

    // 프로필 정보 수정
    @POST("/users/profile/modify")
    Call<RequestModel<ProfileModel>> modifyProfile();

    // 일정 수정
    @POST("/trips/update_trip")
    Call<RequestModel<ScheduleModel>> modifySchedule(@Body SceduleResModel model);


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
    Call<RequestModel<UserModel>> userSignIn(@Body UserResModel model);

    // 여행 가방 목록
    @GET("/backpack/get_category_backpack")
    Call<RequestArrayModel<BagModel>> selectBagList(@Query("user_no") int user_no, @Query("category_theme") String category_theme);

    // 도시 검색
    @GET("/search/city")
    Call<RequestArrayModel<CityModel>> searchCity(@Query("q") String q);

    // 여행 별 일정 조회
    @GET("/trips/find_trip")
    Call<RequestModel<ScheduleModel>> selectSchedule(@Query("trip_no") int trip_no);

    // 프로필 정보 얻기
    @GET("/profileSet")
    Call<RequestArrayModel<ProfileModel>> getProfile();

    // 프로필 정보 조회
    @GET("/users/profile")
    Call<RequestModel<ProfileModel>> selectProfile(@Query("user_no") int user_no);

    // 내 여행 목록 조회
    @GET("/trips/mytrip/{user_no}")
    Call<RequestModel<ScheduleModel>> selectMyScheduleList(@Path("user_no") int user_no);

    // 피드 정보 조회
    @GET("/feed/:scrollCount")
    Call<RequestArrayModel<FeedItemModel>> selectFeedList(@Query("scrollCount") int scrollCount);

    // #############################################################################################
    // DELETE
    // #############################################################################################

    // 로그 아웃
    @POST("/users/logout")
    Call<RequestModel<String>> userLogout();

    // 회원 탈퇴
    @POST("/users/sign_out")
    Call<RequestModel<String>> userSignOut(@Body UserResModel model);

}

/*

    // 프로필 정보 얻기
    @GET("/profileSet/{idx}")
    Call<RequestArrayModel<ProfileModel>> getProfile(@Path("idx") int idx);
 */
