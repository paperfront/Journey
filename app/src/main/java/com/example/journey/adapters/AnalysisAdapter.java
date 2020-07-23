package com.example.journey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.databinding.ItemAnalysisBinding;
import com.example.journey.models.Analysis;

import java.util.List;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.ViewHolder> {

    private Context context;
    private List<Analysis> analysisList;

    public AnalysisAdapter(Context context, List<Analysis> analysisList) {
        this.context = context;
        this.analysisList = analysisList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_analysis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(analysisList.get(position));
    }

    @Override
    public int getItemCount() {
        return analysisList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemAnalysisBinding binding;
        private TextView tvTitle;
        private TextView tvDateCreated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAnalysisBinding.bind(itemView);
            tvTitle = binding.tvTitle;
            tvDateCreated = binding.tvDateCreated;
        }

        private void bind(Analysis analysis) {
            tvTitle.setText(analysis.getTitle());
            tvDateCreated.setText(analysis.getDateCreated().toString());
        }
    }
}
