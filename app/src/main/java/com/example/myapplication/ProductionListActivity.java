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
import com.example.myapplication.service.ProductionService;
import com.example.myapplication.util.ToolbarConfig;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductionListActivity extends AppCompatActivity {

    private ActivityProductionListBinding binding;
    private String animalId;
    private List<String> prodIds;

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

        animalId = getIntent().getStringExtra("animalDocId");

        List<Production> productions = new ArrayList<>();
        prodIds = new ArrayList<>();

        ProductionService productionService = new ProductionService(animalId);
        productionService.getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    Production production = documentSnapshot.toObject(Production.class);
                    productions.add(production);
                    prodIds.add(documentSnapshot.getId());
                }
                binding.rvProductions.setLayoutManager(
                        new LinearLayoutManager(ProductionListActivity.this));
                binding.rvProductions.setItemAnimator(new DefaultItemAnimator());
                binding.rvProductions.setAdapter(new ProductionAdapter(productions,
                        getBaseContext(), onClickProduction()));
            }
        });

    }

    protected ProductionAdapter.ProductionOnClickListener onClickProduction() {
        return ((holder, idx) -> {
            Intent intent = new Intent(getBaseContext(), ProductionFormActivity.class);
            intent.putExtra("animalDocId", animalId);
            startActivity(intent);
        });
    }

    private void onClickNewProduction() {
        binding.fabAddProduction.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ProductionFormActivity.class);
            intent.putExtra("animalDocId", animalId);
            startActivity(intent);
        });
    }

}