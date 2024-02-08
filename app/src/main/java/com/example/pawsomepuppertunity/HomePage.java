package com.example.pawsomepuppertunity;

import static java.io.File.createTempFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pawsomepuppertunity.model.Dog;
import com.example.pawsomepuppertunity.service.DogApi;
import com.example.pawsomepuppertunity.service.RetrofitService;
import com.example.pawsomepuppertunity.view.DogAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {


    private RecyclerView recyclerView;
    private DogAdapter dogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerView = findViewById(R.id.dogList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDogs();
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
                        Toast.makeText(HomePage.this, "failed to load dogs", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Dog> dogList) {
        dogAdapter = new DogAdapter( HomePage.this,dogList, true);
        recyclerView.setAdapter(dogAdapter);


        // Set click listener for each dog item
        dogAdapter.setOnItemClickListener(new DogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Dog dog) {
                // Handle item click
                Intent intent = new Intent(HomePage.this, DogAdoptionForm.class);
                intent.putExtra("dogId", dog.getId()); // Pass dog ID to the next activity
                intent.putExtra("dogName", dog.getName());
                intent.putExtra("dogAge", dog.getAge());
                intent.putExtra("dogBreed", dog.getBreed());
                intent.putExtra("dogSex", dog.getSex());
                intent.putExtra("dogSize", dog.getSize());
                intent.putExtra("dogBirthday", dog.getBirthday());
                intent.putExtra("dogDescription", dog.getDescription());

                // Decode Base64 string to byte array
                byte[] bytesDecodedImage = Base64.decode(dog.getImage(), Base64.DEFAULT);

                // Convert byte array to Bitmap
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytesDecodedImage, 0, bytesDecodedImage.length);


                File imageFile;
                try {
                    imageFile = createTempFile("dog_image", ".png", HomePage.this.getCacheDir());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Bitmap bitmap = bitmapImage; // Assuming the image is accessible from the Dog object
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Adjust compression quality if needed
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                String filePath = imageFile.getAbsolutePath();

                Uri imagePath = Uri.fromFile(imageFile);

                intent.putExtra("dogImage", imagePath);

                startActivity(intent);
            }
        });

    }
}