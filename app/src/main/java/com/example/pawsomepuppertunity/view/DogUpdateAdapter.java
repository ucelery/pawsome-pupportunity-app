package com.example.pawsomepuppertunity.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsomepuppertunity.R;
import com.example.pawsomepuppertunity.model.Dog;

import java.util.List;

public class DogUpdateAdapter extends RecyclerView.Adapter<DogUpdateHolder> {


    private List<Dog> dogList;

    public DogUpdateAdapter(List<Dog> dogList) {
        this.dogList = dogList;
    }

    @NonNull
    @Override
    public DogUpdateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listdog_item, parent, false);
        return new DogUpdateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogUpdateHolder holder, int position) {
        Dog dog = dogList.get(position);
        holder.name.setText(dog.getName());
        holder.id.setText(String.valueOf(dog.getId()));

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
}
