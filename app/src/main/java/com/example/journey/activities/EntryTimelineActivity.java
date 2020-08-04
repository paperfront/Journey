package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
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
    public static final String KEY_MENU = "menu";
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
    private boolean showFavoriteMenu = false;

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
        showFavoriteMenu = getIntent().getBooleanExtra(KEY_MENU, false);
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
        EntriesAdapter.ListUpdater updater = new EntriesAdapter.ListUpdater() {
            @Override
            public void updateItems(boolean toggle) {
                updateShownItems(toggle);
            }
        };
        adapter = new EntriesAdapter(shownEntries, this, title, updater);
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
        if (showFavoriteMenu) {
            getMenuInflater().inflate(R.menu.menu_favorite, menu);
        }
        MenuItem item = menu.findItem(R.id.action_favorite);
        Drawable heart = getDrawable(R.drawable.ic_baseline_favorite_24);
        heart = DrawableCompat.wrap(heart);
        DrawableCompat.setTint(heart, Color.GRAY);
        item.setIcon(heart);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            Timber.d("Favorite button clicked");
            handleFavorites(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleFavorites(MenuItem item) {
        if (!favoriteEnabled) {
            Drawable heart = getDrawable(R.drawable.ic_baseline_favorite_24);
            heart = DrawableCompat.wrap(heart);
            DrawableCompat.setTint(heart, Color.RED);
            item.setIcon(heart);

        } else {
            Drawable heart = getDrawable(R.drawable.ic_baseline_favorite_24);
            heart = DrawableCompat.wrap(heart);
            DrawableCompat.setTint(heart, Color.GRAY);
            item.setIcon(heart);
        }

        updateShownItems(true);

    }

    private void updateShownItems(boolean toggle) {

        if (toggle) {
            if (!favoriteEnabled) {
                shownEntries.clear();
                for (Entry entry : allEntries) {
                    if (entry.isFavorite()) {
                        shownEntries.add(entry);
                    }
                }
            } else {
                shownEntries.clear();
                shownEntries.addAll(allEntries);
            }
            Collections.sort(shownEntries);
            Collections.reverse(shownEntries);
            favoriteEnabled = !favoriteEnabled;
        } else {
            if (favoriteEnabled) {
                shownEntries.clear();
                for (Entry entry : allEntries) {
                    if (entry.isFavorite()) {
                        shownEntries.add(entry);
                    }
                }
            } else {
                shownEntries.clear();
                shownEntries.addAll(allEntries);
            }
            Collections.sort(shownEntries);
            Collections.reverse(shownEntries);
        }
        adapter.notifyDataSetChanged();
    }




}