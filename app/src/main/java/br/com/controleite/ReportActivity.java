package br.com.controleite;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.controleite.databinding.ActivityReportBinding;
import br.com.controleite.model.Animal;
import br.com.controleite.model.Production;
import br.com.controleite.service.AnimalService;
import br.com.controleite.service.ProductionService;
import br.com.controleite.util.DateUtils;
import br.com.controleite.util.DateValidatorNoFuture;
import br.com.controleite.util.ToolbarConfig;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;
    private Date initialDate;
    private Date finalDate;
    private AnimalService animalService;
    private double totalProduction;
    private long days;
    private double dailyAvg;
    private long producingCows;
    private long cows;
    private List<String> animals;
    private List<Double> productions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ActionBar actionBar = ToolbarConfig.config(this, toolbar);
        actionBar.setDisplayShowTitleEnabled(true);

        binding.llReport.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        finalDate = calendar.getTime();
        binding.tvFinalDate.setText(DateUtils.dateToString(calendar));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        initialDate = calendar.getTime();
        binding.tvInitialDate.setText(DateUtils.dateToString(calendar));

        animalService = new AnimalService();

        onClickInitialDate();
        onClickFinalDate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.report_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.generate) {
            binding.llReport.setVisibility(View.VISIBLE);

            days = ChronoUnit.DAYS.between(initialDate.toInstant(), finalDate.toInstant());
            totalProduction = 0;
            dailyAvg = 0;
            producingCows = 0;

            resetValue(binding.tvDailyAvg);
            resetValue(binding.tvTotalProd);
            resetValue(binding.tvProducingCows);
            resetValue(binding.tvCowAvg);
            resetValue(binding.tvHigherProd);
            resetValue(binding.tvLowerProd);
            resetValue(binding.tvBirths);

            getTotalProduction();
            getProducingCows();
            getBirths();

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void resetValue(TextView textView) {
        textView.setText(getString(R.string.loading));
    }

    private void getTotalProduction() {

        animalService.getAllCows().addOnSuccessListener(queryDocumentSnapshots -> {

            cows = 0;
            animals = new ArrayList<>();
            productions = new ArrayList<>();

            if (queryDocumentSnapshots.getDocuments().size() == 0) {
                binding.tvTotalProd.setText("0 L");
                binding.tvDailyAvg.setText("0 L");
                binding.tvCowAvg.setText("0 L");
                binding.tvHigherProd.setText("0 L");
                binding.tvLowerProd.setText("0 L");
            }

            for (QueryDocumentSnapshot animalDoc : queryDocumentSnapshots) {

                cows = queryDocumentSnapshots.getDocuments().size();

                animals.add(animalDoc.toObject(Animal.class).getIdentificacao());

                ProductionService productionService = new ProductionService(animalDoc.getId());

                productionService.getAll(initialDate, finalDate).addOnCompleteListener(task -> {

                    double indProd = 0d;
                    for (DocumentSnapshot prodDoc : task.getResult().getDocuments()) {
                        Production production = prodDoc.toObject(Production.class);
                        totalProduction += production.getLitros();

                        indProd += production.getLitros();

                        String liters = formatValue(totalProduction) + " L";
                        binding.tvTotalProd.setText(liters);

                        if (days == 0) dailyAvg = totalProduction;
                        else dailyAvg = totalProduction / (days + 1);
                        String avg = formatValue(dailyAvg) + " L";
                        binding.tvDailyAvg.setText(avg);
                    }
                    productions.add(indProd);
                    if (totalProduction == 0) binding.tvTotalProd.setText("0 L");
                    if (dailyAvg == 0) binding.tvDailyAvg.setText("0 L");

                    String cowAvg = "0 L";
                    if (cows > 0) cowAvg = formatValue(totalProduction / cows) + " L";
                    binding.tvCowAvg.setText(cowAvg);

                    double higherProd = productions.stream().max(Double::compareTo).get();

                    if (higherProd > 0) {
                        StringBuilder result = new StringBuilder(higherProd + " L");
                        int prods = productions.size();
                        for (int i = 0; i < prods; i++) {
                            Double prod = productions.get(i);
                            if (prod == higherProd) {
                                String animalName = "\n" + animals.get(i);
                                result.append(animalName);
                            }
                        }
                        binding.tvHigherProd.setText(result);
                    } else {
                        binding.tvHigherProd.setText("0 L");
                    }

                    double lowerProd = productions.stream().min(Double::compareTo).get();

                    StringBuilder result = new StringBuilder(lowerProd + " L");
                    int prods = productions.size();
                    for (int i = 0; i < prods; i++) {
                        Double prod = productions.get(i);
                        if (prod == lowerProd) {
                            String animalName = "\n" + animals.get(i);
                            result.append(animalName);
                        }
                    }
                    binding.tvLowerProd.setText(result);

                });

            }

        });
    }

    private void getBirths() {
        animalService.getBirths(initialDate, finalDate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int births = task.getResult().getDocuments().size();
                binding.tvBirths.setText(String.valueOf(births));
            }
        });
    }

    private String formatValue(double value) {
        return String.format(Locale.getDefault(), "%.2f", value);
    }

    private void getProducingCows() {
        animalService.getProducingCows().addOnSuccessListener(queryDocumentSnapshots -> {
            producingCows = queryDocumentSnapshots.getDocuments().size();
            binding.tvProducingCows.setText(String.valueOf(producingCows));
        });
    }

    private void onClickInitialDate() {

        binding.llInitialDate.setOnClickListener(view -> {

            CalendarConstraints calendarConstraints = new CalendarConstraints.Builder().setValidator(new DateValidatorNoFuture()).build();

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.hint_initial_date)).setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).setCalendarConstraints(calendarConstraints).build();

            datePicker.addOnPositiveButtonClickListener(selection -> {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);

                DateUtils.correctDatePickerDate(calendar);

                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);

                initialDate = calendar.getTime();
                binding.tvInitialDate.setText(DateUtils.dateToString(calendar));

            });

            datePicker.show(getSupportFragmentManager(), "tag");

        });

    }

    private void onClickFinalDate() {

        binding.llFinalDate.setOnClickListener(view -> {

            CalendarConstraints calendarConstraints = new CalendarConstraints.Builder().setValidator(new DateValidatorNoFuture()).build();

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.hint_final_date)).setInputMode(MaterialDatePicker.INPUT_MODE_TEXT).setCalendarConstraints(calendarConstraints).build();

            datePicker.addOnPositiveButtonClickListener(selection -> {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);

                DateUtils.correctDatePickerDate(calendar);

                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);

                finalDate = calendar.getTime();
                binding.tvFinalDate.setText(DateUtils.dateToString(calendar));

            });

            datePicker.show(getSupportFragmentManager(), "tag");

        });

    }

}