package com.example.journey.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreClient {

    public static FirebaseFirestore getReference() {
        return FirebaseFirestore.getInstance();
    }

    public static DocumentReference getUserRef() {return FirestoreClient.getReference().collection("users").document(FirebaseAuth.getInstance().getUid());}
}
