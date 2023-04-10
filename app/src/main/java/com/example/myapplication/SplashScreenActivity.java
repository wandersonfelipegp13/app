package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onStart() {

        super.onStart();

        final Handler handler = new Handler();

        handler.postDelayed(() -> {

            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }, 2000);

    }

}