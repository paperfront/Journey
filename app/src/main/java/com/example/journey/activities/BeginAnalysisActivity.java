package com.example.journey.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.databinding.ActivityBeginAnalysisBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Analysis;
import com.example.journey.models.Entry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import timber.log.Timber;

public class BeginAnalysisActivity extends AppCompatActivity {

    private ActivityBeginAnalysisBinding binding;

    private TextView tvDateRange;
    private EditText etTitle;
    private MaterialButton btSetDateFilter;
    private MaterialButton btStartAnalysis;
    private SwitchMaterial swWordCloud;
    private SwitchMaterial swFullMap;
    private SwitchMaterial swImportantEntries;
    private SwitchMaterial swMoodGraph;
    private ProgressBar pbLoading;

    private Date startDate;
    private Date endDate;

    boolean dateFilterEnabled = false;
    boolean moodEnabled;
    boolean mapEnabled;
    boolean wordCloudEnabled;
    boolean keyEntriesEnabled;

    private List<String> stopwords;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBeginAnalysisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindElements();
        setupElements();
    }

    private void bindElements() {
        tvDateRange = binding.tvDateRange;
        btSetDateFilter = binding.btSetDateFilter;
        btStartAnalysis = binding.btStartAnalysis;
        swWordCloud = binding.swWordCloud;
        swFullMap = binding.swFullMap;
        swImportantEntries = binding.swImportantEntries;
        swMoodGraph = binding.swMoodGraph;
        etTitle = binding.etTitle;
        pbLoading = binding.pbLoading;
    }

    private void setupElements() {
        setupButtons();
    }

    private void setupButtons() {
        btSetDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateFilterClicked();
            }
        });

        btStartAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnalysisClicked();
            }
        });
    }

    private void startAnalysisClicked() {
        Timber.d("Start Analysis Button Clicked.");
        moodEnabled = swMoodGraph.isChecked();
        mapEnabled = swFullMap.isChecked();
        wordCloudEnabled = swWordCloud.isChecked();
        keyEntriesEnabled = swImportantEntries.isChecked();
        if (validateEntries()) {
            //todo run analysis
            Timber.d("Word Cloud Enabled: " + wordCloudEnabled);
            Timber.d("Map Setting Enabled: " + mapEnabled);
            Timber.d("Key Entries Setting Enabled: " + keyEntriesEnabled);
            Timber.d("Mood Setting Enabled: " + moodEnabled);
            setupAnalysis();


        } else if (etTitle.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a title for your analysis first.", Toast.LENGTH_SHORT).show();
            etTitle.setError("Title cannot be empty");
            etTitle.requestFocus();
        } else {
            Toast.makeText(this, "Please select at least 1 feature to include in your analysis.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDateFilterClicked() {
        Timber.d("Set Date Filter Button Clicked.");
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());
        picker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Timber.d("Date Picker Cancelled.");
            }
        });
        picker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Negative button was clicked");
            }
        });
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Timber.d("On Positive button clicked. ");
                if (selection.first == null || selection.second == null) {
                    Toast.makeText(BeginAnalysisActivity.this, "Please select a start and end date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                dateFilterEnabled = true;
                TimeZone local = TimeZone.getDefault();
                long start = selection.first;
                long end = selection.second;
                int offsetStart = local.getOffset(start);
                int offsetEnd = local.getOffset(end);
                startDate = new Date(start - offsetStart);
                endDate = new Date(end - offsetEnd);

                Timber.d("Start Date: " + startDate.toString());
                Timber.d("End Date: " + endDate.toString());
                setTvDateRange(startDate, endDate);
            }
        });
    }

    private void setTvDateRange(Date start, Date end) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String startDateText = format.format(start);
        String endDateText = format.format(end);

        tvDateRange.setText(startDateText + " to " + endDateText);
    }

    /**
     * Method to ensure that all required entries are filled/valid.
     * Checks to make sure that a title has been given, and returns true
     * if the field is not empty.
     */
    private boolean validateEntries() {
        return !etTitle.getText().toString().isEmpty()
                && (moodEnabled || mapEnabled || wordCloudEnabled || keyEntriesEnabled);
    }

    private void performAnalysis(List<Entry> entries) {
        Timber.i("Performing Analysis...");
        Analysis analysis = new Analysis(etTitle.getText().toString(), entries);

        if (wordCloudEnabled) {
            HashMap<String, Integer> wordCounts = getSignificantWordsFromPosts(entries);
            analysis.setWordCounts(wordCounts);
        }

        goToAnalysisDetailActivity(analysis);

    }

    private void setupAnalysis() {
        Timber.i("Setting up analysis");
        pbLoading.setVisibility(View.VISIBLE);
        loadStopwords();
        loadEntries();
    }

    private void loadEntries() {

        Query query;
        if (dateFilterEnabled) {
            query = FirestoreClient.getEntriesBetweenQuery(startDate, endDate);
        } else {
            query = FirestoreClient.getAllEntriesRef();
        }

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Entry> entries = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            entries.add(document.toObject(Entry.class));
                        }
                        performAnalysis(entries);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e, "Failed to get all entries.");
            }
        });
    }

    private void loadStopwords() {
        stopwords = Arrays.asList(getResources().getStringArray(R.array.english_stopwords));
    }

    private void updateWordCount(List<String> words, HashMap<String, Integer> wordCounter) {
        for (String word : words) {
            if (wordCounter.containsKey(word)) {
                wordCounter.put(word, wordCounter.get(word) + 1);
            } else {
                wordCounter.put(word, 1);
            }
        }
    }

    private HashMap<String, Integer> getSignificantWordsFromPosts(List<Entry> entries) {

        HashMap<String, Integer> wordCounter = new HashMap<>();

        for (Entry entry : entries) {
            List<String> currentResponses = entry.getAllStringResponses();
            if (currentResponses.isEmpty()) {
                continue;
            }
            for (String response : currentResponses) {
                List<String> allWords =
                        Arrays.asList(response.toLowerCase().replaceAll("\\p{Punct}","").split(" "));
                allWords = new ArrayList<>(allWords);
                allWords.removeAll(stopwords);
                updateWordCount(allWords, wordCounter);
            }
        }
        return wordCounter;
    }

    private void goToAnalysisDetailActivity(Analysis analysis) {
        pbLoading.setVisibility(View.INVISIBLE);
        Intent i = new Intent(this, AnalysisDetailActivity.class);
        i.putExtra(AnalysisDetailActivity.KEY_ANALYSIS, analysis);
        startActivity(i);
        finish();
    }


}