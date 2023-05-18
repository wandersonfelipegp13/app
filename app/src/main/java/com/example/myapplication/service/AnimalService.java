package com.example.myapplication.service;

import com.example.myapplication.model.Animal;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AnimalService {

    private FirebaseFirestore db;
    private DocumentReference userDocument;
    private UserService userService;

    public AnimalService() {
        this.db = FirebaseFirestore.getInstance();
        userService = new UserService();
        userDocument = db.collection("usuarios").document(userService.getUid());
    }

    public Task<Void> createAnimal(Animal animal) {
        return userDocument.collection("vacas").document().set(animal);
    }

    public Task<QuerySnapshot> getAll() {
        return userDocument.collection("vacas").get();
    }

}
