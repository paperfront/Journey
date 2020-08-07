package com.example.journey.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.activities.EntryTimelineActivity;
import com.example.journey.adapters.JournalsAdapter;
import com.example.journey.databinding.ActivityJournalsBinding;
import com.example.journey.databinding.FragmentTimelineBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Journal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {

    private FragmentTimelineBinding binding;
    private RecyclerView rvJournals;
    private JournalsAdapter adapter;
    private List<Journal> journals;
    private List<String> journalTitles;
    private ProgressBar pbLoading;
    private TextView tvNoJournals;

    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimelineFragment.
     */
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentTimelineBinding.bind(view);
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
                Timber.d("Going to entries list for journal: " + journal.getTitle());
                goToEntryTimeline(journal);
            }
        };
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        adapter = new JournalsAdapter(getContext(), journals, getChildFragmentManager(), pd, onClick);
        rvJournals.setAdapter(adapter);
        rvJournals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        loadJournals();
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
                    for (QueryDocumentSnapshot document : snapshot) {
                        Timber.d(document.getId() + " => " + document.getData());
                        Journal journal = document.toObject(Journal.class);
                        journals.add(journal);
                        journalTitles.add(journal.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Timber.e(task.getException(),"Error getting documents: ");
                    tvNoJournals.setVisibility(View.VISIBLE);
                }
                pbLoading.setVisibility(View.GONE);

            }
        });
    }

    public void goToEntryTimeline(Journal journal) {
        Intent i = new Intent(getContext(), EntryTimelineActivity.class);
        i.putParcelableArrayListExtra(EntryTimelineActivity.KEY_ENTRIES, new ArrayList<>(journal.getEntries()));
        i.putExtra(EntryTimelineActivity.KEY_TITLE, journal.getTitle());
        i.putExtra(EntryTimelineActivity.KEY_MENU, true);
        i.putExtra(EntryTimelineActivity.KEY_COLOR, journal.getColorId());
        startActivity(i);
    }
}