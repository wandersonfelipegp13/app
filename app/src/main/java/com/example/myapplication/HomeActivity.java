package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityHomeBinding;
import com.example.myapplication.service.UserService;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickAnimalsList();
        onClickReport();
        onClickProfile();

    }

    @Override
    protected void onStart() {
        super.onStart();
        UserService userService = new UserService();
        binding.tvName.setText(userService.getName());
    }

    private void onClickAnimalsList() {
        binding.tvAnimals.setOnClickListener(view -> {
            Intent intent = new Intent(this, AnimalsListActivity.class);
            startActivity(intent);
        });
    }

    private void onClickReport() {
        binding.tvReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        });
    }

    private void onClickProfile() {
        binding.tvSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }

}
