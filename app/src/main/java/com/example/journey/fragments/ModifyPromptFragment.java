package com.example.journey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.journey.R;
import com.example.journey.databinding.FragmentChangePromptsBinding;
import com.example.journey.databinding.FragmentModifyPromptBinding;
import com.example.journey.models.Journal;
import com.example.journey.models.Prompt;

import java.util.List;

public class ModifyPromptFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private Button btSave;
    private FragmentModifyPromptBinding binding;
    private ModifyPromptDialogListener listener;
    private Prompt prompt;

    private Spinner typeSpinner;

    private EditText etPromptTitle;
    private EditText etPromptQuestion;



    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == i) {
            listener.onFinishModifyPromptDialog(prompt);
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


    public interface ModifyPromptDialogListener {
        void onFinishModifyPromptDialog(Prompt prompt);
    }

    public ModifyPromptFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public ModifyPromptFragment(ModifyPromptDialogListener listener) {
        this.listener = listener;
    }

    public static ModifyPromptFragment newInstance(String title, Prompt prompt, ModifyPromptDialogListener listener) {
        ModifyPromptFragment frag = new ModifyPromptFragment(listener);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelable("prompt", prompt);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modify_prompt, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        binding = FragmentModifyPromptBinding.bind(view);
        bindElements();
        setupElements();

    }


    private void bindElements() {
        btSave = binding.btSave;
        prompt = getArguments().getParcelable("prompt");
        etPromptQuestion = binding.etPromptQuestion;
        etPromptTitle = binding.etPromptTitle;
        typeSpinner = binding.typeSpinner;
    }


    private void setupElements() {
        setupEditText();
        setupButton();
        setupSpinner();
    }

    private void setupSpinner() {
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLACK);
                }
                switch (i) {
                    case 0:
                        prompt.setType(Prompt.TYPE_STRING_RESPONSE);
                        break;
                    case 1:
                        prompt.setType(Prompt.TYPE_SLIDER_RESPONSE);
                        break;
                    case 2:
                        prompt.setType(Prompt.TYPE_MEDIA_RESPONSE);
                        break;
                    case 3:
                        prompt.setType(Prompt.TYPE_LOCATION_RESPONSE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.prompt_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);

        switch (prompt.getType()) {
            case Prompt.TYPE_STRING_RESPONSE:
                typeSpinner.setSelection(0);
                break;
            case Prompt.TYPE_SLIDER_RESPONSE:
                typeSpinner.setSelection(1);
                break;
            case Prompt.TYPE_MEDIA_RESPONSE:
                typeSpinner.setSelection(2);
                break;
            case Prompt.TYPE_LOCATION_RESPONSE:
                typeSpinner.setSelection(3);
                break;
        }
    }

    private void setupButton() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPromptQuestion.getText().toString().isEmpty() || etPromptTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "All fields must be filled out to save.", Toast.LENGTH_SHORT).show();
                    return;
                }
                prompt.setQuestion(etPromptQuestion.getText().toString());
                prompt.setTitle(etPromptTitle.getText().toString());
                listener.onFinishModifyPromptDialog(prompt);
                dismiss();
            }
        });
    }




    private void setupEditText() {
        etPromptTitle.setText(prompt.getTitle());
        etPromptQuestion.setText(prompt.getQuestion());
    }

}