package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.journey.R;
import com.example.journey.databinding.ActivityAnalysisDetailBinding;
import com.example.journey.databinding.ItemEntryBinding;
import com.example.journey.fragments.AnalysisFragment;
import com.example.journey.models.Analysis;
import com.example.journey.models.Entry;
import com.example.journey.models.Location;
import com.example.journey.models.WordCloud;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class AnalysisDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityAnalysisDetailBinding binding;


    public static final String KEY_ANALYSIS = "analysis";
    public static final String KEY_NEW_ANALYSIS = "new analysis";
    private boolean newAnalysis = false;
    private ScrollView svRoot;
    private TextView tvTitle;
    private Analysis analysis;
    private ImageView ivWordCloudHolder;
    private LinearLayout llWordCloud;
    private LinearLayout llMap;
    private LinearLayout llKeyEntries;
    private LinearLayout llMoodGraph;
    private ImageView ivTransparentImage;
    private ItemEntryBinding entryBinding;
    private AnyChartView anyChartView;


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
        newAnalysis = getIntent().getBooleanExtra(KEY_NEW_ANALYSIS, false);

        svRoot = binding.svRoot;
        ivWordCloudHolder = binding.ivWordCloudHolder;
        tvTitle = binding.tvTitle;
        llMap = binding.llMap;
        llWordCloud = binding.llWordCloud;
        ivTransparentImage = binding.transparentImage;
        llKeyEntries = binding.llImportantEntry;
        llMoodGraph = binding.llMoodGraph;
        entryBinding = binding.itemEntry;
        anyChartView = binding.anyChartView;
    }

    private void setupElements() {
        setupTitle();

        if (analysis.isSettingEnabled(Analysis.SETTING_WORD_CLOUD)) {
            setupWordCloud();
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_MAPS)) {
            setupMap();
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_IMPORTANT_ENTRIES)) {
            setupKeyEntries();
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_MOOD_GRAPH)) {
            // note firebase has issues, uncomment when bugs are fixed
            // https://github.com/firebase/firebase-android-sdk/issues/361#issuecomment-664593540
            setupMoodGraph();
        }

    }

    private void setupMoodGraph() {
        llMoodGraph.setVisibility(View.VISIBLE);
        anyChartView.setProgressBar(binding.pbMood);
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Mood Level Over Time");

        cartesian.yAxis(0).title("Mood Level");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        List<Entry> allEntries = analysis.getEntries();
        Collections.sort(allEntries);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        for (Entry entry : allEntries) {
            seriesData.add(new ValueDataEntry(format.format(entry.getDateCreated()), entry.getMood()));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Mood");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.yScale(AnyChart.line().yScale().minimum(1).maximum(10).minimumGap(1));
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartesian);
    }


    private void setupKeyEntries() {
        llKeyEntries.setVisibility(View.VISIBLE);
        final Entry keyEntry = analysis.getImportantEntry();
        entryBinding.tvDateCreated.setText(keyEntry.getDateCreated().toString());
        entryBinding.tvPromptsAnswered.setText("Prompts Answered: " + Integer.toString(keyEntry.getPrompts().size()));
        entryBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AnalysisDetailActivity.this, EntryDetailActivity.class);
                i.putExtra(EntryDetailActivity.KEY_ENTRY, keyEntry);
                startActivity(i);
            }
        });
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