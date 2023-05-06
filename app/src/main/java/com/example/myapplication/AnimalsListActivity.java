package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityAnimalsListBinding;
import com.example.myapplication.model.Animal;
import com.example.myapplication.model.AnimalAdapter;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.ToolbarConfig;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimalsListActivity extends AppCompatActivity {

    private ActivityAnimalsListBinding binding;
    private List<Animal> animals;

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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_animal_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_animal);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppToast.shorMsg(AnimalsListActivity.this, "Buscando "
                        + query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

    }

    @Override
    protected void onStart() {

        super.onStart();

        animals = new ArrayList<>();

        for (int i = 0; i < 33; i++) {
            animals.add(new Animal(UUID.randomUUID().toString().substring(0, 13), null, null, 0.0, null, false, null));
        }

        binding.toolbar.setTitle(animals.size() + " " + getString(R.string.toolbar_animals_title));

        binding.rvAnimals.setLayoutManager(new LinearLayoutManager(AnimalsListActivity.this));
        binding.rvAnimals.setItemAnimator(new DefaultItemAnimator());
        binding.rvAnimals.setAdapter(new AnimalAdapter(animals,
                AnimalsListActivity.this, onClickAnimal()));

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
