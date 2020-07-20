package com.example.journey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.databinding.ItemPromptBinding;
import com.example.journey.models.Prompt;

import java.util.List;


public class PromptsAdapter extends RecyclerView.Adapter<PromptsAdapter.ViewHolder> {

    private List<Prompt> prompts;
    private Context context;
    private FragmentManager fragmentManager;

    public PromptsAdapter(Context context, FragmentManager fragmentManager, List<Prompt> prompts) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.prompts = prompts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prompt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(prompts.get(position));
    }

    @Override
    public int getItemCount() {
        return prompts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout flPromptHolder;
        private ItemPromptBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPromptBinding.bind(itemView);
            flPromptHolder = binding.flPromptHolder;
        }

        private void bind(Prompt prompt) {
            fragmentManager.beginTransaction().replace(binding.flPromptHolder.getId(), prompt.getResponseFragment()).commit();
        }
    }
}
