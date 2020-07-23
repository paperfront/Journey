package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.databinding.ActivityBeginAnalysisBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import timber.log.Timber;

public class BeginAnalysisActivity extends AppCompatActivity {

    private ActivityBeginAnalysisBinding binding;

    private TextView tvDateRange;
    private EditText etTitle;
    private MaterialButton btSetDateFilter;
    private MaterialButton btStartAnalysis;
    private SwitchMaterial swKeyThemes;
    private SwitchMaterial swFullMap;
    private SwitchMaterial swImportantEntries;
    private SwitchMaterial swMoodGraph;




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
        swKeyThemes = binding.swKeyThemes;
        swFullMap = binding.swFullMap;
        swImportantEntries = binding.swImportantEntries;
        swMoodGraph = binding.swMoodGraph;
        etTitle = binding.etTitle;
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
        if (validateEntries()) {
            //todo run analysis
            boolean moodEnabled = swMoodGraph.isChecked();
            boolean mapEnabled = swFullMap.isChecked();
            boolean keyThemesEnabled = swKeyThemes.isChecked();
            boolean keyEntriesEnabled = swImportantEntries.isChecked();
            Timber.d("Key Theme Setting Enabled: " + keyThemesEnabled);
            Timber.d("Map Setting Enabled: " + mapEnabled);
            Timber.d("Key Entries Setting Enabled: " + keyEntriesEnabled);
            Timber.d("Mood Setting Enabled: " + moodEnabled);

        } else {
            Toast.makeText(this, "Please enter a title for your analysis first.", Toast.LENGTH_SHORT).show();
            etTitle.setError("Title cannot be empty");
            etTitle.requestFocus();
        }
    }

    private void setDateFilterClicked() {
        Timber.d("Set Date Filter Button Clicked.");
    }

    /**
     * Method to ensure that all required entries are filled/valid.
     * Checks to make sure that a title has been given, and returns true
     * if the field is not empty.
     */
    private boolean validateEntries() {
        return !etTitle.getText().toString().isEmpty();
    }


}