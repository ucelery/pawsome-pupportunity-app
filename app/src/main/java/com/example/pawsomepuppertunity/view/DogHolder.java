package com.example.pawsomepuppertunity.view;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawsomepuppertunity.R;

public class DogHolder extends RecyclerView.ViewHolder {

    TextView name, id;
    ImageView image;

    public DogHolder (@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.dogList_name);
        id = itemView.findViewById(R.id.dogList_id);
        image = itemView.findViewById(R.id.dogList_image);
    }


}
