package com.example.journey.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.journey.R;
import com.example.journey.activities.BeginAnalysisActivity;
import com.example.journey.adapters.AnalysisAdapter;
import com.example.journey.databinding.FragmentAnalysisBinding;
import com.example.journey.models.Analysis;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private FloatingActionButton btBeginAnalysis;
    private RecyclerView rvAnalysis;

    private AnalysisAdapter adapter;
    private List<Analysis> analysisList;


    public AnalysisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AnalysisFragment.
     */
    public static AnalysisFragment newInstance() {
        AnalysisFragment fragment = new AnalysisFragment();
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
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAnalysisBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        btBeginAnalysis = binding.btBeginAnalysis;
        rvAnalysis = binding.rvAnalysis;
    }

    private void setupElements() {
        setupButtons();
        setupRV();
    }

    private void setupButtons() {
        btBeginAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.i("Begin analysis button clicked.");
                Intent i = new Intent(getContext(), BeginAnalysisActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupRV() {
        adapter = new AnalysisAdapter(getContext(), analysisList);
        rvAnalysis.setAdapter(adapter);
        rvAnalysis.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        loadAnalysisList();
    }

    private void loadAnalysisList() {

    }
}
