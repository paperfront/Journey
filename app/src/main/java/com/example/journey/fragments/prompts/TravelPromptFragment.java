package com.example.journey.fragments.prompts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TravelPromptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelPromptFragment extends Fragment {


    public TravelPromptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TravelPromptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TravelPromptFragment newInstance() {
        TravelPromptFragment fragment = new TravelPromptFragment();
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
        return inflater.inflate(R.layout.fragment_travel_prompt, container, false);
    }
}