package com.example.journey.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey.R;
import com.example.journey.databinding.ItemJournalBinding;
import com.example.journey.models.Journal;

import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.ViewHolder> {



    public static final int MODE_CREATE = 0;
    public static final int MODE_TIMELINE = 1;
    private JournalOnClick onClick;
    private Context context;
    private List<Journal> journals;

    public JournalsAdapter(Context context, List<Journal> journals, JournalOnClick onClick) {
        this.context = context;
        this.journals = journals;
        this.onClick = onClick;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemJournalBinding binding;

        private TextView tvTitle;
        private TextView tvTotalEntries;
        private View rootView;
        private ImageButton btEdit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemJournalBinding.bind(itemView);
            tvTitle = binding.tvTitle;
            tvTotalEntries = binding.tvTotalEntries;
            btEdit = binding.btEdit;
            rootView = itemView;
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

            switch (journal.getColorId()) {
                case 0:
                    rootView.setBackgroundColor(context.getColor(R.color.colorPrimary));
                    break;
                case 1:
                    rootView.setBackgroundColor(Color.BLUE);
                    break;
                case 2:
                    rootView.setBackgroundColor(context.getColor(R.color.red));
                    break;
                case 3:
                    rootView.setBackgroundColor(Color.GREEN);
                    break;
                case 4:
                    rootView.setBackgroundColor(context.getColor(R.color.brown));
                    break;
                case 5:
                    rootView.setBackgroundColor(Color.BLACK);
                    break;
                case 6:
                    rootView.setBackgroundColor(Color.YELLOW);
                    break;
                case 7:
                    rootView.setBackgroundColor(context.getColor(R.color.orange));
                    break;
                default:
                    rootView.setBackgroundColor(context.getColor(R.color.colorPrimary));
                    break;
            }

        }


    }
}
