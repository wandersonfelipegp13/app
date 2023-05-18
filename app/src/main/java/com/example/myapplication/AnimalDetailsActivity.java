package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityAnimalDetailsBinding;
import com.example.myapplication.model.Animal;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.DateUtils;
import com.example.myapplication.util.ToolbarConfig;

import java.util.Calendar;

public class AnimalDetailsActivity extends AppCompatActivity {

    private ActivityAnimalDetailsBinding binding;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        animal = getIntent().getParcelableExtra("animal");
        if (animal != null) setData();

    }

    private void setData() {

        Log.d("animal", animal.toString());

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
                    .load("https://firebasestorage.googleapis.com/v0/b/tcc-if-889c5" +
                            ".appspot.com/o/imagens%2F" +
                            animal.getFoto() +
                            "?alt=media"
                    )
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
            startActivity(intent);
            return true;

        } else if (id == R.id.menuItemCuidados) {

            Intent intent = new Intent(this, CareListActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menuItemEditar) {

            Intent intent = new Intent(this, AnimalFormActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menuItemExcluir) {

            finish();
            AppToast.longMsg(this, getString(R.string.animal_deleted));
            return true;

        }

        return super.onOptionsItemSelected(item);

    }

}
