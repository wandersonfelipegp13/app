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
import com.example.myapplication.util.DateUtils;
import com.example.myapplication.util.DateValidatorNoFuture;
import com.example.myapplication.util.ToolbarConfig;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class ProductionFormActivity extends AppCompatActivity {

    private ActivityProductionFormBinding binding;
    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        date = Calendar.getInstance();
        changeDate(date);
        changeTime(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));

        onClickSelectProductionDate();
        onClickSelectProductionTime();

    }

    private void changeDate(Calendar date) {

        this.date = date;
        binding.tvDataProd.setText(DateUtils.dateToString(date));

    }

    private void changeTime(int hour, int min) {

        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, min);
        binding.tvTimeProd.setText(DateUtils.timeToString(date));

    }

    private void onClickSelectProductionDate() {

        binding.llSelectDate.setOnClickListener(view -> {

            CalendarConstraints calendarConstraints =
                    new CalendarConstraints.Builder()
                            .setValidator(new DateValidatorNoFuture())
                            .build();

            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText(getString(R.string.production_date))
                            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                            .setCalendarConstraints(calendarConstraints)
                            .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);

                DateUtils.correctDatePickerDate(calendar);

                changeDate(calendar);

            });

            datePicker.show(getSupportFragmentManager(), "tag");

        });

    }

    private void onClickSelectProductionTime() {

        binding.llSelectTime.setOnClickListener(view -> {

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(date.get(Calendar.HOUR_OF_DAY))
                    .setMinute(date.get(Calendar.MINUTE))
                    .setTitleText(getString(R.string.production_hour_select))
                    .build();

            timePicker.show(getSupportFragmentManager(), "tag");

            timePicker.addOnPositiveButtonClickListener(view1 ->
                    changeTime(timePicker.getHour(), timePicker.getMinute())
            );

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