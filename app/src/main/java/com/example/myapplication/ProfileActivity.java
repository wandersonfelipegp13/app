package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.example.myapplication.model.Animal;
import com.example.myapplication.service.AnimalService;
import com.example.myapplication.service.ProductionService;
import com.example.myapplication.service.UserService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.InputValidator;
import com.example.myapplication.util.ToolbarConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

        onClickSignOut();
        onClickResetPassword();
        onClickResetEmail();
        onDeleteAccount();

    }

    private void onDeleteAccount() {
        binding.tvDeleteAccount.setOnClickListener(view -> {
            AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.profile_delete_account_confirm));
            builder.setMessage(getString(R.string.profile_delete_account_msg));
            builder.setPositiveButton(R.string.yes, (dialog, id) -> deleteProductions());
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void deleteProductions() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.profile_deleting_data));
        progressDialog.show();

        AnimalService animalService = new AnimalService();

        animalService.getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot animalDoc : task.getResult()) {

                    Animal animal = animalDoc.toObject(Animal.class);

                    // Photo Delete
                    if (animal.getFoto() != null) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReference()
                                .child("imagens/" + animal.getFoto());
                        storageReference.delete();
                    }

                    // Production Delete
                    ProductionService productionService = new ProductionService(animalDoc.getId());

                    productionService.getAll().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot prod : task2.getResult()) {
                                productionService.delete(prod.getId());
                            }
                        }}
                    );

                    // Animal Delete
                    animalService.delete(animalDoc.getId());
                }

                userService.deleteDocument();
                FirebaseAuth.getInstance().signOut();
                userService.delete().addOnCompleteListener(task1 -> {
                    AppToast.longMsg(getBaseContext(), "Conta excluída");
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                });

            }
        });
        progressDialog.dismiss();

    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        userService = new UserService();

        binding.titName.setText(userService.getName());
    }

    private void onClickResetPassword() {
        binding.tvRedefinePass.setOnClickListener(view -> {
            AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.password_reset_sent_title));
            builder.setMessage(getString(R.string.password_reset_email));
            builder.setPositiveButton(R.string.yes, (dialog, id) ->
                    auth.sendPasswordResetEmail(userService.getEmail())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    AppToast.longMsg(ProfileActivity.this, getString(R.string.password_reset_sent));
                                } else {
                                    AppToast.longMsg(ProfileActivity.this, getString(R.string.password_reset_fail));
                                }
                            })
            );
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void onClickResetEmail() {
        binding.tvRedefineEmail.setOnClickListener(view -> {
            Intent intent = new Intent(this, RedefineEmailActivity.class);
            startActivity(intent);
        });
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
