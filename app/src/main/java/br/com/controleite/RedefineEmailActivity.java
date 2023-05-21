package br.com.controleite;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import br.com.controleite.databinding.ActivityRedefineEmailBinding;
import br.com.controleite.service.UserService;
import br.com.controleite.util.AppToast;
import br.com.controleite.util.InputValidator;
import br.com.controleite.util.ToolbarConfig;

public class RedefineEmailActivity extends AppCompatActivity {

    private ActivityRedefineEmailBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRedefineEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ActionBar actionBar = ToolbarConfig.config(this, toolbar);
        actionBar.setDisplayShowTitleEnabled(true);

        userService = new UserService();

        onClickRedefineEmail();

    }

    private void onClickRedefineEmail() {
        binding.btnSendEmail.setOnClickListener(view -> {

            if (!InputValidator.isValid(binding.titNewEmail)) {
                AppToast.shorMsg(this, getString(R.string.inform_valid_email));
                return;
            }

            if (!InputValidator.isValid(binding.titConfirmNewEmail)) {
                AppToast.shorMsg(this, getString(R.string.confirm_new_email_hint));
                return;
            }

            String newEmail = binding.titNewEmail.getText().toString();
            String confirmedNewEmail = binding.titConfirmNewEmail.getText().toString();

            if (!newEmail.equals(confirmedNewEmail)) {
                AppToast.shorMsg(this, getString(R.string.emails_do_not_match));
                return;
            }

            userService.updateEmail(newEmail).addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    AppToast.longMsg(this, getString(R.string.redefine_email_sent));
                    finish();
                } else {
                    AppToast.longMsg(this, getString(R.string.redefine_email_not_sent));
                }
            });

        });
    }

}
