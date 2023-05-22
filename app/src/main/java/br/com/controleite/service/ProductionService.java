package br.com.controleite.service;

import br.com.controleite.model.Production;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class ProductionService {

    private final CollectionReference collection;

    public ProductionService(String animalId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserService userService = new UserService();
        collection = db
                .collection("usuarios")
                .document(userService.getUid())
                .collection("animais")
                .document(animalId)
                .collection("producoes");
    }

    public Task<Void> create(Production production) {
        return collection.document().set(production);
    }

    public Task<Void> delete(String prodId) {
        return collection.document(prodId).delete();
    }

    public Task<Void> update(Production production, String productionId) {
        return collection.document(productionId).set(production);
    }

    public Task<QuerySnapshot> getAll() {
        return collection.orderBy("data", Query.Direction.DESCENDING).get();
    }

    public Task<QuerySnapshot> getAll(Date initialDate, Date finalDate) {
        return collection
                .whereGreaterThan("data", initialDate)
                .whereLessThan("data", finalDate)
                .get();
    }

    public Task<QuerySnapshot> getProdToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return collection.whereGreaterThan("data", calendar.getTime()).get();
    }

    public Task<QuerySnapshot> getProdThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return collection.whereGreaterThan("data", calendar.getTime()).get();
    }

}
