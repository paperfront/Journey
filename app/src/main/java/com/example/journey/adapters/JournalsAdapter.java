package com.example.journey.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.activities.CreateJournalEntryActivity;
import com.example.journey.activities.JournalsActivity;
import com.example.journey.databinding.ItemJournalBinding;
import com.example.journey.fragments.CreateJournalEntryFragment;
import com.example.journey.models.Journal;
import com.example.journey.models.Track;

import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.ViewHolder> {


    private Context context;
    private JournalsActivity activity;
    private List<Journal> journals;

    public JournalsAdapter(Context context, JournalsActivity activity, List<Journal> journals) {
        this.context = context;
        this.activity = activity;
        this.journals = journals;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemJournalBinding binding;

        private TextView tvTitle;
        private TextView tvTotalEntries;
        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemJournalBinding.bind(itemView);
            tvTitle = binding.tvTitle;
            tvTotalEntries = binding.tvTotalEntries;
            rootView = itemView;
        }

        private void bind(final Journal journal) {
            tvTitle.setText(journal.getTitle());
            tvTotalEntries.setText("Total Entries: " + Integer.toString(journal.getEntries().size()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToCreateJournalEntryActivity(journal);
                }
            });
        }

        private void goToCreateJournalEntryActivity(Journal journal) {
            //todo replace hardcoded track with the users current track
            Intent i = new Intent(context, CreateJournalEntryActivity.class);
            i.putExtra(CreateJournalEntryActivity.KEY_JOURNAL, journal.getTitle());
            context.startActivity(i);
        }
    }
}
