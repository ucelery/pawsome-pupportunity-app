package com.example.pawsomepuppertunity.service;

import static okhttp3.internal.Internal.instance;

import com.google.gson.Gson;

import okhttp3.internal.Internal;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static RetrofitService mInstance;
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.1.16.60:8080") //change to ur ip
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public static synchronized RetrofitService getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitService();
        }
        return mInstance;
    }

    public UserApi getUserApi() {
        return retrofit.create(UserApi.class);
    }

    public DogApi getDogApi() {
        return retrofit.create(DogApi.class);
    }
}


