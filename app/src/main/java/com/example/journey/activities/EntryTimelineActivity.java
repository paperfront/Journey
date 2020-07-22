package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.adapters.EntriesAdapter;
import com.example.journey.databinding.ActivityEntryDetailBinding;
import com.example.journey.databinding.ActivityEntryTimelineBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Journal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class EntryTimelineActivity extends AppCompatActivity {

    public static final String KEY_ENTRIES = "entries";
    public static final String KEY_TITLE = "title";
    private ActivityEntryTimelineBinding binding;
    private ProgressBar pbLoading;

    private TextView tvJournalName;
    private TextView tvNoEntries;
    private RecyclerView rvEntries;
    private EntriesAdapter adapter;
    private List<Entry> entries;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bindElements();
        setupElements();
    }

    private void bindElements() {
        entries = getIntent().getParcelableArrayListExtra(KEY_ENTRIES);
        title = getIntent().getStringExtra(KEY_TITLE);
        tvJournalName = binding.tvJournalName;
        rvEntries = binding.rvEntries;
        tvNoEntries = binding.tvNoEntries;
        tvNoEntries.setVisibility(View.GONE);
        pbLoading = binding.pbLoading;
    }

    private void setupElements() {
        setupTextViews();
        setupRV();
    }

    private void setupTextViews() {
        tvJournalName.setText(title);
    }

    private void setupRV() {
        adapter = new EntriesAdapter(entries, this);
        rvEntries.setAdapter(adapter);
        rvEntries.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        loadEntries();
    }

    private void loadEntries() {
        Collections.reverse(entries);
        if (entries.isEmpty()) {
            tvNoEntries.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
}