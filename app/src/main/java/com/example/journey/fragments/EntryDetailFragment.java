package com.example.journey.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;
import com.example.journey.adapters.PromptsAdapter;
import com.example.journey.databinding.FragmentEntryDetailBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Prompt;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryDetailFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ENTRY = "entry";

    private Entry entry;
    private List<Prompt> prompts;

    private FragmentEntryDetailBinding binding;

    private RecyclerView rvPrompts;

    private PromptsAdapter adapter;

    public EntryDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EntryDetailFragment.
     */
    public static EntryDetailFragment newInstance(Entry entry) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ENTRY, entry);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entry = getArguments().getParcelable(ARG_ENTRY);
            prompts = entry.getPrompts();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEntryDetailBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements(){
        rvPrompts = binding.rvPrompts;
    }

    private void setupElements(){
        setupRV();
    }

    private void setupRV() {
        adapter = null;//new PromptsAdapter(getContext(), getActivity(), this, prompts, rvPrompts);
        rvPrompts.setAdapter(adapter);
        rvPrompts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    private void saveEntry() {
        String journalName = "first";
        CollectionReference entryRef =  FirestoreClient.getReference().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("journals")
                .document(journalName)
                .collection("entries");
        entryRef.add(entry)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Timber.d("DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.w(e, "Error adding document");
                    }
                });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        saveEntry();
    }
}