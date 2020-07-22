package com.example.journey.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.journey.R;
import com.example.journey.databinding.FragmentCreateJournalEntryBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Prompt;
import com.example.journey.models.Track;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private ProgressBar pbLoading;
    private Button btNext;

    private FragmentCreateJournalEntryBinding binding;


    private static final String ARG_TRACK = "track";

    private Track track;
    private ArrayList<Prompt> prompts;
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
        pbLoading = binding.pbLoading;
        btNext = binding.btNext;
    }

    private void setupElements() {
        setupButtons();
        getPrompts(track);
    }

    private void setupButtons() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextPrompt();
            }
        });
    }



    private void getPrompts(Track track) {
        FirebaseFirestore db = FirestoreClient.getReference();
        startLoading();
        db.collection(track.getKey()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    stopLoading();
                    QuerySnapshot snapshot = task.getResult();
                    if (!snapshot.isEmpty()) {
                        prompts = new ArrayList<>();
                        Timber.d("QuerySnapshot has some data");
                        List<DocumentSnapshot> documents = snapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            prompts.add(document.toObject(Prompt.class));
                        }
                        loadNextPrompt();
                    } else {
                        Timber.d("Query yielded no results");
                    }
                } else {
                    Timber.d(task.getException(), "get failed with ");
                }
            }
        });

    }

    private void startLoading() {
        pbLoading.setVisibility(View.VISIBLE);
        btNext.setVisibility(View.INVISIBLE);
    }

    private void stopLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
        btNext.setVisibility(View.VISIBLE);
    }



    private void loadNextPrompt() {
         if (currentPromptCounter >= prompts.size()) {
            Timber.d("Finished loading all prompts for track " + track.toString());
            goToDetailPage();
        } else {
             if (currentPromptCounter == prompts.size() - 1) {
                 btNext.setText("Finish");
             }
            currentPrompt = prompts.get(currentPromptCounter);
            currentPromptCounter += 1;
            setupPrompt();
        }
    }

    private void setupPrompt() {
        tvQuestion.setText(currentPrompt.getQuestion());
        fragmentManager.beginTransaction().replace(binding.flPromptHolder.getId(), currentPrompt.getPromptFragment()).commit();
        Timber.d("Loaded prompt: " + currentPrompt.getPromptId());
    }

    public FragmentManager getCurrentFragmentManager() {
        return fragmentManager;
    }

    private void goToDetailPage() {
        Entry entry = new Entry(prompts);
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, MainPageFragment.newInstance())
                .addToBackStack(null)
                .commit();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, EntryDetailFragment.newInstance(entry))
                .addToBackStack(null)
                .commit();

    }
}