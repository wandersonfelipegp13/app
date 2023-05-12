package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.InputValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

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
                AppToast.shorMsg(LoginActivity.this, "Informe um email válido");
                return;
            }

            if (!InputValidator.isValid(binding.titPass)) {
                AppToast.shorMsg(LoginActivity.this, "Informe sua senha");
                return;
            }

            String email = binding.titEmail.getText().toString();
            String password = binding.titPass.getText().toString();

            auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {

                        user = auth.getCurrentUser();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                        if (user != null && !user.isEmailVerified()) {
                            intent = new Intent(LoginActivity.this, EmailVerificationActivity.class);
                        }

                        startActivity(intent);
                        finish();

                    })
                    .addOnFailureListener(e ->
                            AppToast.shorMsg(LoginActivity.this, "Falha ao entrar na conta")
                    );

        });

    }

    private void onClickSignUp() {
        binding.btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

}