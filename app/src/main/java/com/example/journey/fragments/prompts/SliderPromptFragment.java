package com.example.journey.fragments.prompts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;
import com.example.journey.databinding.FragmentSliderPromptBinding;
import com.example.journey.models.Prompt;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SliderPromptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderPromptFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROMPT = "prompt";

    private Prompt prompt;
    private Slider slMood;
    private FragmentSliderPromptBinding binding;
    private List<String> mood = new ArrayList<>();

    public SliderPromptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MoodPromptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SliderPromptFragment newInstance(Prompt promt) {
        SliderPromptFragment fragment = new SliderPromptFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROMPT, promt);
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
        return inflater.inflate(R.layout.fragment_slider_prompt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSliderPromptBinding.bind(view);
        slMood = binding.slMood;
        setupSlider();
    }

    private void setupSlider() {
        mood.add(Float.toString(slMood.getValue()));
        prompt.setStringResponse(mood);
        slMood.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                mood.set(0, Float.toString(value));
            }
        });
    }
}