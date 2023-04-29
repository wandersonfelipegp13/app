package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityCareListBinding;
import com.example.myapplication.model.Care;
import com.example.myapplication.model.CareAdapter;
import com.example.myapplication.util.ToolbarConfig;

import java.time.OffsetDateTime;
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
        ToolbarConfig.config(this, toolbar);

        onClickNewCare();

    }

    @Override
    protected void onStart() {

        super.onStart();

        List<Care> careList = new ArrayList<>();

        for (int i = 0; i < 33; i++) {

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, 2023);
            calendar.set(Calendar.MONTH, new Random().nextInt(12));
            calendar.set(Calendar.DATE, new Random().nextInt(31));

            Date date = calendar.getTime();

            careList.add(new Care(UUID.randomUUID().toString(), date, null, null));

        }

        binding.rvCare.setLayoutManager(new LinearLayoutManager(CareListActivity.this));
        binding.rvCare.setItemAnimator(new DefaultItemAnimator());
        CareAdapter careAdapter;
        binding.rvCare.setAdapter(careAdapter = new CareAdapter(careList, CareListActivity.this, onClickCare()));

    }

    protected CareAdapter.CareOnClickListener onClickCare() {

        return ((holder, idx) -> {

        });

    }

    private void onClickNewCare() {

        binding.fabAddCare.setOnClickListener(view -> {

        });

    }

}