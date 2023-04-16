package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickLogin();
        onClickSignUp();

    }

    private void onClickLogin() {

        binding.btnSignIn.setOnClickListener(view -> {

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();

        });

    }

    private void onClickSignUp() {

        binding.tvSignUpLink.setOnClickListener(view -> {

            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);

        });

    }

}