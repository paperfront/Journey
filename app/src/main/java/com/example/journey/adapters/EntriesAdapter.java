package com.example.journey.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.activities.EntryDetailActivity;
import com.example.journey.databinding.ItemEntryBinding;
import com.example.journey.models.Entry;

import java.util.List;

import timber.log.Timber;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    private List<Entry> entries;
    private Context context;

    public EntriesAdapter(List<Entry> entries, Context context) {
        this.entries = entries;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDateCreated;
        private TextView tvPromptsAnswered;
        private ItemEntryBinding binding;
        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEntryBinding.bind(itemView);
            tvDateCreated = binding.tvDateCreated;
            tvPromptsAnswered = binding.tvPromptsAnswered;
            rootView = binding.getRoot();
        }

        private void bind(final Entry entry) {
            tvDateCreated.setText(entry.getDateCreated().toString());
            tvPromptsAnswered.setText("Prompts Answered: " + Integer.toString(entry.getPrompts().size()));
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.i("Entry clicked at position: " + getAdapterPosition());
                    Intent i = new Intent(context, EntryDetailActivity.class);
                    i.putExtra(EntryDetailActivity.KEY_ENTRY, entry);
                    context.startActivity(i);

                }
            });
        }
    }
}
