package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.ActivityAnalysisDetailBinding;
import com.example.journey.models.Analysis;
import com.example.journey.models.Location;
import com.example.journey.models.WordCloud;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class AnalysisDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityAnalysisDetailBinding binding;

    public static final String KEY_ANALYSIS = "analysis";
    private ScrollView svRoot;
    private TextView tvTitle;
    private Analysis analysis;
    private ImageView ivWordCloudHolder;
    private LinearLayout llWordCloud;
    private LinearLayout llMap;
    private ImageView ivTransparentImage;


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
        svRoot = binding.svRoot;
        ivWordCloudHolder = binding.ivWordCloudHolder;
        tvTitle = binding.tvTitle;
        llMap = binding.llMap;
        llWordCloud = binding.llWordCloud;
        ivTransparentImage = binding.transparentImage;
    }

    private void setupElements() {
        setupTitle();

        if (analysis.isSettingEnabled(Analysis.SETTING_MAPS)) {
            setupWordCloud();
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_WORD_CLOUD)) {
            setupMap();
        }

    }

    private void setupWordCloud() {
        llWordCloud.setVisibility(View.VISIBLE);
        WordCloud wordCloud = new WordCloud(analysis.getWordCounts());
        Bitmap bitmap = wordCloud.createBitmap();
        ivWordCloudHolder.setImageBitmap(bitmap);
    }

    private void setupMap() {

        ivTransparentImage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        // Disallow RV to intercept touch events.
                        svRoot.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow RV to intercept touch events.
                        svRoot.requestDisallowInterceptTouchEvent(false);
                        return true;

                    default:
                        return true;
                }
            }
        });

        llMap.setVisibility(View.VISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.getUiSettings().setZoomControlsEnabled(true);
        HashMap<Location, Integer> locations = analysis.getLocations();
        for (Location location : locations.keySet()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getName())
                    .snippet("Times Visited: " + locations.get(location)));
        }

    }

    private void setupTitle() {
        tvTitle.setText(analysis.getTitle());
    }

}