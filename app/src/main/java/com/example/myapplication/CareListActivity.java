package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityCareListBinding;
import com.example.myapplication.model.Care;
import com.example.myapplication.model.CareAdapter;
import com.example.myapplication.util.ToolbarConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CareListActivity extends AppCompatActivity {

    private ActivityCareListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCareListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ActionBar actionBar = ToolbarConfig.config(this, toolbar);
        actionBar.setDisplayShowTitleEnabled(true);

        onClickNewCare();

    }

    @Override
    protected void onStart() {

        super.onStart();

        List<Care> careList = new ArrayList<>();

        // Today
        for (int i = 0; i < 5; i++) {

            Date data = new Date();
            data.setTime(data.getTime() - 100000);

            careList.add(new Care(null, data, null, null));

        }

        // Yesterday
        for (int i = 0; i < 3; i++) {
            Calendar yesterday = Calendar.getInstance();
            yesterday.set(Calendar.DAY_OF_YEAR, yesterday.get(Calendar.DAY_OF_YEAR) - 1);

            Date data = new Date(yesterday.getTimeInMillis());
            careList.add(new Care(null, data, null, null));
        }

        for (int i = 0; i < 10; i++) {

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, new Random().nextInt(12));
            calendar.set(Calendar.DATE, new Random().nextInt(31));

            Date date = calendar.getTime();

            careList.add(new Care(UUID.randomUUID().toString(), date, null, null));

        }

        binding.rvCare.setLayoutManager(new LinearLayoutManager(CareListActivity.this));
        binding.rvCare.setItemAnimator(new DefaultItemAnimator());
        binding.rvCare.setAdapter(new CareAdapter(careList, CareListActivity.this, onClickCare()));

    }

    protected CareAdapter.CareOnClickListener onClickCare() {

        return ((holder, idx) -> {

            Intent intent = new Intent(CareListActivity.this, CareActivity.class);
            startActivity(intent);

        });

    }

    private void onClickNewCare() {

        binding.fabAddCare.setOnClickListener(view -> {

            Intent intent = new Intent(CareListActivity.this, CareActivity.class);
            startActivity(intent);

        });

    }

}