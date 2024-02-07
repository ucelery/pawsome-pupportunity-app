package com.example.pawsomepuppertunity.view;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsomepuppertunity.R;

public class DogUpdateHolder extends RecyclerView.ViewHolder {

    TextView name, age, breed, sex, size, birthday, description;
    ImageView image;

    public DogUpdateHolder (@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.dogUpdateName);
        age = itemView.findViewById(R.id.dogUpdateAge);
        breed = itemView.findViewById(R.id.dogUpdateBreed);
        sex = itemView.findViewById(R.id.dogUpdateSex);
        size = itemView.findViewById(R.id.dogUpdateSex);
        birthday = itemView.findViewById(R.id.dogUpdateBirthday);
        description = itemView.findViewById(R.id.dogUpdateDescription);

        image = itemView.findViewById(R.id.dogUpdateImageBtn);
    }


}
