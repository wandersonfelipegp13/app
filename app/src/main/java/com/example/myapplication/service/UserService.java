package com.example.myapplication.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private FirebaseUser firebaseUser;
    private final FirebaseAuth auth;

    public UserService() {
        this.auth = FirebaseAuth.getInstance();
        this.auth.useAppLanguage();
        this.firebaseUser = auth.getCurrentUser();
    }

    public Task<Void> updateName(String newName) {

        firebaseUser = auth.getCurrentUser();

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        return firebaseUser.updateProfile(profileUpdate);

    }

    public String getName() {
        return firebaseUser.getDisplayName();
    }

    public String getEmail() {
        return firebaseUser.getEmail();
    }

    public void sendEmailVerification() {
        firebaseUser.sendEmailVerification();
    }

    public Task<Void> updateEmail(String newEmail) {
        return firebaseUser.verifyBeforeUpdateEmail(newEmail);
    }

    public boolean isEmailVerified() {
        return firebaseUser.isEmailVerified();
    }

    public boolean isSignedIn() {
        return firebaseUser != null;
    }

    public Task<Void> delete() {
        return firebaseUser.delete();
    }

    public Task<Void> deleteDocument() {
        return FirebaseFirestore.getInstance().collection("usuarios")
                .document(firebaseUser.getUid()).delete();
    }

    public void createUserDocument() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> createdAt = new HashMap<>();
        createdAt.put("created_at", new Date());
        db.collection("usuarios").document(firebaseUser.getUid()).set(createdAt);
    }

    public String getUid() {
        return firebaseUser.getUid();
    }

}
