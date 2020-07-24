package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.journey.R;
import com.example.journey.databinding.ActivityAnalysisDetailBinding;

public class AnalysisDetailActivity extends AppCompatActivity {

    private ActivityAnalysisDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalysisDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindElements();
        setupElements();
    }

    private void bindElements() {

    }

    private void setupElements() {

    }
}