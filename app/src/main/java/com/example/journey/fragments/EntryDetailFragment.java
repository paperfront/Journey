package com.example.journey.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;
import com.example.journey.models.Prompt;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROMPTS = "prompts";

    // TODO: Rename and change types of parameters
    private List<Prompt> prompts;

    public EntryDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EntryDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryDetailFragment newInstance(ArrayList<Prompt> prompts) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PROMPTS, prompts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prompts = getArguments().getParcelableArrayList(ARG_PROMPTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_detail, container, false);
    }
}