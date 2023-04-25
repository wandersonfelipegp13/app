package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityAnimalDetailsBinding;

public class AnimalDetailsActivity extends AppCompatActivity {

    private ActivityAnimalDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureToolbar();
        configureActionBar();

    }
    private void configureToolbar() {

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

    }

    private void configureActionBar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.animal_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuItemEditar) {

            Intent intent = new Intent(this, AnimalFormActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menuItemCuidados) {

            return true;

        } else if (id == R.id.menuItemExcluir) {

            return true;

        }

        return super.onOptionsItemSelected(item);

    }
}
