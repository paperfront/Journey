package com.example.journey.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreClient {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static FirebaseFirestore getReference() {
        return db;
    }

}
