package br.com.controleite;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import br.com.controleite.databinding.ActivityProductionListBinding;
import br.com.controleite.model.Animal;
import br.com.controleite.model.Production;
import br.com.controleite.model.ProductionAdapter;
import br.com.controleite.service.ProductionService;
import br.com.controleite.util.AppToast;
import br.com.controleite.util.ToolbarConfig;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductionListActivity extends AppCompatActivity {

    private ActivityProductionListBinding binding;
    private String animalId;
    private List<Production> productions;
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

        productions = new ArrayList<>();
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
            try {
                Intent intent = new Intent(getBaseContext(), ProductionFormActivity.class);
                intent.putExtra("animalDocId", animalId);
                intent.putExtra("prodDocId", prodIds.get(idx));
                intent.putExtra("producao", productions.get(idx));
                startActivity(intent);
            } catch (Exception e) {
                AppToast.shorMsg(getBaseContext(), getString(R.string.loading_production));
            }
        });
    }

    private void onClickNewProduction() {
        binding.fabAddProduction.setOnClickListener(view -> {

            Animal animal = getIntent().getParcelableExtra("animal");

            if (animal != null && !animal.isProduzindo()) {
                AppToast.shorMsg(getBaseContext(), getString(R.string.animal_not_producing));
                return;
            }

            Intent intent = new Intent(getBaseContext(), ProductionFormActivity.class);
            intent.putExtra("animalDocId", animalId);
            startActivity(intent);
        });
    }

}