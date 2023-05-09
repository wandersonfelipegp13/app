package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityCareBinding;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.DateUtils;
import com.example.myapplication.util.DateValidatorNoFuture;
import com.example.myapplication.util.ToolbarConfig;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;

public class CareActivity extends AppCompatActivity {

    private ActivityCareBinding binding;
    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        date = Calendar.getInstance();
        changeDate(date);

        onClickSelectCareDate();

    }

    private void changeDate(Calendar time) {

        date = time;
        binding.tvDataCare.setText(DateUtils.dateToString(time));

    }

    private void onClickSelectCareDate() {

        binding.llSelectDate.setOnClickListener(view -> {

            CalendarConstraints calendarConstraints =
                    new CalendarConstraints.Builder()
                            .setValidator(new DateValidatorNoFuture())
                            .build();

            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText(getString(R.string.care_date))
                            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                            .setCalendarConstraints(calendarConstraints)
                            .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {

                Calendar correctDate = Calendar.getInstance();
                correctDate.setTimeInMillis(selection);

                DateUtils.correctDatePickerDate(correctDate);

                changeDate(correctDate);

            });

            datePicker.show(getSupportFragmentManager(), "tag");

        });

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