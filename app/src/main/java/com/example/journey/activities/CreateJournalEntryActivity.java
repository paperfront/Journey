package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.journey.databinding.ActivityCreateJournalEntryBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Journal;
import com.example.journey.models.Prompt;
import com.example.journey.models.Track;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CreateJournalEntryActivity extends AppCompatActivity {



    public static final String KEY_JOURNAL = "journal";

    private FragmentManager fragmentManager;
    private FrameLayout flPromptRoot;
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
        flPromptRoot = binding.getRoot();
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
        startLoading();
        prompts = new ArrayList<>();
        for (Prompt prompt : journal.getPrompts()) {
            if (prompt.isEnabled()) {
                prompts.add(prompt);
            }
        }
        stopLoading();
        loadNextPrompt();


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
            if (currentPromptCounter == 0) {
                currentPrompt = prompts.get(currentPromptCounter);
                currentPromptCounter += 1;
                setupPrompt();
            } else {
                YoYo.with(Techniques.FadeOut).duration(700).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (currentPromptCounter == prompts.size() - 1) {
                            btNext.setText("Finish");
                        }
                        currentPrompt = prompts.get(currentPromptCounter);
                        currentPromptCounter += 1;
                        setupPrompt();
                    }
                }).playOn(flPromptRoot);
            }
        }
    }

    private void setupPrompt() {
        tvQuestion.setText(currentPrompt.getQuestion());
        fragmentManager.beginTransaction().replace(binding.flPromptHolder.getId(), currentPrompt.getPromptFragment()).commit();
        Timber.d("Loaded prompt: " + currentPrompt.getPromptId());
        YoYo.with(Techniques.FadeIn).duration(1200).playOn(flPromptRoot);
    }


    private void goToDetailPage() {
        Entry entry = new Entry(prompts);
        saveEntry(entry);
        Intent i = new Intent(this, EntryDetailActivity.class);
        i.putExtra(EntryDetailActivity.KEY_ENTRY, entry);
        i.putExtra(EntryDetailActivity.KEY_JOURNAL, journal);
        Intent data = new Intent();
        journal.getEntries().add(entry);
        data.putExtra(JournalsActivity.KEY_JOURNAL, journal);
        setResult(RESULT_OK, data);
        startActivity(i);
        finish();
    }

    private void saveEntry(Entry entry) {
        CollectionReference entryRef =
                FirestoreClient.getUserRef()
                        .collection("journals");
        CollectionReference allEntries =
                FirestoreClient.getUserRef()
                        .collection("allEntries");
        DocumentReference docRef = entryRef.document(journal.getTitle());
        docRef.update("entries", FieldValue.arrayUnion(entry));
        allEntries.add(entry);
        FirestoreClient.getAllEntriesRef().document("recentEntry").set(entry);
    }

}