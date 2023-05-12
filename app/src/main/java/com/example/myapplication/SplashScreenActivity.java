package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.service.UserService;

public class SplashScreenActivity extends AppCompatActivity {

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        userService = new UserService();

    }

    @Override
    protected void onStart() {

        super.onStart();

        final Handler handler = new Handler();

        handler.postDelayed(() -> {

            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);

            if (userService.isSignedIn() && userService.isEmailVerified()) {
                intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            } else if (userService.isSignedIn() && !userService.isEmailVerified()) {
                intent = new Intent(SplashScreenActivity.this, EmailVerificationActivity.class);
            }

            startActivity(intent);
            finish();

        }, 2000);

    }

}