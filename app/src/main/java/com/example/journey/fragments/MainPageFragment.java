package com.example.journey.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.activities.EntryTimelineActivity;
import com.example.journey.activities.JournalsActivity;
import com.example.journey.databinding.FragmentMainPageBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Track;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {


    private FragmentMainPageBinding binding;
    private CalendarView calendar;
    private ProgressBar pbLoading;

    private FloatingActionButton btCompose;
    private List<Entry> allEntries;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
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
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainPageBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        btCompose = binding.btComposeEntry;
        calendar = binding.calendarView;
        pbLoading = binding.pbLoading;
    }

    private void setupElements() {
        setupButtons();
    }

    private void setupButtons() {
        btCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Compose Entry button clicked.");
                goToJournalsActivity();
            }
        });
        setupCalendar();
    }

    private void setupCalendar() {
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Timber.d("Date Selected: " + month + "/" + dayOfMonth + "/" + year);
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                Date today = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();
                getEntriesFromDate(today, tomorrow);
            }
        });
    }

    private void getEntriesFromDate(final Date today, Date tomorrow) {
        pbLoading.setVisibility(View.VISIBLE);

        FirestoreClient.getEntriesBetweenQuery(today, tomorrow)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            entries.add(document.toObject(Entry.class));
                        }
                        Timber.i(queryDocumentSnapshots.getQuery().toString());
                        pbLoading.setVisibility(View.INVISIBLE);
                        goToEntryTimelineActivity(entries, "Entries From " + today.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e, "Failed to query calendar.");
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
        Timber.d("Start: " + today.toString());
        Timber.d("End: " + tomorrow.toString());

    }

    private void goToJournalsActivity() {
        Intent i = new Intent(getContext(), JournalsActivity.class);
        startActivity(i);
    }

    private void goToEntryTimelineActivity(ArrayList<Entry> entries, String title) {
        Intent i = new Intent(getContext(), EntryTimelineActivity.class);
        i.putParcelableArrayListExtra(EntryTimelineActivity.KEY_ENTRIES, entries);
        i.putExtra(EntryTimelineActivity.KEY_TITLE, title);
        i.putExtra(EntryTimelineActivity.KEY_MENU, false);
        i.putExtra(EntryTimelineActivity.KEY_COLOR, 0);
        startActivity(i);
    }



}