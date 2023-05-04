package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityProductionListBinding;
import com.example.myapplication.model.Production;
import com.example.myapplication.model.ProductionAdapter;
import com.example.myapplication.util.ToolbarConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ProductionListActivity extends AppCompatActivity {

    private ActivityProductionListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ActionBar actionBar = ToolbarConfig.config(this, toolbar);
        actionBar.setDisplayShowTitleEnabled(true);

        onClickNewProduction();

    }

    @Override
    protected void onStart() {

        super.onStart();

        List<Production> productions = new ArrayList<>();

        for (int i = 0; i < 11; i++) {

            Date data = new Date();
            data.setTime(data.getTime() - 100000);

            productions.add(new Production(null, new Random().nextInt(200), data));

        }

        for (int i = 0; i < 3; i++) {
            Calendar yesterday = Calendar.getInstance();
            yesterday.set(Calendar.DAY_OF_YEAR, yesterday.get(Calendar.DAY_OF_YEAR) - 1);

            Date data = new Date(yesterday.getTimeInMillis());
            productions.add(new Production(null, new Random().nextInt(200), data));
        }

        for (int i = 0; i < 22; i++) {

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, new Random().nextInt(12));
            calendar.set(Calendar.DATE, new Random().nextInt(31));

            Date date = calendar.getTime();

            productions.add(new Production(null, new Random().nextInt(200), date));

        }

        binding.rvProductions.setLayoutManager(new LinearLayoutManager(ProductionListActivity.this));
        binding.rvProductions.setItemAnimator(new DefaultItemAnimator());
        binding.rvProductions.setAdapter(new ProductionAdapter(productions, ProductionListActivity.this, onClickProduction()));

    }

    protected ProductionAdapter.ProductionOnClickListener onClickProduction() {

        return ((holder, idx) -> {

            Intent intent = new Intent(ProductionListActivity.this, ProductionFormActivity.class);
            startActivity(intent);

        });

    }

    private void onClickNewProduction() {

        binding.fabAddProduction.setOnClickListener(view -> {

            Intent intent = new Intent(ProductionListActivity.this, ProductionFormActivity.class);
            startActivity(intent);

        });

    }

}