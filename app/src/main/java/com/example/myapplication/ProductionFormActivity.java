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
import com.example.myapplication.util.DateValidatorNoFuture;
import com.example.myapplication.util.ToolbarConfig;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductionFormActivity extends AppCompatActivity {

    private ActivityProductionFormBinding binding;
    private Calendar now;
    private int currentHour;
    private int currentMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        now = Calendar.getInstance();
        currentHour = now.get(Calendar.HOUR_OF_DAY);
        currentMin = now.get(Calendar.MINUTE);

        onClickSelectProductionDate();
        onClickSelectProductionTime();

    }

    private void onClickSelectProductionDate() {

        showProductionDate(now);

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
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                binding.tvDataProd.setText(df.format(calendar.getTime()));

            });

            datePicker.show(getSupportFragmentManager(), "tag");

        });

    }

    private void showProductionDate(Calendar date) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        binding.tvDataProd.setText(df.format(date.getTime()));

    }

    private void onClickSelectProductionTime() {

        showProductionTime(currentHour, currentMin);

        binding.llSelectTime.setOnClickListener(view -> {

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(currentHour)
                    .setMinute(currentMin)
                    .setTitleText("Selecione a hora da produção")
                    .build();

            timePicker.show(getSupportFragmentManager(), "tag");

            timePicker.addOnPositiveButtonClickListener(view1 ->
                    showProductionTime(timePicker.getHour(), timePicker.getMinute())
            );

        });

    }

    private void showProductionTime(int hour, int min) {

        String time = String.format(Locale.getDefault(), "%02d", hour) + ":"
                + String.format(Locale.getDefault(), "%02d", min);
        binding.tvTimeProd.setText(time);

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