package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.example.myapplication.service.UserService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.InputValidator;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private UserService userService;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        onClickSignUp();

    }

    public void onClickSignUp() {

        binding.btnSignUp.setOnClickListener(view -> {

            if (!InputValidator.isValid(binding.titName)) {
                AppToast.shorMsg(SignUpActivity.this, getString(R.string.inform_your_name));
                return;
            }

            if (!InputValidator.isValid(binding.titEmail)) {
                AppToast.shorMsg(SignUpActivity.this, getString(R.string.inform_valid_email));
                return;
            }

            if (!InputValidator.isValid(binding.titPass)) {
                AppToast.shorMsg(SignUpActivity.this, getString(R.string.inform_password));
                return;
            }

            if (!InputValidator.isValid(binding.titConfirmPass)) {
                AppToast.shorMsg(SignUpActivity.this,  getString(R.string.confirm_password));
                return;
            }

            String name = binding.titName.getText().toString();
            String email = binding.titEmail.getText().toString();
            String password = binding.titPass.getText().toString();
            String confirmedPassword = binding.titConfirmPass.getText().toString();

            if (!password.equals(confirmedPassword)) {
                AppToast.shorMsg(SignUpActivity.this, getString(R.string.passwords_not_match));
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    userService = new UserService();
                    userService.updateName(name);
                    userService.sendEmailVerification();
                    userService.createUserDocument();
                    Intent intent = new Intent(SignUpActivity.this, EmailVerificationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AppToast.longMsg(SignUpActivity.this, getString(R.string.error_sign_up));
                }
            });

        });
    }

}