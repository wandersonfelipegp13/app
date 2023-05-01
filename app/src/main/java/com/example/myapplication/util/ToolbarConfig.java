package com.example.myapplication.util;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolbarConfig {

    public static ActionBar config(AppCompatActivity activity, Toolbar toolbar) {

        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> activity.finish());

        ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        return actionBar;

    }

}
