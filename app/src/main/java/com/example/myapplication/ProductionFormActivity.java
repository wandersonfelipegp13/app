package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityProductionFormBinding;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.ToolbarConfig;

public class ProductionFormActivity extends AppCompatActivity {

    private ActivityProductionFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_delete_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.delete) {

            finish();
            AppToast.longMsg(this, getString(R.string.production_deleted));
            return true;

        } else if (id == R.id.save) {

            finish();
            AppToast.longMsg(this, getString(R.string.production_saved));
            return true;

        }

        return super.onOptionsItemSelected(item);

    }

}