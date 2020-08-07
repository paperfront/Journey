package com.example.journey.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.journey.databinding.ItemImportantEntryBinding;
import com.example.journey.databinding.ItemMoodGraphBinding;
import com.example.journey.databinding.ItemTravelMapBinding;
import com.example.journey.databinding.ItemWordCloudBinding;
import com.example.journey.fragments.AnalysisFragment;
import com.example.journey.models.Analysis;
import com.example.journey.models.Entry;
import com.example.journey.models.Location;
import com.example.journey.models.Prompt;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class AnalysisDetailActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private ActivityAnalysisDetailBinding binding;


    public static final String KEY_ANALYSIS = "analysis";
    public static final String KEY_NEW_ANALYSIS = "new analysis";
    private List<String> settings;
    private boolean newAnalysis = false;
    private ScrollView svRoot;
    private TextView tvTitle;
    private Analysis analysis;
    private LinearLayout analysisItemHolder;
    private Spinner settingSpinner;
    private View inflatedLayout;


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
        tvTitle = binding.tvTitle;
        settingSpinner = binding.settingSpinner;
        analysisItemHolder = binding.analysisItemHolder;
        settings = new ArrayList<>();
    }

    private void setupElements() {
        setupTitle();

        if (analysis.isSettingEnabled(Analysis.SETTING_WORD_CLOUD)) {
            settings.add(Analysis.SETTING_WORD_CLOUD);
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_MAPS)) {
            settings.add(Analysis.SETTING_MAPS);
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_IMPORTANT_ENTRIES)) {
            settings.add(Analysis.SETTING_IMPORTANT_ENTRIES);
        }

        if (analysis.isSettingEnabled(Analysis.SETTING_MOOD_GRAPH)) {
            // note firebase has issues, uncomment when bugs are fixed
            // https://github.com/firebase/firebase-android-sdk/issues/361#issuecomment-664593540
            settings.add(Analysis.SETTING_MOOD_GRAPH);
        }

        setupSpinner();

    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_analysis_item, settings);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        settingSpinner.setAdapter(adapter);
        settingSpinner.setOnItemSelectedListener(this);
    }

    private void clearLayout() {
        if (inflatedLayout != null) {
            analysisItemHolder.removeView(inflatedLayout);
        }
    }

    private void setupMoodGraph() {


        HashMap<String, List<Pair<Prompt, Date>>> numericalEntries = new HashMap<>();
        List<Entry> allEntries = analysis.getEntries();
        for (Entry entry : allEntries) {
            List<Prompt> prompts = entry.getPrompts();
            for (Prompt prompt : prompts) {
                if (prompt.getType() == Prompt.TYPE_SLIDER_RESPONSE) {
                    if (numericalEntries.containsKey(prompt.getQuestion())) {
                        List<Pair<Prompt, Date>> currentPrompts = numericalEntries.get(prompt.getQuestion());
                        currentPrompts.add(new Pair<Prompt, Date>(prompt, entry.getDateCreated()));
                    } else {
                        List<Pair<Prompt, Date>> promptList = new ArrayList<>();
                        promptList.add(new Pair<Prompt, Date>(prompt, entry.getDateCreated()));
                        numericalEntries.put(prompt.getQuestion(), promptList);
                    }
                }
            }
        }

        clearLayout();

        for (String question : numericalEntries.keySet()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            inflatedLayout = inflater.inflate(R.layout.item_mood_graph, (LinearLayout) analysisItemHolder, false);
            analysisItemHolder.addView(inflatedLayout);

            ItemMoodGraphBinding binding = ItemMoodGraphBinding.bind(inflatedLayout);
            AnyChartView anyChartView = binding.anyChartView;
            binding.tvTitle.setText(question);

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

            cartesian.title(question);

            cartesian.yAxis(0).title("Y");
            cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

            List<DataEntry> seriesData = new ArrayList<>();
            Collections.sort(allEntries);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            for (Pair<Prompt, Date> promptDatePair : numericalEntries.get(question)) {
                seriesData.add(new ValueDataEntry(format.format(promptDatePair.second),  (int) Float.parseFloat(promptDatePair.first.getStringResponse().get(0))));
            }

            Set set = Set.instantiate();
            set.data(seriesData);
            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

            Line series1 = cartesian.line(series1Mapping);
            series1.name("Value");
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




    }


    private void setupKeyEntries() {

        clearLayout();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflatedLayout = inflater.inflate(R.layout.item_important_entry, (LinearLayout) analysisItemHolder, false);
        analysisItemHolder.addView(inflatedLayout);

        ItemImportantEntryBinding binding = ItemImportantEntryBinding.bind(inflatedLayout);

        final Entry keyEntry = analysis.getImportantEntry();
        binding.itemEntry.tvDateCreated.setText(keyEntry.getDateCreatedString());
        binding.itemEntry.tvPromptsAnswered.setText("Prompts Answered: " + Integer.toString(keyEntry.getPrompts().size()));
        binding.itemEntry.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AnalysisDetailActivity.this, EntryDetailActivity.class);
                i.putExtra(EntryDetailActivity.KEY_ENTRY, keyEntry);
                startActivity(i);
            }
        });
    }

    private void setupWordCloud() {

        clearLayout();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflatedLayout = inflater.inflate(R.layout.item_word_cloud, (LinearLayout) analysisItemHolder, false);
        analysisItemHolder.addView(inflatedLayout);

        ItemWordCloudBinding binding = ItemWordCloudBinding.bind(inflatedLayout);

        WordCloud wordCloud = new WordCloud(analysis.getWordCounts());
        Bitmap bitmap = wordCloud.createBitmap();
        binding.ivWordCloudHolder.setImageBitmap(bitmap);
    }

    private void setupMap() {

        clearLayout();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflatedLayout = inflater.inflate(R.layout.item_travel_map, (LinearLayout) analysisItemHolder, false);
        analysisItemHolder.addView(inflatedLayout);

        ItemTravelMapBinding binding = ItemTravelMapBinding.bind(inflatedLayout);

        binding.transparentImage.setOnTouchListener(new View.OnTouchListener() {

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView selectedText = (TextView) adapterView.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(Color.BLACK);
        }

        switch (settings.get(i)) {
            case Analysis.SETTING_WORD_CLOUD:
                setupWordCloud();
                break;
            case Analysis.SETTING_MAPS:
                setupMap();
                break;
            case Analysis.SETTING_IMPORTANT_ENTRIES:
                setupKeyEntries();
                break;
            case Analysis.SETTING_MOOD_GRAPH:
                setupMoodGraph();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}