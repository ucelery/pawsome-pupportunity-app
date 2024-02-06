package com.example.pawsomepuppertunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class DogAdoptionForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_adoption_form);

        // Java
        ImageButton imageButton = findViewById(R.id.submit_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        Toast.makeText(DogAdoptionForm.this, "Sending Email", Toast.LENGTH_SHORT).show();

        EditText name_editText = findViewById(R.id.adopt_name);
        String name = name_editText.getText().toString();

//        EditText email_editText = findViewById(R.id.adopt_email);
//        String email = email_editText.getText().toString();

        EditText address_editText = findViewById(R.id.adopt_address);
        String address = address_editText.getText().toString();

        EditText reason_editText = findViewById(R.id.adopt_reason);
        String reason = reason_editText.getText().toString();

        String[] recipient = { "charmisk1@gmail.com" };

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dog Adoption");

        String emailBody = "Hello! I would like to adopt <name of dog>. " +
                "I assure you they will be in good hands! \n\n" +
                "Physical Address: \n" + address + "\n\n" +
                "Sincerely, " + name + ".";
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DogAdoptionForm.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}