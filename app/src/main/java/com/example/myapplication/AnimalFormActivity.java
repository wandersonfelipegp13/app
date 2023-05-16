package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityAnimalFormBinding;
import com.example.myapplication.model.Animal;
import com.example.myapplication.service.AnimalService;
import com.example.myapplication.util.AppToast;
import com.example.myapplication.util.DateUtils;
import com.example.myapplication.util.DateValidatorNoFuture;
import com.example.myapplication.util.InputValidator;
import com.example.myapplication.util.ToolbarConfig;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;

public class AnimalFormActivity extends AppCompatActivity {

    private ActivityAnimalFormBinding binding;
    private Calendar nascimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);


        onClickSelectBirthDate();
        onClickDeleteBirthDate();

    }

    private void onClickSelectBirthDate() {

        binding.llSelectDate.setOnClickListener(view -> {

            CalendarConstraints calendarConstraints =
                    new CalendarConstraints.Builder()
                            .setValidator(new DateValidatorNoFuture())
                            .build();

            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText(getString(R.string.animal_birth))
                            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                            .setCalendarConstraints(calendarConstraints)
                            .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);

                nascimento = calendar;
                binding.tvDataNasc.setText(DateUtils.dateToString(calendar));

            });

            datePicker.show(getSupportFragmentManager(), "tag");

        });

    }

    private void onClickDeleteBirthDate() {

        binding.ivDeleteDataNasc.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.animal_birth_delete_question);
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                binding.tvDataNasc.setText(R.string.animal_birth_not_selected);
                nascimento = null;
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.save) {

            if (!InputValidator.isValid(binding.titId)) {
                AppToast.shorMsg(this, getString(R.string.inform_animal_id));
                return true;
            }

            Animal animal = new Animal();

            animal.setIdentificacao(binding.titId.getText().toString());
            animal.setRaca(binding.titRaca.getText().toString());
            animal.setRaca(binding.titPeso.getText().toString());
            if (nascimento != null) animal.setDataNascimento(nascimento.getTime());

            if (binding.rbMacho.isChecked()) animal.setGenero(getString(R.string.male));
            else animal.setGenero(getString(R.string.female));

            animal.setProduzindo(binding.sProduzindo.isChecked());

            AnimalService animalService = new AnimalService();

            animalService.createAnimal(animal).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finish();
                    AppToast.longMsg(this, getString(R.string.animal_saved));
                } else {
                    AppToast.longMsg(this, getString(R.string.animal_not_saved));
                }
            });

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

}