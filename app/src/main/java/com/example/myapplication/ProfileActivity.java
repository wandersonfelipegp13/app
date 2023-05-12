package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityProfileBinding;
import com.example.myapplication.service.UserService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.InputValidator;
import com.example.myapplication.util.ToolbarConfig;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth auth;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ActionBar actionBar = ToolbarConfig.config(this, toolbar);
        actionBar.setDisplayShowTitleEnabled(true);

        auth = FirebaseAuth.getInstance();
        userService = new UserService();

        onClickSignOut();

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.titName.setText(userService.getName());
    }

    private void onClickSignOut() {

        binding.tvExit.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.confirm_sign_out));
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                auth.signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save) {

            if (!InputValidator.isValid(binding.titName)) {
                AppToast.longMsg(this, getString(R.string.inform_your_name));
                return super.onOptionsItemSelected(item);
            }

            userService.updateName(binding.titName.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AppToast.longMsg(this, getString(R.string.name_updated));
                        } else {
                            AppToast.longMsg(this, getString(R.string.error_update_name));
                        }
                    });

        }

        return super.onOptionsItemSelected(item);

    }

}
