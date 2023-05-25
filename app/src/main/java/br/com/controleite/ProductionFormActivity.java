package br.com.controleite;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

import br.com.controleite.databinding.ActivityProductionFormBinding;
import br.com.controleite.model.Production;
import br.com.controleite.service.ProductionService;
import br.com.controleite.util.AppToast;
import br.com.controleite.util.DateUtils;
import br.com.controleite.util.DateValidatorNoFuture;
import br.com.controleite.util.InputValidator;
import br.com.controleite.util.ToolbarConfig;

public class ProductionFormActivity extends AppCompatActivity {

    private ActivityProductionFormBinding binding;
    private Calendar date;
    private String animalId;
    private String prodId;
    private Production producao;
    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        animalId = getIntent().getStringExtra("animalDocId");
        prodId = getIntent().getStringExtra("prodDocId");
        producao = getIntent().getParcelableExtra("producao");

        date = Calendar.getInstance();

        if (producao == null) {
            hour = date.get(Calendar.HOUR_OF_DAY);
            min = date.get(Calendar.MINUTE);
            binding.tvDataProd.setText(DateUtils.dateToString(date));
            binding.tvTimeProd.setText(DateUtils.timeToString(date));
        } else {
            setData();
        }

        onClickSelectProductionDate();
        onClickSelectProductionTime();

    }

    private void setData() {
        binding.titLiters.setText(String.valueOf(producao.getLitros().doubleValue()));
        date.setTime(producao.getData());
        binding.tvDataProd.setText(DateUtils.dateToString(date));
        binding.tvTimeProd.setText(DateUtils.timeToString(date));
        hour = date.get(Calendar.HOUR_OF_DAY);
        min = date.get(Calendar.MINUTE);
    }

    private void changeDate(Calendar date) {
        this.date = date;
        this.date.set(Calendar.HOUR_OF_DAY, hour);
        this.date.set(Calendar.MINUTE, min);
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

        animalId = getIntent().getStringExtra("animalDocId");

        int id = item.getItemId();

        if (id == R.id.delete) {

            if (prodId == null) {
                finish();
                return true;
            }

            ProductionService productionService = new ProductionService(animalId);
            productionService.delete(prodId);
            finish();
            return true;

        } else if (id == R.id.save) {

            if (!InputValidator.isValid(binding.titLiters)) {
                AppToast.shorMsg(getBaseContext(), getString(R.string.production_invalid_liters));
                return true;
            }

            Calendar now = Calendar.getInstance();
            if (DateUtils.isToday(date.getTime()) &&
                    (now.get(Calendar.HOUR_OF_DAY) < date.get(Calendar.HOUR_OF_DAY)) ||
                    (now.get(Calendar.HOUR_OF_DAY) == date.get(Calendar.HOUR_OF_DAY) &&
                            now.get(Calendar.MINUTE) < date.get(Calendar.MINUTE))) {
                AppToast.shorMsg(getBaseContext(), getString(R.string.production_invalid_hour));
                return true;
            }

            saveProd();
        }

        return super.onOptionsItemSelected(item);

    }

    private void saveProd() {

        Production production = new Production();
        production.setLitros(Double.parseDouble(binding.titLiters.getText().toString()));
        production.setData(date.getTime());

        ProductionService productionService = new ProductionService(animalId);

        if (prodId != null) {
            productionService.update(production, prodId);
            finish();
            return;
        }

        productionService.create(production);
        finish();

    }

}