package com.example.journey.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.databinding.ItemJournalBinding;
import com.example.journey.fragments.ChangePromptsFragment;
import com.example.journey.fragments.CreateJournalFragment;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Journal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.ViewHolder> {



    public static final int MODE_CREATE = 0;
    public static final int MODE_TIMELINE = 1;
    private JournalOnClick onClick;
    private Context context;
    private FragmentManager fragmentManager;
    private ProgressDialog pd;
    private List<Journal> journals;

    public JournalsAdapter(Context context, List<Journal> journals, FragmentManager fragmentManager, ProgressDialog pd, JournalOnClick onClick) {
        this.context = context;
        this.journals = journals;
        this.onClick = onClick;
        this.pd = pd;
        this.fragmentManager = fragmentManager;
    }

    public interface JournalOnClick {
        public void setOnClick(Journal journal);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_journal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(journals.get(position));
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CreateJournalFragment.EditNameDialogListener, ChangePromptsFragment.EditPromptsDialogListener {

        private ItemJournalBinding binding;

        private TextView tvTitle;
        private TextView tvTotalEntries;
        private CardView rootView;
        private ImageButton btEdit;
        private ImageButton btChangePrompts;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemJournalBinding.bind(itemView);
            tvTitle = binding.tvTitle;
            tvTotalEntries = binding.tvTotalEntries;
            btEdit = binding.btEdit;
            btChangePrompts = binding.btChangePrompts;
            rootView = binding.getRoot();
        }


        private void bind(final Journal journal) {
            tvTitle.setText(journal.getTitle());
            tvTotalEntries.setText("Total Entries: " + Integer.toString(journal.getEntries().size()));
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.setOnClick(journal);
                }
            });
            setBackgroundColor(journal);
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog();
                }
            });

            btChangePrompts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChangePromptsFragment();
                }
            });


        }

        private void setBackgroundColor(Journal journal) {
            switch (journal.getColorId()) {
                case 0:
                    rootView.setCardBackgroundColor(context.getColor(R.color.colorPrimary));
                    break;
                case 1:
                    rootView.setCardBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    rootView.setCardBackgroundColor(context.getColor(R.color.red));
                    break;
                case 3:
                    rootView.setCardBackgroundColor(Color.GREEN);
                    break;
                case 4:
                    rootView.setCardBackgroundColor(context.getColor(R.color.brown));
                    break;
                case 5:
                    rootView.setCardBackgroundColor(Color.BLACK);
                    break;
                case 6:
                    rootView.setCardBackgroundColor(Color.YELLOW);
                    break;
                case 7:
                    rootView.setCardBackgroundColor(context.getColor(R.color.orange));
                    break;
                default:
                    rootView.setCardBackgroundColor(context.getColor(R.color.colorPrimary));
                    break;
            }
        }

        private void showEditDialog() {
            FragmentManager fm = fragmentManager;
            CreateJournalFragment createJournalDialogFragment = CreateJournalFragment.newInstance("Create New Journal", journals.get(getAdapterPosition()).getTitle(), this);
            createJournalDialogFragment.show(fm, "fragment_create_journal");
        }

        private void showChangePromptsFragment() {
            FragmentManager fm = fragmentManager;
            ChangePromptsFragment changePromptsFragment = ChangePromptsFragment.newInstance("Create New Journal", journals.get(getAdapterPosition()), this);
            changePromptsFragment.show(fm, "fragment_change_prompts");
        }

        @Override
        public void onFinishEditDialog(String title, int colorId) {

            List<String> journalTitles = new ArrayList<>();
            for (int i = 0; i < journals.size(); i++) {
                if (i != getAdapterPosition()) {
                    Journal journal = journals.get(i);
                    journalTitles.add(journal.getTitle());
                }
            }
            if (journalTitles.contains(title)) {
                Toast.makeText(context, "A journal with that name already exists", Toast.LENGTH_SHORT).show();
                return;
            } else if (title.isEmpty()) {
                Toast.makeText(context, "Journal must be given a title to be created", Toast.LENGTH_SHORT).show();
                return;
            }
            Journal currentJournal = journals.get(getAdapterPosition());
            final String oldTitle = currentJournal.getTitle();
            currentJournal.setColorId(colorId);
            currentJournal.setTitle(title);
            notifyItemChanged(getAdapterPosition());
            pd.show();
            FirestoreClient.getUserRef().collection("journals").document(title).set(currentJournal).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirestoreClient.getUserRef().collection("journals").document(oldTitle).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Edited Journal Successfully", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                }
            });

        }



        @Override
        public void onFinishEditPromptsDialog(Journal journal) {
            FirestoreClient.getUserRef().collection("journals").document(journal.getTitle()).update("prompts", journal.getPrompts()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Successfully saved journal.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
