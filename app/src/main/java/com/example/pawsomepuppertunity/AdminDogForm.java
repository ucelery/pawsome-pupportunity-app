package com.example.pawsomepuppertunity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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