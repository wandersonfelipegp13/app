package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityEmailVerificationBinding;
import com.example.myapplication.util.AppToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {

    private ActivityEmailVerificationBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityEmailVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        concatWithUserEmail();
        onClickDeleteAccount();
        onCLickSignOut();

    }

    private void concatWithUserEmail() {

        String email;

        if (user != null && user.getEmail() != null) {
            email = user.getEmail();
        } else {
            email = getString(R.string.your_email);
        }

        binding.tvEmail.setText(email);

    }

    private void onClickDeleteAccount() {
        binding.btnDeleteAcc.setOnClickListener(view ->
                user.delete().addOnSuccessListener(unused -> {
                    AppToast.longMsg(EmailVerificationActivity.this, "Conta excluída");
                    auth.signOut();
                    backToLogin();
                }).addOnFailureListener(e ->
                        AppToast.longMsg(EmailVerificationActivity.this,
                                "Erro ao excluir sua conta")
                ));
    }

    private void onCLickSignOut() {
        binding.btnExit.setOnClickListener(view -> {
            auth.signOut();
            backToLogin();
        });
    }

    private void backToLogin() {
        Intent intent = new Intent(EmailVerificationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
