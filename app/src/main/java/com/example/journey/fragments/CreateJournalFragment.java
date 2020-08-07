package com.example.journey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.journey.R;
import com.example.journey.databinding.FragmentCreateJournalBinding;

public class CreateJournalFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText etJournalName;
    private Button btSave;
    private Spinner colorSpinner;
    private FragmentCreateJournalBinding binding;
    private int lastSelectedColor = 0;
    private String currentName;
    private EditNameDialogListener listener;

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == i) {
            // Return input text back to activity through the implemented listener
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(etJournalName.getText().toString(), lastSelectedColor);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }


    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText, int colorId);
    }

    public CreateJournalFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public CreateJournalFragment(EditNameDialogListener listener) {
        this.listener = listener;
    }

    public static CreateJournalFragment newInstance(String title, String currentName, EditNameDialogListener listener) {
        CreateJournalFragment frag = new CreateJournalFragment(listener);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("currentName", currentName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_journal, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        binding = FragmentCreateJournalBinding.bind(view);
        bindElements();
        setupElements();

    }


    private void bindElements() {
        btSave = binding.btSave;
        etJournalName = binding.etJournalName;
        colorSpinner = binding.colorSpinner;
    }


    private void setupElements() {
        setupButtons();
        setupEditText();
        setupSpinner();
    }

    private void setupSpinner() {
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lastSelectedColor = i;
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                lastSelectedColor = 0;
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(adapter);
    }

    private void setupButtons() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFinishEditDialog(etJournalName.getText().toString(), lastSelectedColor);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });
    }

    private void setupEditText() {
        etJournalName.setOnEditorActionListener(this);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etJournalName.setText(getArguments().getString("currentName", ""));
        etJournalName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
