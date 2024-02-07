package com.example.pawsomepuppertunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;


import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawsomepuppertunity.model.Users;
import com.example.pawsomepuppertunity.service.RetrofitService;
import com.example.pawsomepuppertunity.service.UserApi;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_admin_dog_form);

        startActivity(new Intent(MainActivity.this, AdminDogForm.class));


//        emailInput = findViewById(R.id.emailField);
//        passwordInput = findViewById(R.id.passwordField);
//
//        ImageButton loginButton = findViewById(R.id.loginBtn);
//
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginUser();
//            }
//        });
//
//
//        TextView textView = findViewById(R.id.guestSpan);
//        String text = "or\ncontinue as guest";
//
//        SpannableString ss = new SpannableString(text);
//
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                Toast.makeText(MainActivity.this, "Open Guest View", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        ss.setSpan(clickableSpan, 15, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(ss);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void loginUser() {

        RetrofitService retrofitService = new RetrofitService();

        final String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(email.isEmpty()) {
            emailInput.setError("Email is required!");
            emailInput.requestFocus();
            return;
        }else if (password.isEmpty()) {
            passwordInput.setError("Password is required!");
            passwordInput.requestFocus();
        }

        Call<ResponseBody> call = RetrofitService
                .getInstance()
                .getUserApi()
                .checkUsers(new Users(email, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int responseCode = response.code();
                Log.d("MainActivity", "Response Code: " + responseCode);


                String s ="";
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (s.equals(email)) {
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, AdminDash.class).putExtra("email", email));
                }else {
                    Toast.makeText(MainActivity.this, "Wrong credentials, please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}