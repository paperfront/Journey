package com.example.journey.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreClient {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final DocumentReference userRef = FirestoreClient.getReference().collection("users").document(FirebaseAuth.getInstance().getUid());

    public static FirebaseFirestore getReference() {
        return db;
    }

    public static DocumentReference getUserRef() {return userRef;}
}
