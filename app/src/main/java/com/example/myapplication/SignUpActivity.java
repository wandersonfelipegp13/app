package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.InputValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickSignUp();

    }

    public void onClickSignUp() {

        binding.btnSignUp.setOnClickListener(view -> {

            if (!InputValidator.isValid(binding.titEmail)) {
                AppToast.shorMsg(SignUpActivity.this, "Informe um email válido");
                return;
            }

            if (!InputValidator.isValid(binding.titPass)) {
                AppToast.shorMsg(SignUpActivity.this, "Informe uma senha");
                return;
            }

            if (!InputValidator.isValid(binding.titConfirmPass)) {
                AppToast.shorMsg(SignUpActivity.this, "Confirme sua senha");
                return;
            }

            String email = binding.titEmail.getText().toString();
            String password = binding.titPass.getText().toString();
            String confirmedPassword = binding.titConfirmPass.getText().toString();

            if (!password.equals(confirmedPassword)) {
                AppToast.shorMsg(SignUpActivity.this, "As senhas não conferem");
                return;
            }

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.useAppLanguage();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();

                            if (user != null) {
                                user.sendEmailVerification();
                                Intent intent = new Intent(SignUpActivity.this, EmailVerificationActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            AppToast.longMsg(SignUpActivity.this, "Erro ao entrar no app");
                        }

                    });

        });
    }

}