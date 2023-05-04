package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.myapplication.databinding.ActivityCareBinding;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.ToolbarConfig;

public class CareActivity extends AppCompatActivity {

    private ActivityCareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCareBinding.inflate(getLayoutInflater());
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
            AppToast.longMsg(this, getString(R.string.care_deleted));
            return true;

        } else if (id == R.id.save) {

            finish();
            AppToast.longMsg(this, getString(R.string.care_saved));
            return true;

        }

        return super.onOptionsItemSelected(item);

    }

}