package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private List<Entry> shownEntries;
    private List<Entry> allEntries;
    private String title;
    private boolean favoriteEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bindElements();
        setupElements();
    }

    private void bindElements() {
        allEntries = getIntent().getParcelableArrayListExtra(KEY_ENTRIES);
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
        shownEntries = new ArrayList<>();
        Collections.reverse(allEntries);
        adapter = new EntriesAdapter(shownEntries, this, title);
        rvEntries.setAdapter(adapter);
        rvEntries.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        loadEntries();
    }

    private void loadEntries() {
        shownEntries.addAll(allEntries);
        if (shownEntries.isEmpty()) {
            tvNoEntries.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            Timber.d("Favorite button clicked");
            if (!favoriteEnabled) {
                Drawable heart = item.getIcon();
                heart = DrawableCompat.wrap(heart);
                DrawableCompat.setTint(heart, ContextCompat.getColor(this, R.color.quantum_googred));
                item.setIcon(heart);
                shownEntries.clear();
                for (Entry entry : allEntries) {
                    if (entry.isFavorite()) {
                        shownEntries.add(entry);
                    }
                }
            } else {
                Drawable heart = item.getIcon();
                heart = DrawableCompat.wrap(heart);
                DrawableCompat.setTint(heart, ContextCompat.getColor(this, R.color.quantum_white_100));
                item.setIcon(heart);
                shownEntries.clear();
                shownEntries.addAll(allEntries);
            }
            favoriteEnabled = !favoriteEnabled;
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

}