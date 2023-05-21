package br.com.controleite;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.com.controleite.databinding.ActivityEmailVerificationBinding;
import br.com.controleite.service.UserService;
import br.com.controleite.util.AppToast;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerificationActivity extends AppCompatActivity {

    private ActivityEmailVerificationBinding binding;
    private FirebaseAuth auth;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityEmailVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        userService = new UserService();

        concatWithUserEmail();
        onClickDeleteAccount();
        onCLickSignOut();

    }

    private void concatWithUserEmail() {
        if (userService.isSignedIn()) {
            binding.tvEmail.setText(userService.getEmail());
        } else {
            binding.tvEmail.setText(getString(R.string.your_email));
        }
    }

    private void onClickDeleteAccount() {
        binding.btnDeleteAcc.setOnClickListener(view -> {
            userService.deleteDocument();
            userService.delete().addOnSuccessListener(unused -> {
                AppToast.longMsg(EmailVerificationActivity.this, getString(R.string.account_deleted));
                auth.signOut();
                backToLogin();
            }).addOnFailureListener(e ->
                    AppToast.longMsg(EmailVerificationActivity.this,
                            getString(R.string.account_delete_error))
            );
        });
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
