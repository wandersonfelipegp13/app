package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityAnimalsListBinding;
import com.example.myapplication.model.Animal;
import com.example.myapplication.model.AnimalAdapter;
import com.example.myapplication.util.ToolbarConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimalsListActivity extends AppCompatActivity {

    private ActivityAnimalsListBinding binding;
    private List<Animal> animals;
    private AnimalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        onClickNewAnimal();

    }

    @Override
    protected void onStart() {

        super.onStart();

        animals = new ArrayList<>();

        for (int i = 0; i < 33; i++) {
            animals.add(new Animal(UUID.randomUUID().toString().substring(0, 13), null, null, 0.0, null, false, null));
        }

        binding.tvAnimalsNumber.setText(animals.size() + " Animais");

        binding.rvAnimals.setLayoutManager(new LinearLayoutManager(AnimalsListActivity.this));
        binding.rvAnimals.setItemAnimator(new DefaultItemAnimator());
        binding.rvAnimals.setAdapter(adapter = new AnimalAdapter(animals, AnimalsListActivity.this, onClickAnimal()));

    }

    protected AnimalAdapter.AnimalOnClickListener onClickAnimal() {

        return ((holder, idx) -> {

            Intent intent = new Intent(AnimalsListActivity.this, AnimalDetailsActivity.class);
            startActivity(intent);

        });

    }

    private void onClickNewAnimal() {

        binding.fabAddAnimal.setOnClickListener(view -> {

            Intent intent = new Intent(AnimalsListActivity.this, AnimalFormActivity.class);
            startActivity(intent);

        });

    }

}
