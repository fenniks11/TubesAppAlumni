package com.example.tubesmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ForgotPassword extends AppCompatActivity {
    ImageView keLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        keLogin = findViewById(R.id.back_to_login);

        keLogin.setOnClickListener(view -> {
            Intent i = new Intent(ForgotPassword.this, Login.class);
            startActivity(i);
        });
    }
}