package com.example.journey.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.activities.BeginAnalysisActivity;
import com.example.journey.adapters.AnalysisAdapter;
import com.example.journey.databinding.FragmentAnalysisBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Analysis;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalysisFragment extends Fragment {

    public static final String KEY_NEW_ANALYSIS = "new analysis";
    public static final int RESULT_MAKE_ENTRY = 12345;
    private FragmentAnalysisBinding binding;
    private FloatingActionButton btBeginAnalysis;
    private ProgressBar pbLoading;
    private RecyclerView rvAnalysis;
    private TextView tvNoAnalysis;
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
        tvNoAnalysis = binding.tvNoAnalysis;
        pbLoading = binding.pbLoading;
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
                startActivityForResult(i, RESULT_MAKE_ENTRY);
            }
        });
    }

    private void setupRV() {
        analysisList = new ArrayList<>();
        adapter = new AnalysisAdapter(getContext(), analysisList);
        rvAnalysis.setAdapter(adapter);
        rvAnalysis.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        loadAnalysisList();
    }

    private void loadAnalysisList() {
        pbLoading.setVisibility(View.VISIBLE);
        tvNoAnalysis.setVisibility(View.GONE);
        FirestoreClient.getAnalysisRef().orderBy("dateCreated", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pbLoading.setVisibility(View.GONE);
                Timber.i("Successfully got analysis.");
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    analysisList.add(document.toObject(Analysis.class));
                }
                if (analysisList.isEmpty()) {
                    tvNoAnalysis.setVisibility(View.VISIBLE);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbLoading.setVisibility(View.GONE);
                tvNoAnalysis.setVisibility(View.VISIBLE);
                Timber.e("Failed to get analysis list.");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_MAKE_ENTRY) {
                Timber.i("Received result");
                Analysis analysisReceived = data.getParcelableExtra(KEY_NEW_ANALYSIS);
                analysisList.add(0, analysisReceived);
                adapter.notifyItemInserted(0);
                rvAnalysis.scrollToPosition(0);
            }
        }

    }

}
