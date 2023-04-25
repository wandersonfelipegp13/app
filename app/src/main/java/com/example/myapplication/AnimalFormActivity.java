package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityAnimalFormBinding;

public class AnimalFormActivity extends AppCompatActivity {

    private ActivityAnimalFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickCancel();

    }

    private void onClickCancel() {
        binding.btnCancel.setOnClickListener(view -> finish());
    }

}