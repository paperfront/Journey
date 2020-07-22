package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.journey.R;
import com.example.journey.adapters.PromptsAdapter;
import com.example.journey.databinding.ActivityEntryDetailBinding;
import com.example.journey.databinding.FragmentEntryDetailBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Journal;
import com.example.journey.models.Prompt;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import timber.log.Timber;

public class EntryDetailActivity extends AppCompatActivity {

    public static final String KEY_ENTRY = "entry";
    public static final String KEY_JOURNAL_TITLE = "journal";

    private Entry entry;
    private List<Prompt> prompts;

    private ActivityEntryDetailBinding binding;

    private RecyclerView rvPrompts;

    private PromptsAdapter adapter;
    private String journalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        entry = (Entry) getIntent().getParcelableExtra(KEY_ENTRY);
        journalTitle = getIntent().getStringExtra(KEY_JOURNAL_TITLE);
        prompts = entry.getPrompts();
        bindElements();
        setupElements();
        saveEntry();
    }

    private void bindElements(){

        rvPrompts = binding.rvPrompts;
    }

    private void setupElements(){
        setupRV();
    }

    private void setupRV() {
        adapter = new PromptsAdapter(this, this, prompts, rvPrompts);
        rvPrompts.setAdapter(adapter);
        rvPrompts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    private void saveEntry() {
        CollectionReference entryRef =  FirestoreClient.getReference().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("journals")
                .document(journalTitle)
                .collection("entries");
        entryRef.add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Timber.d("DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.w(e, "Error adding document");
                    }
                });

    }
}