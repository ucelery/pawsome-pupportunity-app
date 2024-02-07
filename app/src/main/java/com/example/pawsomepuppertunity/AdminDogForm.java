package com.example.pawsomepuppertunity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pawsomepuppertunity.model.Dog;
import com.example.pawsomepuppertunity.service.DogApi;
import com.example.pawsomepuppertunity.service.RetrofitService;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDogForm extends AppCompatActivity {
    private ActivityResultLauncher<String> galleryLauncher;
    ImageButton uploadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dog_form);

        uploadBtn = findViewById(R.id.dogImageBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AdminDogForm.this)
                        .galleryOnly()
                        .start();

            }
        });


    }

    private void showToast(String message) {
        Toast.makeText(AdminDogForm.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(AdminDogForm.this)
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
            uploadBtn.setImageURI(uri);

            // Convert to Blob
            ContentResolver cr = getContentResolver();
            byte[] blob = uriToBlob(cr, uri);

            // TODO: Upload Blob to Database
            EditText nameInput = findViewById(R.id.dogName);
            EditText ageInput = findViewById(R.id.dogAge);
            EditText breedInput = findViewById(R.id.dogBreed);
            EditText sexInput = findViewById(R.id.dogSex);
            EditText sizeInput = findViewById(R.id.dogSize);
            EditText descriptionInput = findViewById(R.id.dogDescription);
            EditText birthdayInput = findViewById(R.id.dogBirthday);
            ImageButton submitButton = findViewById(R.id.saveButton);

            RetrofitService retrofitService = new RetrofitService();
            DogApi dogApi = retrofitService.getDogApi();

            submitButton.setOnClickListener(view -> {
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


                dogApi.save(dog).enqueue(new Callback<Dog>() {
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
}