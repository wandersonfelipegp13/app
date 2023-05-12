package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.service.UserService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.InputValidator;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

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

            if (!InputValidator.isValid(binding.titEmail)) {
                AppToast.shorMsg(LoginActivity.this, getString(R.string.inform_valid_email));
                return;
            }

            if (!InputValidator.isValid(binding.titPass)) {
                AppToast.shorMsg(LoginActivity.this, getString(R.string.inform_password));
                return;
            }

            String email = binding.titEmail.getText().toString();
            String password = binding.titPass.getText().toString();

            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    redirectUser();
                } else {
                    AppToast.shorMsg(LoginActivity.this, getString(R.string.sign_in_error));
                }
            });
        });

    }

    private void redirectUser() {
        UserService userService = new UserService();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        if (userService.isSignedIn() && !userService.isEmailVerified()) {
            intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void onClickSignUp() {
        binding.btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

}