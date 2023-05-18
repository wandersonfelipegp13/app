package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.UUID;

public class AnimalFormActivity extends AppCompatActivity {

    private static final int GALLERY_PERMISSION_CODE = 1;
    private ActivityAnimalFormBinding binding;
    private Calendar nascimento;
    private Uri imageUri;
    private String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        onClickSelectPhoto();
        onClickSelectBirthDate();
        onClickDeleteBirthDate();
        onClickMale();
        onClickProducing();

    }

    private void onClickMale() {
        binding.rbMacho.setOnClickListener(view -> binding.sProduzindo.setChecked(false));
    }

    private void onClickProducing() {
        binding.sProduzindo.setOnClickListener(view -> binding.rbFemea.setChecked(true));
    }

    private void onClickSelectPhoto() {
        binding.imageAnimal.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setItems(R.array.image_fonts, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                selectFromGallery();
                                break;
                            case 1:
                                binding.imageAnimal.setImageDrawable(
                                        AppCompatResources.getDrawable(getBaseContext(),
                                                R.drawable.outline_add_a_photo_24));
                                imageUri = null;
                        }
                    });
            builder.create().show();
        });
    }

    private void selectFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,
                "Escolha uma imagem"), GALLERY_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                AppToast.shorMsg(getBaseContext(),
                        "Aceite a permissao para poder acessar a camera");
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_PERMISSION_CODE) {
                if (data != null) {
                    if (data.getData() != null) {
                        imageUri = data.getData();
                        Glide
                                .with(getBaseContext())
                                .load(imageUri)
                                .centerCrop()
                                .into(binding.imageAnimal);
                    }
                }
            }
        }

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

            if (imageUri != null) {

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Enviando foto");
                progressDialog.show();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference reference = storage.getReference().child("imagens");
                photoId = UUID.randomUUID().toString();
                StorageReference photo = reference.child(photoId);

                photo.putFile(imageUri)

                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred()
                                    / snapshot.getTotalByteCount());
                            progressDialog.setMessage((int) progress + "%");
                        })

                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                createAnimalDoc();
                            } else {
                                progressDialog.dismiss();
                                AppToast.shorMsg(getBaseContext(), "Não foi possível carregar a foto");
                                photoId = null;
                                createAnimalDoc();
                            }
                        });
            } else {
                createAnimalDoc();
            }

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    private void createAnimalDoc() {
        Animal animal = new Animal();

        animal.setIdentificacao(binding.titId.getText().toString());

        if (InputValidator.isValid(binding.titRaca)) {
            animal.setRaca(binding.titRaca.getText().toString());
        }

        if (InputValidator.isValid(binding.titPeso)) {
            animal.setPeso(Double.parseDouble(binding.titPeso.getText().toString()));
        }

        if (nascimento != null) animal.setDataNascimento(nascimento.getTime());

        if (binding.rbMacho.isChecked()) animal.setGenero(getString(R.string.male));
        else animal.setGenero(getString(R.string.female));

        animal.setProduzindo(binding.sProduzindo.isChecked());

        if (photoId != null) {
            animal.setFoto(photoId);
        }

        AnimalService animalService = new AnimalService();

        animalService.createAnimal(animal).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                AppToast.longMsg(this, getString(R.string.animal_saved));
            } else {
                AppToast.longMsg(this, getString(R.string.animal_not_saved));
            }
        });
    }

}