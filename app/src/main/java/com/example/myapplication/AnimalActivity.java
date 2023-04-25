package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityAnimalBinding;

public class AnimalActivity extends AppCompatActivity {

    private ActivityAnimalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureToolbar();
        configureToggleGroup();

    }

    private void configureToolbar() {

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

    }

    private void configureToggleGroup() {

        binding.content.btnNaoProduzindo.setChecked(true);

        binding.content.mtbProduzindo.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

            if (binding.content.btnProduzindo.isChecked()) {

            }

            if (binding.content.btnNaoProduzindo.isChecked()) {

            }

        });

    }

}