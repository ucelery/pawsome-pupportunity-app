package com.example.pawsomepuppertunity.service;

import com.example.pawsomepuppertunity.model.Users;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/api/login")
    Call<ResponseBody> checkUsers (
            @Body Users users
            );
}
