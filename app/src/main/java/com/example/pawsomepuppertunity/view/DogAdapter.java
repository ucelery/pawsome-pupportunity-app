package com.example.pawsomepuppertunity.view;

import static java.io.File.createTempFile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.pawsomepuppertunity.AdminDash;
import com.example.pawsomepuppertunity.AdminDogUpdate;
import com.example.pawsomepuppertunity.DogAdoptionForm;
import com.example.pawsomepuppertunity.DogPreview;
import com.example.pawsomepuppertunity.R;
import com.example.pawsomepuppertunity.model.Dog;

import java.io.ByteArrayInputStream;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogHolder> {

    private List<Dog> dogList;
    private OnItemClickListener mListener;
    private Context mContext;
    private boolean isUserView = false;


    public DogAdapter(Context context, List<Dog> dogList) {
        this.mContext = context;
        this.dogList = dogList;
    }

    public DogAdapter(Context context, List<Dog> dogList, boolean isUserView) {
        this.mContext = context;
        this.dogList = dogList;
        this.isUserView = isUserView;
    }

    @NonNull
    @Override
    public DogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listdog_item, parent, false);

        if (isUserView) {
            ImageButton utilButton = view.findViewById(R.id.editButton);
            utilButton.setImageResource(R.drawable.box);
        }

        return new DogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogHolder holder, int position) {
        Dog dog = dogList.get(position);
        holder.name.setText(dog.getName());
        holder.id.setText(String.valueOf(dog.getId()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                // Handle item click
                if (isUserView)
                    intent = new Intent(mContext, DogPreview.class);
                else
                    intent = new Intent(mContext, AdminDogUpdate.class);

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
                    imageFile = createTempFile("dog_image", ".jpg", mContext.getCacheDir());
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
                // bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Adjust compression quality if needed
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                String filePath = imageFile.getAbsolutePath();

                intent.putExtra("dogImage", filePath);

                mContext.startActivity(intent);
            }
        });

        // Decode Base64 string to byte array
        byte[] bytesDecodedImage = Base64.decode(dog.getImage(), Base64.DEFAULT);

        // Convert byte array to Bitmap
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytesDecodedImage, 0, bytesDecodedImage.length);

        // Convert Bitmap to Drawable
        Drawable drawableImage = new BitmapDrawable(holder.itemView.getContext().getResources(), bitmapImage);

        // Set the Drawable as background for the view
        holder.image.setBackground(drawableImage);
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Dog dog);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
