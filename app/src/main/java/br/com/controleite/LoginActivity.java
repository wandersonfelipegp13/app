package br.com.controleite;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.com.controleite.databinding.ActivityLoginBinding;
import br.com.controleite.service.UserService;
import br.com.controleite.util.AppToast;
import br.com.controleite.util.InputValidator;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        auth.useAppLanguage();

        onClickLogin();
        onClickSignUp();
        onClickResetPassword();

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

    private void onClickResetPassword() {
        binding.tvResetPass.setOnClickListener(view -> {

            if (!InputValidator.isValid(binding.titEmail)) {
                AppToast.shorMsg(LoginActivity.this, getString(R.string.inform_email_reset));
                return;
            }

            AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.password_reset_sent_title));
            builder.setMessage(getString(R.string.password_reset_email));
            builder.setPositiveButton(R.string.yes, (dialog, id) ->
                    auth.sendPasswordResetEmail(binding.titEmail.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    AppToast.longMsg(LoginActivity.this, getString(R.string.password_reset_sent));
                                } else {
                                    AppToast.longMsg(LoginActivity.this, getString(R.string.password_reset_fail));
                                }
                            })
            );
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

}