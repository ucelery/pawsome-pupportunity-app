package com.example.pawsomepuppertunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class DogPreview extends AppCompatActivity {

    int dogId;
    String dogName;
    int dogAge;
    String dogBreed;
    String dogSex;
    String dogSize;
    String dogBirthday;
    String dogDescription;
    String dogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_preview);

        updateValues();
        adoptListener();
    }

    private void adoptListener() {
        ImageButton adoptButton = findViewById(R.id.adoptButton);
        adoptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DogPreview.this, DogAdoptionForm.class);

                intent.putExtra("dogId", dogId); // Pass dog ID to the next activity
                intent.putExtra("dogName", dogName);
                intent.putExtra("dogAge", dogAge);
                intent.putExtra("dogBreed", dogBreed);
                intent.putExtra("dogSex", dogSex);
                intent.putExtra("dogSize", dogSize);
                intent.putExtra("dogBirthday", dogBirthday);
                intent.putExtra("dogDescription", dogDescription);

                startActivity(intent);
            }
        });
    }

    private void updateValues() {
        TextView dogNameText = findViewById(R.id.nameText);
        TextView dogDescText = findViewById(R.id.descriptionText);
        TextView dogAgeText = findViewById(R.id.ageText);
        TextView dogBreedText = findViewById(R.id.breedText);
        TextView dogSexText = findViewById(R.id.sexText);
        TextView dogBirthText = findViewById(R.id.birthText);
        TextView dogSizeText = findViewById(R.id.sizeText);
        ImageView dogImageView = findViewById(R.id.dogImage);

        // Retrieve dogId from the previous intent
        dogId = getIntent().getIntExtra("dogId", -1);
        dogName = getIntent().getStringExtra("dogName");
        dogAge = getIntent().getIntExtra("dogAge", -1);
        dogBreed = getIntent().getStringExtra("dogBreed");
        dogSex = getIntent().getStringExtra("dogSex");
        dogSize = getIntent().getStringExtra("dogSize");
        dogBirthday = getIntent().getStringExtra("dogBirthday");
        dogDescription = getIntent().getStringExtra("dogDescription");
        dogImage = getIntent().getStringExtra("dogImage");

        dogNameText.setText(dogName);
        dogDescText.setText(dogDescription);
        dogAgeText.setText(String.valueOf(dogAge));
        dogBreedText.setText(dogBreed);
        dogSexText.setText(dogSex);
        int indexOfT = dogBirthday.indexOf("T");
        dogBirthText.setText(dogBirthday.substring(0, indexOfT));
        dogSizeText.setText(dogSize);

        // Decode Base64 string to byte array
        byte[] bytesDecodedImage = Base64.decode(dogImage, Base64.DEFAULT);

        // Convert byte array to Bitmap
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytesDecodedImage, 0, bytesDecodedImage.length);

        // Convert Bitmap to Drawable
        Drawable drawableImage = new BitmapDrawable(getResources(), bitmapImage);

        // Set the Drawable as background for the view
        dogImageView.setBackground(drawableImage);
    }
}