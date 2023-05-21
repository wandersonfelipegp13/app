package br.com.controleite;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.com.controleite.databinding.ActivityHomeBinding;
import br.com.controleite.model.Production;
import br.com.controleite.service.AnimalService;
import br.com.controleite.service.ProductionService;
import br.com.controleite.service.UserService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private double todayProd;
    private double thisMonthProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickAnimalsList();
        onClickReport();
        onClickProfile();

    }

    @Override
    protected void onStart() {
        super.onStart();
        todayProd = 0.0;
        thisMonthProd = 0.0;

        binding.tvProdHoje.setText("0 L");
        binding.tvProdMonth.setText("0 L");

        UserService userService = new UserService();
        binding.tvName.setText(userService.getName());

        AnimalService animalService = new AnimalService();

        animalService.getAllCows().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                ProductionService productionService = new ProductionService(doc.getId());

                productionService.getProdToday().addOnCompleteListener(task -> {
                    for (DocumentSnapshot prodDoc : task.getResult().getDocuments()) {
                        Production production = prodDoc.toObject(Production.class);
                        todayProd += production.getLitros();
                        String liters = todayProd + " L";
                        binding.tvProdHoje.setText(liters);
                    }
                    if (todayProd == 0) binding.tvProdHoje.setText("0 L");
                });

                productionService.getProdThisMonth().addOnCompleteListener(task -> {
                    for (DocumentSnapshot prodDoc : task.getResult().getDocuments()) {
                        Production production = prodDoc.toObject(Production.class);
                        thisMonthProd += production.getLitros();
                        String liters = thisMonthProd + " L";
                        binding.tvProdMonth.setText(liters);
                    }
                    if (thisMonthProd == 0) binding.tvProdMonth.setText("0 L");
                });
            }

        });

        animalService.getBirths().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int births = task.getResult().getDocuments().size();
                binding.tvBirths.setText(String.valueOf(births));
            }
        });

        animalService.getAllCows().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int cows = task.getResult().getDocuments().size();
                binding.tvCows.setText(String.valueOf(cows));
            }
        });

    }

    private void onClickAnimalsList() {
        binding.tvAnimals.setOnClickListener(view -> {
            Intent intent = new Intent(this, AnimalsListActivity.class);
            startActivity(intent);
        });
    }

    private void onClickReport() {
        binding.tvReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        });
    }

    private void onClickProfile() {
        binding.tvSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }

}
