package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickAnimalsList();

    }

    private void onClickAnimalsList() {

        binding.tvAnimals.setOnClickListener(view -> {

            Intent intent = new Intent(this, AnimalsListActivity.class);
            startActivity(intent);

        });

    }

}