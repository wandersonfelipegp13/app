package br.com.controleite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import br.com.controleite.constants.Constants;
import br.com.controleite.databinding.ActivityAnimalFormBinding;
import br.com.controleite.model.Animal;
import br.com.controleite.service.AnimalService;
import br.com.controleite.service.ProductionService;
import br.com.controleite.util.AppToast;
import br.com.controleite.util.DateUtils;
import br.com.controleite.util.DateValidatorNoFuture;
import br.com.controleite.util.InputValidator;
import br.com.controleite.util.ToolbarConfig;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
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
    private Animal animal;
    private boolean deletePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAnimalFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        ToolbarConfig.config(this, toolbar);

        deletePhoto = false;

        animal = getIntent().getParcelableExtra("animal");
        if (animal != null) setData();

        onClickPhoto();
        onClickSelectBirthDate();
        onClickDeleteBirthDate();
        onClickMale();
        onClickProducing();

    }

    private void setData() {
        if (animal.getFoto() != null) {
            Glide
                    .with(AnimalFormActivity.this)
                    .load(Constants.STORAGE_IMAGES + animal.getFoto() + "?alt=media")
                    .centerCrop()
                    .into(binding.imageAnimal);
        }

        setContent(animal.getIdentificacao(), binding.titId);
        setContent(animal.getRaca(), binding.titRaca);

        if (animal.getPeso() != null) {
            setContent(String.valueOf(animal.getPeso()), binding.titPeso);
        }

        if (animal.getDataNascimento() != null) {
            nascimento = Calendar.getInstance();
            nascimento.setTime(animal.getDataNascimento());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(animal.getDataNascimento());
            binding.tvDataNasc.setText(DateUtils.dateToString(calendar));
        }

        if (animal.getGenero() != null && animal.getGenero().equals(getString(R.string.male))) {
            binding.rbMacho.setChecked(true);
        }

        if (animal.isProduzindo()) {
            binding.sProduzindo.setChecked(true);
        }

    }

    private void setContent(String content, TextInputEditText textInputEditText) {
        if (content != null) {
            textInputEditText.setText(content);
        }
    }

    private void onClickMale() {

        binding.rbMacho.setOnClickListener(view -> {
            binding.sProduzindo.setChecked(false);

            String id = getIntent().getStringExtra("animalDocId");

            if (id != null) {

                ProductionService productionService = new ProductionService(id);

                productionService.getAll().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getDocuments().size() > 0) {
                            AppToast.shorMsg(getBaseContext(),
                                    getString(R.string.animal_with_prods));
                            binding.rbFemea.setChecked(true);
                        }
                    }
                });
            }
        });

    }

    private void onClickProducing() {
        binding.sProduzindo.setOnClickListener(view -> binding.rbFemea.setChecked(true));
    }

    private void onClickPhoto() {
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
                                deletePhoto = true;
                        }
                    });
            builder.create().show();
        });
    }

    private void selectFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.choose_image)), GALLERY_PERMISSION_CODE);
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

                String oldPhoto = null;

                if (animal != null) {
                    oldPhoto = animal.getFoto();
                }

                if (oldPhoto != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference()
                            .child("imagens/" + oldPhoto);

                    storageReference.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            saveNewPhoto();
                        } else {
                            AppToast.shorMsg(getBaseContext(), getString(R.string.error_update_photo));
                            createAnimalDoc();
                        }
                    });
                } else {
                    saveNewPhoto();
                }

            } else if (animal != null && animal.getFoto() != null && deletePhoto) {

                StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReference()
                        .child("imagens/" + animal.getFoto());

                storageReference.delete().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        animal.setFoto(null);
                        createAnimalDoc();
                    } else {
                        AppToast.shorMsg(getBaseContext(), getString(R.string.error_update_photo));
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

    private void saveNewPhoto() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.saving_photo));
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
                        AppToast.shorMsg(getBaseContext(), getString(R.string.error_saving_photo));
                        photoId = null;
                        createAnimalDoc();
                    }
                });
    }

    private void createAnimalDoc() {
        Animal saveAnimal = new Animal();

        saveAnimal.setIdentificacao(binding.titId.getText().toString());

        if (InputValidator.isValid(binding.titRaca)) {
            saveAnimal.setRaca(binding.titRaca.getText().toString());
        }

        if (InputValidator.isValid(binding.titPeso)) {
            saveAnimal.setPeso(Double.parseDouble(binding.titPeso.getText().toString()));
        }

        if (nascimento != null) saveAnimal.setDataNascimento(nascimento.getTime());

        if (binding.rbMacho.isChecked()) saveAnimal.setGenero(getString(R.string.male));
        else saveAnimal.setGenero(getString(R.string.female));

        saveAnimal.setProduzindo(binding.sProduzindo.isChecked());

        if (animal != null && animal.getFoto() != null) {
            saveAnimal.setFoto(animal.getFoto());
        }

        if (photoId != null) {
            saveAnimal.setFoto(photoId);
        }

        AnimalService animalService = new AnimalService();

        if (getIntent().getStringExtra("animalDocId") == null) {
            animalService.createAnimal(saveAnimal).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    finish();
                    AppToast.longMsg(this, getString(R.string.animal_saved));
                } else {
                    AppToast.longMsg(this, getString(R.string.animal_not_saved));
                }
            });
        } else {
            animalService.updateAnimal(saveAnimal, getIntent().getStringExtra("animalDocId"))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            finish();
                            AppToast.longMsg(this, getString(R.string.animal_saved));
                        } else {
                            AppToast.longMsg(this, getString(R.string.animal_not_saved));
                        }
                    });
        }
    }

}