package com.example.myapplication.service;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.model.Animal;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class AnimalService {

    private final CollectionReference collection;

    public AnimalService() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserService userService = new UserService();
        collection = db
                .collection("usuarios")
                .document(userService.getUid())
                .collection("vacas");
    }

    public Task<Void> createAnimal(Animal animal) {
        return collection.document().set(animal);
    }

    public Task<DocumentSnapshot> getAnimal(String animalId) {
        return collection.document(animalId).get();
    }

    public Task<Void> updateAnimal(Animal animal, String id) {
        return collection.document(id).set(animal);
    }

    public Task<QuerySnapshot> getAll() {
        return collection.orderBy("identificacao").get();
    }

    public Task<QuerySnapshot> getAllCows() {
        return collection.whereEqualTo("genero", "Fêmea").get();
    }

    public Task<QuerySnapshot> getBirths() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return collection.whereGreaterThan("dataNascimento", calendar.getTime()).get();
    }

    public Task<Void> delete(String animalId) {
        return collection.document(animalId).delete();
    }

}
