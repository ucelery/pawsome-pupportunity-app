package com.example.pawsomepuppertunity.service;

import com.example.pawsomepuppertunity.model.Dog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
public interface DogApi {

    @GET("/api/")
    Call<List<Dog>> getAllDogs();

    @POST("/api/add-dog")
    Call<Dog> save(@Body Dog dog);

    @GET("/api/dogs/{id}")
    Call<Dog> getDog(@Path("id") int id);

    @PUT("/api/update-dog/{id}")
    Call<Dog> updateDog(@Path("id") int id, @Body Dog dog);

    @DELETE("/api/dogs/{id}")
    Call<Dog> deleteDog(@Path("id") int id);

}
