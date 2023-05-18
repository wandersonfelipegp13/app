package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityAnimalsListBinding;
import com.example.myapplication.model.Animal;
import com.example.myapplication.model.AnimalAdapter;
import com.example.myapplication.service.AnimalService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.ToolbarConfig;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnimalsListActivity extends AppCompatActivity {

    private ActivityAnimalsListBinding binding;
    private List<Animal> animals;
    private AnimalService animalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        animalService = new AnimalService();

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
                List<Animal> queryAnimals = new ArrayList<>();
                for (Animal animal : animals) {
                    if (animal.getIdentificacao().contains(query)) {
                        queryAnimals.add(animal);
                    }
                }
                setList(queryAnimals);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            setList(animals);
            return false;
        });

        return true;

    }

    @Override
    protected void onStart() {
        super.onStart();

        animals = new ArrayList<>();

        animalService.getAll().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               for (QueryDocumentSnapshot document : task.getResult()) {
                   Animal animal = document.toObject(Animal.class);
                   animals.add(animal);
               }
               setList(animals);
           }
        });
    }

    private void setList(List<Animal> animals) {
        binding.toolbar.setTitle(animals.size() + " " + getString(R.string.toolbar_animals_title));
        binding.rvAnimals.setLayoutManager(new LinearLayoutManager(AnimalsListActivity.this));
        binding.rvAnimals.setItemAnimator(new DefaultItemAnimator());
        binding.rvAnimals.setAdapter(new AnimalAdapter(animals,
                AnimalsListActivity.this, onClickAnimal()));
    }

    protected AnimalAdapter.AnimalOnClickListener onClickAnimal() {
        return ((holder, idx) -> {
            try {
                Animal animal = animals.get(idx);
                Intent intent = new Intent(AnimalsListActivity.this,
                        AnimalDetailsActivity.class);
                intent.putExtra("animal", animal);
                startActivity(intent);
            } catch (Exception e) {
                AppToast.shorMsg(getBaseContext(), getString(R.string.animal_list_error));
            }
        });
    }

    private void onClickNewAnimal() {
        binding.fabAddAnimal.setOnClickListener(view -> {
            Intent intent = new Intent(AnimalsListActivity.this,
                    AnimalFormActivity.class);
            startActivity(intent);
        });
    }

}
