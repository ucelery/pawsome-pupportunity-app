package com.example.pawsomepuppertunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pawsomepuppertunity.model.Dog;
import com.example.pawsomepuppertunity.service.DogApi;
import com.example.pawsomepuppertunity.service.RetrofitService;
import com.example.pawsomepuppertunity.view.DogAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDash extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        recyclerView = findViewById(R.id.dogList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDogs();

        addDog();

    }

    private void addDog() {
        ImageButton addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminDogForm.class);
                startActivity(intent);
            }
        });
    }

    private void loadDogs() {
        RetrofitService retrofitService = new RetrofitService();
        DogApi dogApi = retrofitService.getDogApi();
        dogApi.getAllDogs()
                .enqueue(new Callback<List<Dog>>() {
                    @Override
                    public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Dog>> call, Throwable t) {
                        Toast.makeText(AdminDash.this, "failed to load dogs", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Dog> dogList) {
        DogAdapter dogAdapter = new DogAdapter(dogList);
        recyclerView.setAdapter(dogAdapter);

    }
}