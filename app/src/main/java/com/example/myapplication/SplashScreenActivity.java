package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    @Override
    protected void onStart() {

        super.onStart();

        final Handler handler = new Handler();

        handler.postDelayed(() -> {

            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);

            if (user != null && !user.isEmailVerified()) {
                intent = new Intent(SplashScreenActivity.this, EmailVerificationActivity.class);
            } else if (user != null && user.isEmailVerified()) {
                intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            }

            startActivity(intent);
            finish();

        }, 2000);

    }

}