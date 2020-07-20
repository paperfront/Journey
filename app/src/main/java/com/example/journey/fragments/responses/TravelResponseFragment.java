package com.example.journey.fragments.responses;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;
import com.example.journey.models.Prompt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TravelResponseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelResponseFragment extends Fragment {

    private static final String ARG_PROMPT = "prompt";

    private Prompt prompt;

    public TravelResponseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TravelResponseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TravelResponseFragment newInstance(Prompt prompt) {
        TravelResponseFragment fragment = new TravelResponseFragment();
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
        return inflater.inflate(R.layout.fragment_travel_response, container, false);
    }
}