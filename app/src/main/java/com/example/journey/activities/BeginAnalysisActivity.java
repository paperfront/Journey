package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.ActivityBeginAnalysisBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class BeginAnalysisActivity extends AppCompatActivity {

    private ActivityBeginAnalysisBinding binding;

    private TextView tvDateRange;
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
    }

    private void bindElements() {
        tvDateRange = binding.tvDateRange;
        btSetDateFilter = binding.btSetDateFilter;
        btStartAnalysis = binding.btStartAnalysis;
        swKeyThemes = binding.swKeyThemes;
        swFullMap = binding.swFullMap;
        swImportantEntries = binding.swImportantEntries;
        swMoodGraph = binding.swMoodGraph;
    }

}