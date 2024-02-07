package com.example.pawsomepuppertunity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdminDogForm extends AppCompatActivity {
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dog_form);

        ImageButton uploadBtn = findViewById(R.id.dogImageBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                        new ActivityResultCallback<Uri>() {
                            @Override
                            public void onActivityResult(Uri result) {
                                if (result != null) {
                                    try {
                                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);

                                        // Convert to blob
                                        byte[] blob = getBytesFromBitmap(bitmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}