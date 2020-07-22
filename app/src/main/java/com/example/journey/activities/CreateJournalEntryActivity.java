package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.ActivityCreateJournalEntryBinding;
import com.example.journey.databinding.FragmentCreateJournalEntryBinding;
import com.example.journey.fragments.EntryDetailFragment;
import com.example.journey.fragments.MainPageFragment;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Journal;
import com.example.journey.models.Prompt;
import com.example.journey.models.Track;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CreateJournalEntryActivity extends AppCompatActivity {



    public static final String KEY_JOURNAL = "journal";

    private FragmentManager fragmentManager;
    private TextView tvQuestion;
    private ProgressBar pbLoading;
    private Button btNext;

    private ActivityCreateJournalEntryBinding binding;


    //todo replace hardcoded value
    private Track track = Track.GENERAL;
    private ArrayList<Prompt> prompts;
    private int currentPromptCounter = 0;
    private Prompt currentPrompt;
    private Journal journal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJournalEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        journal = getIntent().getParcelableExtra(KEY_JOURNAL);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        tvQuestion = binding.tvQuestion;
        fragmentManager = getSupportFragmentManager();
        pbLoading = binding.pbLoading;
        btNext = binding.btNext;
    }

    private void setupElements() {
        setupButtons();
        getPrompts(track);
    }

    private void setupButtons() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextPrompt();
            }
        });
    }



    private void getPrompts(Track track) {
        FirebaseFirestore db = FirestoreClient.getReference();
        startLoading();
        db.collection(track.getKey()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    stopLoading();
                    QuerySnapshot snapshot = task.getResult();
                    if (!snapshot.isEmpty()) {
                        prompts = new ArrayList<>();
                        Timber.d("QuerySnapshot has some data");
                        List<DocumentSnapshot> documents = snapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            prompts.add(document.toObject(Prompt.class));
                        }
                        loadNextPrompt();
                    } else {
                        Timber.d("Query yielded no results");
                    }
                } else {
                    Timber.d(task.getException(), "get failed with ");
                }
            }
        });

    }

    private void startLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        btNext.setVisibility(View.INVISIBLE);
    }

    private void stopLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        btNext.setVisibility(View.VISIBLE);
    }



    private void loadNextPrompt() {
        if (currentPromptCounter >= prompts.size()) {
            Timber.d("Finished loading all prompts for track " + track.toString());
            goToDetailPage();
        } else {
            if (currentPromptCounter == prompts.size() - 1) {
                btNext.setText("Finish");
            }
            currentPrompt = prompts.get(currentPromptCounter);
            currentPromptCounter += 1;
            setupPrompt();
        }
    }

    private void setupPrompt() {
        tvQuestion.setText(currentPrompt.getQuestion());
        fragmentManager.beginTransaction().replace(binding.flPromptHolder.getId(), currentPrompt.getPromptFragment()).commit();
        Timber.d("Loaded prompt: " + currentPrompt.getPromptId());
    }


    private void goToDetailPage() {
        Entry entry = new Entry(prompts);
        Intent i = new Intent(this, EntryDetailActivity.class);
        i.putExtra(EntryDetailActivity.KEY_ENTRY, entry);
        i.putExtra(EntryDetailActivity.KEY_JOURNAL_TITLE, journal.getTitle());
        startActivity(i);
        finish();
    }

    public Journal getJournal() {
        return journal;
    }
}