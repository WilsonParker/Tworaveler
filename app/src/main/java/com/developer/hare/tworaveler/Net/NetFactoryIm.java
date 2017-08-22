package com.developer.hare.tworaveler.Net;


import com.developer.hare.tworaveler.Model.BagModel;
import com.developer.hare.tworaveler.Model.CityModel;
import com.developer.hare.tworaveler.Model.ProfileModel;
import com.developer.hare.tworaveler.Model.Request.LikeModel;
import com.developer.hare.tworaveler.Model.Request.UserReqModel;
import com.developer.hare.tworaveler.Model.Response.ResponseArrayModel;
import com.developer.hare.tworaveler.Model.Response.ResponseModel;
import com.developer.hare.tworaveler.Model.ScheduleDayRootModel;
import com.developer.hare.tworaveler.Model.ScheduleModel;
import com.developer.hare.tworaveler.Model.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    Call<ResponseModel<UserModel>> userSignUp(@Body UserReqModel model);

    // 일정 등록
    @POST("/trips/insert_trip")
    Call<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> insertSchedule(@Body ScheduleModel model);

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
    Call<ResponseModel<UserModel>> modifyProfile(@Body UserReqModel model);

    // 일정 수정
    @POST("/trips/update_trip")
    Call<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> modifySchedule(@Body ScheduleModel model);

    // 좋아요
    @POST("/feed/like")
    Call<ResponseModel<LikeModel>> modifyLike(@Body LikeModel model);

    // 좋아요 취소
    @POST("/feed/unlike")
    Call<ResponseModel<LikeModel>> modifyUnLike(@Body LikeModel model);


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
    Call<ResponseModel<UserModel>> userSignIn(@Body UserReqModel model);

    // 여행 가방 목록
    @GET("/backpack/get_category_backpack")
    Call<ResponseArrayModel<BagModel>> selectBagList(@Query("user_no") int user_no, @Query("category_theme") String category_theme);

    // 도시 검색
//    @GET("/search/city")
//    Call<ResponseArrayModel<CityModel>> searchCity(@Query("q") String q);

    // 도시 검색
    @GET("/location/get_location")
    Call<ResponseArrayModel<CityModel>> searchCity(@Query("city") String city);

    // 여행 별 상세일정 조회
    @GET("/trips/find_dtrip/{trip_no}/{trip_date}")
    Call<ResponseArrayModel<ScheduleDayRootModel>> selectDetailSchedule(@Query("trip_no") int trip_no, @Query("trip_date") String trip_date);

    // 프로필 정보 얻기
    @GET("/profileSet")
    Call<ResponseArrayModel<ProfileModel>> getProfile();

    // 프로필 정보 조회
    @GET("/users/profile")
    Call<ResponseModel<ProfileModel>> selectProfile(@Query("user_no") int user_no);

    // 내 여행 목록 조회
    @GET("/trips/mytrip/{user_no}")
    Call<ResponseModel<com.developer.hare.tworaveler.Model.ScheduleModel>> selectMyScheduleList(@Path("user_no") int user_no);

    // 피드 정보 조회
    @GET("/feed/{user_no}/{scrollCount}")
    Call<ResponseArrayModel<ScheduleModel>> selectFeedList(@Path("user_no") int user_no,@Path("scrollCount") int scrollCount);

    // #############################################################################################
    // DELETE
    // #############################################################################################

    // 로그 아웃
    @POST("/users/logout")
    Call<ResponseModel<String>> userLogout(@Header("Cookie") String sessionID);

    // 회원 탈퇴
    @POST("/users/sign_out")
    Call<ResponseModel<String>> userSignOut(@Header("Cookie") String cookie, @Body UserReqModel model);




}
/*

    // 프로필 정보 얻기
    @GET("/profileSet/{idx}")
    Call<ResponseArrayModel<ProfileModel>> getProfile(@Path("idx") int idx);
 */
