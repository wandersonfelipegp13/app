package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityAnimalDetailsBinding;
import com.example.myapplication.model.Animal;
import com.example.myapplication.service.AnimalService;
import com.example.myapplication.service.ProductionService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.DateUtils;
import com.example.myapplication.util.ToolbarConfig;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class AnimalDetailsActivity extends AppCompatActivity {

    private ActivityAnimalDetailsBinding binding;
    private Animal animal;
    private String animalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

    }

    @Override
    protected void onStart() {
        super.onStart();
        animalId = getIntent().getStringExtra("animalDocId");
        if (animalId != null) loadData();
    }

    private void loadData() {
        AnimalService animalService = new AnimalService();
        animalService.getAnimal(animalId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                animal = document.toObject(Animal.class);
                setData();
            } else {
                AppToast.shorMsg(getBaseContext(), getString(R.string.error_loading_animal));
                finish();
            }
        });
    }

    private void setData() {
        binding.content.tvAnimalId.setText(animal.getIdentificacao());

        setContent(animal.getRaca(), binding.content.tvAnimalRaca);
        setContent(animal.getGenero(), binding.content.tvAnimalGenero);

        if (animal.getDataNascimento() == null) {
            binding.content.tvAnimalNasc.setText(getString(R.string.not_informed));
        } else {
            Calendar nasc = Calendar.getInstance();
            nasc.setTime(animal.getDataNascimento());
            String data = DateUtils.dateToString(nasc);
            binding.content.tvAnimalNasc.setText(data);
        }

        if (animal.getPeso() == null) {
            binding.content.tvAnimalPeso.setText(getString(R.string.not_informed));
        } else {
            setContent(String.valueOf(animal.getPeso()), binding.content.tvAnimalPeso);
        }

        if (animal.isProduzindo()) {
            binding.content.tvAnimalProd.setText(R.string.producing);
        } else {
            binding.content.tvAnimalProd.setText(R.string.not_producing);
        }

        if (animal.getFoto() != null) {
            Glide
                    .with(getBaseContext())
                    .load(Constants.STORAGE_IMAGES + animal.getFoto() + "?alt=media")
                    .into(binding.acivFoto);
        }
    }

    private void setContent(String content, TextView textView) {
        if (content == null) {
            textView.setText(R.string.not_informed);
        } else {
            textView.setText(content);
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

        if (id == R.id.menuItemProducoes) {
            Intent intent = new Intent(this, ProductionListActivity.class);
            intent.putExtra("animalDocId", animalId);
            intent.putExtra("animal", animal);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuItemEditar) {
            Intent intent = new Intent(this, AnimalFormActivity.class);
            intent.putExtra("animal", animal);
            intent.putExtra("animalDocId", animalId);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuItemExcluir) {
            confirmDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void deleteAnimal() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.animal_deleting));
        progressDialog.show();

        ProductionService productionService = new ProductionService(animalId);

        productionService.getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot prod : task.getResult().getDocuments()) {
                    productionService.delete(prod.getId());
                }

                if (animal.getFoto() != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference()
                            .child("imagens/" + animal.getFoto());
                    storageReference.delete();
                }

                AnimalService animalService = new AnimalService();
                animalService.delete(animalId).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        progressDialog.dismiss();
                        finish();
                        AppToast.longMsg(this, getString(R.string.animal_deleted));
                    }
                });

            } else {
                AppToast.shorMsg(getBaseContext(), getString(R.string.animal_delete_error));
            }
            progressDialog.dismiss();
        });
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.animal_delete_confirm));
        builder.setMessage(getString(R.string.animal_delete_message));
        builder.setPositiveButton(R.string.yes, (dialog, id) -> deleteAnimal());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
