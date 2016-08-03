package com.android.project.util;

import com.android.project.model.Record;
import com.android.project.model.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Lobster on 19.06.16.
 */

public interface RestRequestService {

    @GET("get_last_records")
    Call<List<Record>> getLastRecords();

    @GET("get_user_info")
    Call<User> getUserInfo(@Query("name") String name);

    @GET("get_record")
    Call<Record> getRecordById(@Query("recordId") Long recordId);

    @GET("get_last_user_records")
    Call<List<Record>> getLastUserRecords(@Query("name") String name);

    @GET("get_next_records")
    Call<List<Record>> getNextRecords(@Query("recordId") Long recordId);

    @GET("get_users")
    Call<List<User>> getUsers(@Query("searchQuery") String searchQuery);

    @GET("get_next_user_records")
    Call<List<Record>> getNextUserRecords(@Query("name") String name, @Query("recordId") Long recordId);

    @GET("get_users_from_id")
    Call<List<User>> getUsersFromId(@Query("searchQuery") String searchQuery, @Query("userId") Long userId);

    @Multipart
    @POST("update_avatar")
    Call<User> updateAvatar(@Part("avatar\"; filename=\"*.png") RequestBody file, @Part("name") RequestBody name);

    @POST("save_vote")
    Call<Void> sendVote(@Query("userName") String userName, @Query("recordId") Long recordId, @Query("option") String option);

    @Multipart
    @POST("update_background")
    Call<User> updateBackground(@Part("background\"; filename=\"*.png") RequestBody file, @Part("name") RequestBody name);

    @Multipart
    @POST("add_record")
    Call<ResponseBody> addRecord(@Part("files\"; filename=\"*.png") List<RequestBody> files, @Part("options") List<RequestBody> options, @Part("name") RequestBody name);

}
