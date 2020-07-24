package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.journey.R;
import com.example.journey.databinding.ActivityAnalysisDetailBinding;
import com.example.journey.models.Analysis;

public class AnalysisDetailActivity extends AppCompatActivity {

    private ActivityAnalysisDetailBinding binding;

    public static final String KEY_ANALYSIS = "analysis";
    private Analysis analysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalysisDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindElements();
        setupElements();
    }

    private void bindElements() {
        analysis = getIntent().getParcelableExtra(KEY_ANALYSIS);
    }

    private void setupElements() {

    }
}