package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.ActivityAnalysisDetailBinding;
import com.example.journey.models.Analysis;
import com.example.journey.models.WordCloud;

import org.w3c.dom.Text;

public class AnalysisDetailActivity extends AppCompatActivity {

    private ActivityAnalysisDetailBinding binding;

    public static final String KEY_ANALYSIS = "analysis";
    private TextView tvTitle;
    private Analysis analysis;
    private FrameLayout flWordCloudHolder;


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
        flWordCloudHolder = binding.flWordCloudHolder;
        tvTitle = binding.tvTitle;
    }

    private void setupElements() {
        setupTitle();

        if (analysis.getWordCounts() != null) {
            setupWordCloud();
        }

    }

    private void setupWordCloud() {
        WordCloud wordCloud = new WordCloud(analysis.getWordCounts());
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), wordCloud.createBitmap());
        flWordCloudHolder.setBackground(bitmapDrawable);
    }

    private void setupTitle() {
        tvTitle.setText(analysis.getTitle());
    }

}