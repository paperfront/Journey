package com.example.journey.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.FragmentCreateJournalEntryBinding;
import com.example.journey.models.Prompt;
import com.example.journey.models.Track;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateJournalEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateJournalEntryFragment extends Fragment {

    private FragmentManager fragmentManager;
    private TextView tvQuestion;

    private FragmentCreateJournalEntryBinding binding;


    private static final String ARG_TRACK = "track";

    private Track track;
    private List<Prompt> prompts;
    private int currentPromptCounter = 0;
    private Prompt currentPrompt;

    public CreateJournalEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CreateJournalEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateJournalEntryFragment newInstance(Track track) {
        CreateJournalEntryFragment fragment = new CreateJournalEntryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRACK, track);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.track = (Track) getArguments().getSerializable(ARG_TRACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_journal_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCreateJournalEntryBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        tvQuestion = binding.tvQuestion;
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    private void setupElements() {
        getPrompts();
        loadNextPrompt();
    }

    private void getPrompts() {
        prompts = Prompt.getPromptsOfType(track);
    }

    private void loadNextPrompt() {
        if (currentPromptCounter >= prompts.size()) {
            Timber.d("Finished loading all prompts for track " + track.toString());
            return;
        } else {
            currentPrompt = prompts.get(currentPromptCounter);
            currentPromptCounter += 1;
            setupPrompt();
        }
    }

    private void setupPrompt() {
        tvQuestion.setText(currentPrompt.getQuestion());
        fragmentManager.beginTransaction().replace(binding.flPromptHolder.getId(), currentPrompt.getNewPromptFragment()).commit();
        Timber.d("Loaded prompt: " + currentPrompt.name());
    }
}