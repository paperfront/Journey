package com.example.journey.fragments.responses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.FragmentProudPromptBinding;
import com.example.journey.databinding.FragmentProudResponseBinding;
import com.example.journey.models.Prompt;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProudResponseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProudResponseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROMPT = "prompt";

    // TODO: Rename and change types of parameters
    private Prompt prompt;

    private FragmentProudResponseBinding binding;

    private TextView tvPrompt;
    private TextView tvProud;

    public ProudResponseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProudResponseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProudResponseFragment newInstance(Prompt prompt) {
        ProudResponseFragment fragment = new ProudResponseFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROMPT, prompt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prompt = getArguments().getParcelable(ARG_PROMPT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proud_response, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProudResponseBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        tvPrompt = binding.tvPrompt;
        tvProud = binding.tvProud;
    }

    private void setupElements() {
        setupTextViews();
    }

    private void setupTextViews() {
        tvPrompt.setText(prompt.getQuestion());
        List<String> responses = prompt.getStringResponse();
        for (String response : responses) {
            tvProud.append(" - " + response + "\n");
        }
    }
}