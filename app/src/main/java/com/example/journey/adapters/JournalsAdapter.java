package com.example.journey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.databinding.ItemJournalBinding;
import com.example.journey.models.Journal;

import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.ViewHolder> {


    private Context context;
    private List<Journal> journals;

    public JournalsAdapter(Context context, List<Journal> journals) {
        this.context = context;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemJournalBinding.bind(itemView);
            tvTitle = binding.tvTitle;
        }

        private void bind(Journal journal) {
            tvTitle.setText(journal.getTitle());
        }
    }
}
