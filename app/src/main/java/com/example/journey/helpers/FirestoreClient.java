package com.example.journey.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

public class FirestoreClient {

    public static FirebaseFirestore getReference() {
        return FirebaseFirestore.getInstance();
    }

    public static DocumentReference getUserRef() {return FirestoreClient.getReference().collection("users").document(FirebaseAuth.getInstance().getUid());}

    public static CollectionReference getAllEntriesRef() {return FirestoreClient.getUserRef().collection("allEntries");}

    public static Query getEntriesBetweenQuery(Date start, Date end) {
        return FirestoreClient.getAllEntriesRef()
                .whereGreaterThan("dateCreated", start)
                .whereLessThan("dateCreated", end);
    }

    public static CollectionReference getAnalysisRef() {
        return FirestoreClient.getUserRef().collection("analysis");
    }


}
