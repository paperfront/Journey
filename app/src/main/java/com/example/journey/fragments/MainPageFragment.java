package com.example.journey.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;
import com.example.journey.databinding.FragmentMainPageBinding;
import com.example.journey.models.Track;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {


    private FragmentMainPageBinding binding;

    private FloatingActionButton btCompose;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance() {
        MainPageFragment fragment = new MainPageFragment();
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
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainPageBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        btCompose = binding.btComposeEntry;
    }

    private void setupElements() {
        setupButtons();
    }

    private void setupButtons() {
        btCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Compose Entry button clicked.");

                //todo replace hardcoded track with the users current track
                CreateJournalEntryFragment fragment = CreateJournalEntryFragment.newInstance(Track.GENERAL);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentHolder, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}