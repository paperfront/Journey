package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.adapters.JournalsAdapter;
import com.example.journey.databinding.ActivityJournalsBinding;
import com.example.journey.fragments.CreateJournalFragment;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Journal;
import com.example.journey.models.Prompt;
import com.example.journey.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private TextView tvNoJournals;

    public static final int FINISH_MAKING_POST = 121;
    public static final String KEY_JOURNAL = "journal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJournalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rvJournals = binding.rvJournals;
        pbLoading = binding.pbLoading;
        tvNoJournals = binding.tvNoJournals;
        setupRV();
    }


    private void setupRV() {
        journals = new ArrayList<>();
        journalTitles = new ArrayList<>();

        JournalsAdapter.JournalOnClick onClick = new JournalsAdapter.JournalOnClick() {
            @Override
            public void setOnClick(Journal journal) {
                goToCreateJournalEntryActivity(journal);
            }
        };
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);

        adapter = new JournalsAdapter(this, journals, getSupportFragmentManager(), pd, onClick);
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
        CreateJournalFragment createJournalDialogFragment = CreateJournalFragment.newInstance("Create New Journal", "", this);
        createJournalDialogFragment.show(fm, "fragment_create_journal");
    }

    @Override
    public void onFinishEditDialog(String title, int colorId) {

        if (journalTitles.contains(title)) {
            Toast.makeText(this, "A journal with that name already exists", Toast.LENGTH_SHORT).show();
            return;
        } else if (title.isEmpty()) {
            Toast.makeText(this, "Journal must be given a title to be created", Toast.LENGTH_SHORT).show();
            return;
        }
        journalTitles.add(title);
        Journal newJournal = new Journal(title, colorId);
        journals.add(newJournal);
        adapter.notifyItemInserted(journals.size() - 1);
        saveJournal(newJournal);
        Toast.makeText(this, "Created Journal Successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadJournals() {
        pbLoading.setVisibility(View.VISIBLE);
        CollectionReference docRef = FirestoreClient.getUserRef().collection("journals");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    tvNoJournals.setVisibility(View.GONE);
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot.isEmpty()) {
                        tvNoJournals.setVisibility(View.VISIBLE);
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Timber.d(document.getId() + " => " + document.getData());
                        Journal journal = document.toObject(Journal.class);
                        journals.add(journal);
                        journalTitles.add(journal.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    tvNoJournals.setVisibility(View.VISIBLE);
                    Timber.e(task.getException(),"Error getting documents: ");
                }
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

    private void saveJournal(final Journal journal) {
        tvNoJournals.setVisibility(View.GONE);
        DocumentReference userRef = FirestoreClient.getUserRef();
        userRef.update("journalNames", FieldValue.arrayUnion(journal.getTitle()));
        FirestoreClient.getReference().collection("general").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Prompt> prompts = new ArrayList<>();
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    prompts.add(documentSnapshot.toObject(Prompt.class));
                }
                journal.setPrompts(prompts);
                FirestoreClient.getUserRef().collection("journals").document(journal.getTitle()).set(journal);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e("Failed to get prompts for journal.");
            }
        });
    }

    public void goToCreateJournalEntryActivity(Journal journal) {
        //todo replace hardcoded track with the users current track
        Intent i = new Intent(this, CreateJournalEntryActivity.class);
        i.putExtra(CreateJournalEntryActivity.KEY_JOURNAL, journal);
        startActivityForResult(i, FINISH_MAKING_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FINISH_MAKING_POST) {
            Timber.d("Received result: " + resultCode);
            if (resultCode == RESULT_OK) {
                Journal journal = data.getParcelableExtra(KEY_JOURNAL);
                for (int i = 0; i < journals.size(); i++) {
                    if (journals.get(i).getTitle().equals(journal.getTitle())) {
                        journals.set(i, journal);
                        adapter.notifyItemChanged(i);
                        return;
                    }
                }
            }
        }
    }
}