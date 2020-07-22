package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.adapters.JournalsAdapter;
import com.example.journey.databinding.ActivityJournalsBinding;
import com.example.journey.fragments.CreateJournalFragment;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Journal;
import com.example.journey.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class JournalsActivity extends AppCompatActivity implements CreateJournalFragment.EditNameDialogListener {

    private ActivityJournalsBinding binding;
    private RecyclerView rvJournals;
    private JournalsAdapter adapter;
    private List<Journal> journals;
    private List<String> journalTitles;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJournalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rvJournals = binding.rvJournals;
        pbLoading = binding.pbLoading;
        setupRV();
    }


    private void setupRV() {
        journals = new ArrayList<>();
        adapter = new JournalsAdapter(this, this, journals);
        rvJournals.setAdapter(adapter);
        rvJournals.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        loadJournals();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_journal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btAdd) {
            Timber.d("Add button clicked");
            showEditDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CreateJournalFragment createJournalDialogFragment = CreateJournalFragment.newInstance("Create New Journal");
        createJournalDialogFragment.show(fm, "fragment_create_journal");
    }

    @Override
    public void onFinishEditDialog(String title) {

        if (journalTitles.contains(title)) {
            Toast.makeText(this, "A journal with that name already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        journalTitles.add(title);
        Journal newJournal = new Journal(title);
        journals.add(newJournal);
        adapter.notifyItemInserted(journals.size() - 1);
        saveJournal(newJournal);
        Toast.makeText(this, "Created Journal Successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadJournals() {
        pbLoading.setVisibility(View.VISIBLE);
        DocumentReference docRef = FirestoreClient.getUserRef();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User currentUser = documentSnapshot.toObject(User.class);

                if (currentUser.getJournalNames() == null) {
                    journalTitles = new ArrayList<>();
                    FirestoreClient.getUserRef().update("journalNames", new ArrayList<String>());
                    return;
                }

                for (String journalName : currentUser.getJournalNames()) {
                    journals.add(new Journal(journalName));
                }
                journalTitles = currentUser.getJournalNames();
                adapter.notifyDataSetChanged();
                pbLoading.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e("Failed to load journals.");
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

    private void saveJournal(Journal journal) {
        DocumentReference userRef = FirestoreClient.getUserRef();
        userRef.update("journalNames", FieldValue.arrayUnion(journal.getTitle()));
        userRef.collection("journals").document(journal.getTitle()).set(journal);
    }
}