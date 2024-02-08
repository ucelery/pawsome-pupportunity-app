package com.example.pawsomepuppertunity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pawsomepuppertunity.model.Dog;
import com.example.pawsomepuppertunity.service.DogApi;
import com.example.pawsomepuppertunity.service.RetrofitService;
import com.github.dhaval2404.imagepicker.ImagePicker;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDogUpdate extends AppCompatActivity {

    TextView name, age, breed, sex, size, birthday, description;
    ImageButton image, update, delete;

    private ActivityResultLauncher<String> galleryLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dog_update);

        // Retrieve dogId from the previous intent
        int dogId = getIntent().getIntExtra("dogId", -1);
        String dogName = getIntent().getStringExtra("dogName");
        int dogAge = getIntent().getIntExtra("dogAge", -1);
        String dogBreed = getIntent().getStringExtra("dogBreed");
        String dogSex = getIntent().getStringExtra("dogSex");
        String dogSize = getIntent().getStringExtra("dogSize");
        String dogBirthday = getIntent().getStringExtra("dogBirthday");
        String dogDescription = getIntent().getStringExtra("dogDescription");
        String dogImage = getIntent().getStringExtra("dogImage");

        name = findViewById(R.id.dogUpdateName);
        age = findViewById(R.id.dogUpdateAge);
        breed = findViewById(R.id.dogUpdateBreed);
        sex = findViewById(R.id.dogUpdateSex);
        size = findViewById(R.id.dogUpdateSize);
        birthday = findViewById(R.id.dogUpdateBirthday);
        description = findViewById(R.id.dogUpdateDescription);
        image = findViewById(R.id.dogUpdateImageBtn);

        name.setText(dogName);
        age.setText(String.valueOf(dogAge));
        breed.setText(dogBreed);
        sex.setText(dogSex);
        size.setText(dogSize);

        String dateOnly = dogBirthday.substring(0, 10);
        birthday.setText(dateOnly); // Set the date portion to the TextView
        
        description.setText(dogDescription);


        loadImagefromURI(dogImage);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AdminDogUpdate.this)
                        .galleryOnly()
                        .start();

            }
        });




        if (dogId == -1) {
            Toast.makeText(this, "Dog ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if dog ID is not found
        } else {
            Toast.makeText(this, "ID: "+ dogId, Toast.LENGTH_SHORT).show();

        }




    }


    private void showToast(String message) {
        Toast.makeText(AdminDogUpdate.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(AdminDogUpdate.this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            image.setImageURI(uri);

            // Convert to Blob
            ContentResolver cr = getContentResolver();
            byte[] blob = uriToBlob(cr, uri);

            // TODO: Upload Blob to Database
            int id = getIntent().getIntExtra("dogId", -1);
            EditText nameInput = findViewById(R.id.dogUpdateName);
            EditText ageInput = findViewById(R.id.dogUpdateAge);
            EditText breedInput = findViewById(R.id.dogUpdateBreed);
            EditText sexInput = findViewById(R.id.dogUpdateSex);
            EditText sizeInput = findViewById(R.id.dogUpdateSize);
            EditText descriptionInput = findViewById(R.id.dogUpdateDescription);
            EditText birthdayInput = findViewById(R.id.dogUpdateBirthday);
            ImageButton updateButton = findViewById(R.id.updateButton);

            RetrofitService retrofitService = new RetrofitService();
            DogApi dogApi = retrofitService.getDogApi();

            updateButton.setOnClickListener(view -> {
                String name = String.valueOf(nameInput.getText());
                int age = Integer.parseInt(String.valueOf(ageInput.getText()));
                String breed = String.valueOf(breedInput.getText());
                String sex = String.valueOf(sexInput.getText());
                String size = String.valueOf(sizeInput.getText());
                String description = String.valueOf(descriptionInput.getText());
                String birthday = String.valueOf(birthdayInput.getText());

                Dog dog = new Dog();

                dog.setName(name);
                dog.setAge(age);
                dog.setBreed(breed);
                dog.setSex(sex);
                dog.setSize(size);
                dog.setDescription(description);
                dog.setBirthday(birthday);
                dog.setImage(Base64.encodeToString(blob, Base64.DEFAULT));


                dogApi.updateDog(id, dog).enqueue(new Callback<Dog>() {
                    @Override
                    public void onResponse(Call<Dog> call, Response<Dog> response) {
                        if (response.isSuccessful()) {
                            String successMessage = "Success";
                            showToast(successMessage);
                        } else {
                            String errorMessage = "Failed: " + response.message();
                            showErrorDialog(errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<Dog> call, Throwable t) {
                        String errorMessage = "Failed: " + t.getMessage();
                        showErrorDialog(errorMessage);
                        Logger.getLogger(AdminDogForm.class.getName()).log(Level.SEVERE, "Error Occurred!", t);
                    }
                });
            });


            if (blob != null) {

                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] uriToBlob(ContentResolver cr, Uri uri) {
        byte[] blob = null;
        try {
            InputStream input = cr.openInputStream(uri);

            if (input != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                blob = bitmapToBlob(bitmap);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return blob;
    }

    private byte[] bitmapToBlob(Bitmap bitmap) {
        byte[] blob = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            // Compress the Bitmap into a PNG format with maximum quality (100)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            // Convert the ByteArrayOutputStream to a byte array
            blob = outputStream.toByteArray();
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blob;
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void loadImagefromURI(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new  File(imagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image.setImageBitmap(myBitmap);

                // Delete the temporary file after loading the image
                imgFile.delete();
            }
        }
    }





}