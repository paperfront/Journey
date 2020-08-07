package com.example.journey.fragments;

import android.graphics.Color;
import android.media.Image;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.journey.R;
import com.example.journey.databinding.FragmentChangePromptsBinding;
import com.example.journey.databinding.FragmentCreateJournalBinding;
import com.example.journey.models.Journal;
import com.example.journey.models.Prompt;

import java.util.List;

public class ChangePromptsFragment extends DialogFragment implements TextView.OnEditorActionListener, ModifyPromptFragment.ModifyPromptDialogListener {

    private Button btSave;
    private FragmentChangePromptsBinding binding;
    private EditPromptsDialogListener listener;
    private Journal journal;

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private CheckBox checkbox4;
    private CheckBox checkbox5;

    private ImageButton btEditPrompt1;
    private ImageButton btEditPrompt2;
    private ImageButton btEditPrompt3;
    private ImageButton btEditPrompt4;
    private ImageButton btEditPrompt5;


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == i) {
            listener.onFinishEditPromptsDialog(journal);
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onFinishModifyPromptDialog(Prompt prompt) {

    }


    public interface EditPromptsDialogListener {
        void onFinishEditPromptsDialog(Journal journal);
    }

    public ChangePromptsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public ChangePromptsFragment(EditPromptsDialogListener listener) {
        this.listener = listener;
    }

    public static ChangePromptsFragment newInstance(String title, Journal journal, EditPromptsDialogListener listener) {
        ChangePromptsFragment frag = new ChangePromptsFragment(listener);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("journal", journal);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_prompts, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        binding = FragmentChangePromptsBinding.bind(view);
        bindElements();
        setupElements();

    }


    private void bindElements() {
        btSave = binding.btSave;
        checkbox1 = binding.prompt1;
        checkbox2 = binding.prompt2;
        checkbox3 = binding.prompt3;
        checkbox4 = binding.prompt4;
        checkbox5 = binding.prompt5;

        btEditPrompt1 = binding.btEditPrompt1;
        btEditPrompt2 = binding.btEditPrompt2;
        btEditPrompt3 = binding.btEditPrompt3;
        btEditPrompt4 = binding.btEditPrompt4;
        btEditPrompt5 = binding.btEditPrompt5;
        journal = getArguments().getParcelable("journal");
    }


    private void setupElements() {
        setupButtons();
        setupEditText();
        setupCheckBoxes();
    }

    private void setupCheckBoxes() {
        List<Prompt> prompts = journal.getPrompts();

        setupCheckbox(0);
        setupCheckbox(1);
        setupCheckbox(2);
        setupCheckbox(3);
        setupCheckbox(4);

        setCheckboxStateChangeListener(checkbox1, 0);
        setCheckboxStateChangeListener(checkbox2, 1);
        setCheckboxStateChangeListener(checkbox3, 2);
        setCheckboxStateChangeListener(checkbox4, 3);
        setCheckboxStateChangeListener(checkbox5, 4);

        setEditButtonClicked(btEditPrompt1, 0);
        setEditButtonClicked(btEditPrompt2, 1);
        setEditButtonClicked(btEditPrompt3, 2);
        setEditButtonClicked(btEditPrompt4, 3);
        setEditButtonClicked(btEditPrompt5, 4);


    }

    private void setupCheckbox(int position) {
        List<Prompt> prompts = journal.getPrompts();
        switch (position) {
            case 0:
                checkbox1.setText(prompts.get(0).getTitle());
                checkbox1.setChecked(prompts.get(0).isEnabled());
                break;
            case 1:
                checkbox2.setText(prompts.get(1).getTitle());
                checkbox2.setChecked(prompts.get(1).isEnabled());
                break;
            case 2:
                checkbox3.setText(prompts.get(2).getTitle());
                checkbox3.setChecked(prompts.get(2).isEnabled());
                break;
            case 3:
                checkbox4.setText(prompts.get(3).getTitle());
                checkbox4.setChecked(prompts.get(3).isEnabled());
                break;
            case 4:
                checkbox5.setText(prompts.get(4).getTitle());
                checkbox5.setChecked(prompts.get(4).isEnabled());
                break;
        }
    }

    private void setCheckboxStateChangeListener(CheckBox checkbox, final int position) {
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                journal.getPrompts().get(position).setEnabled(b);
            }
        });
    }

    private void setEditButtonClicked(ImageButton bt, final int position) {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prompt prompt = journal.getPrompts().get(position);
                ModifyPromptFragment.ModifyPromptDialogListener listener = new ModifyPromptFragment.ModifyPromptDialogListener() {
                    @Override
                    public void onFinishModifyPromptDialog(Prompt prompt) {
                        List<Prompt> prompts = journal.getPrompts();
                        prompts.set(position, prompt);
                        setupCheckbox(position);
                    }
                };
                showModifyPromptFragment(prompt, listener);
            }
        });
    }

    private void setupButtons() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkbox1.isChecked()
                || checkbox2.isChecked()
                || checkbox3.isChecked()
                || checkbox4.isChecked()
                || checkbox5.isChecked()) {
                    listener.onFinishEditPromptsDialog(journal);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "At least 1 prompt must be enabled in order to save.", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });
    }

    private void setupEditText() {
    }

    private void showModifyPromptFragment(Prompt prompt, ModifyPromptFragment.ModifyPromptDialogListener listener) {
        FragmentManager fm = getChildFragmentManager();
        ModifyPromptFragment modifyPromptFragment = ModifyPromptFragment.newInstance("Modify Prompt", prompt, listener);
        modifyPromptFragment.show(fm, "fragment_modify_prompt");
    }
}
