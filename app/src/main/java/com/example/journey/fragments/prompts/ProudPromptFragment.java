package com.example.journey.fragments.prompts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.databinding.FragmentProudPromptBinding;
import com.example.journey.models.Prompt;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProudPromptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProudPromptFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROMPT = "prompt";

    // TODO: Rename and change types of parameters
    private Prompt prompt;

    private EditText etEntry;
    private Button btAdd;
    private FragmentProudPromptBinding binding;
    private TextView tvList;
    private List<String> entries = new ArrayList<>();

    public ProudPromptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProudPromptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProudPromptFragment newInstance(Prompt prompt) {
        ProudPromptFragment fragment = new ProudPromptFragment();
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
        return inflater.inflate(R.layout.fragment_proud_prompt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProudPromptBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements(){
        etEntry = binding.etEntry;
        btAdd = binding.btAdd;
        tvList = binding.tvList;
    }
    private void setupElements(){
        setupButtons();
    }

    private void setupButtons() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entryText = etEntry.getText().toString();
                if (entryText.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your response before pressing the add button", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Added item", Toast.LENGTH_SHORT).show();
                etEntry.setText("");
                tvList.append("- " + entryText + "\n");
                entries.add(entryText);
                prompt.setStringResponse(entries);
                Timber.i("Added the entry: " + etEntry);

            }
        });
    }
}