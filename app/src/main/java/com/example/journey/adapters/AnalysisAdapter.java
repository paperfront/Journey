package com.example.journey.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.activities.AnalysisDetailActivity;
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
        private View rootView;
        private TextView tvTitle;
        private TextView tvDateCreated;
        private ImageView ivWordCloud;
        private ImageView ivLocations;
        private ImageView ivKeyEntry;
        private ImageView ivMood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAnalysisBinding.bind(itemView);
            tvTitle = binding.tvTitle;
            tvDateCreated = binding.tvDateCreated;
            rootView = binding.getRoot();
            ivWordCloud = binding.ivWordCloud;
            ivLocations = binding.ivLocations;
            ivKeyEntry = binding.ivKeyEntry;
            ivMood = binding.ivMood;

        }

        private void bind(final Analysis analysis) {
            tvTitle.setText(analysis.getTitle());
            tvDateCreated.setText(analysis.getDateCreatedString());
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, AnalysisDetailActivity.class);
                    i.putExtra(AnalysisDetailActivity.KEY_ANALYSIS, analysis);
                    context.startActivity(i);
                }
            });

            if (analysis.isSettingEnabled(Analysis.SETTING_WORD_CLOUD)) {
                setActivated(ivWordCloud);
            } else {
                setDisabled(ivWordCloud);
            }
            if (analysis.isSettingEnabled(Analysis.SETTING_MAPS)) {
                setActivated(ivLocations);
            } else {
                setDisabled(ivLocations);
            }
            if (analysis.isSettingEnabled(Analysis.SETTING_IMPORTANT_ENTRIES)) {
                setActivated(ivKeyEntry);
            } else {
                setDisabled(ivKeyEntry);
            }
            if (analysis.isSettingEnabled(Analysis.SETTING_MOOD_GRAPH)) {
                setActivated(ivMood);
            } else {
                setDisabled(ivMood);
            }

        }

        private void setActivated(ImageView iv) {
            Drawable background = iv.getBackground();
            DrawableCompat.setTint(background, Color.BLACK);
            iv.setBackground(background);
        }

        private void setDisabled(ImageView iv) {
            Drawable background = iv.getBackground();
            DrawableCompat.setTint(background, Color.WHITE);
            iv.setBackground(background);
        }
    }
}
