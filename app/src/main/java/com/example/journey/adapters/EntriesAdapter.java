package com.example.journey.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.journey.R;
import com.example.journey.activities.EntryDetailActivity;
import com.example.journey.databinding.ItemEntryBinding;
import com.example.journey.helpers.FirestoreClient;
import com.example.journey.models.Entry;
import com.example.journey.models.Journal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

import timber.log.Timber;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    private List<Entry> entries;
    private Context context;
    private String journalTitle;
    private ListUpdater updater;

    public EntriesAdapter(List<Entry> entries, Context context, String journalTitle, ListUpdater updater) {
        this.entries = entries;
        this.context = context;
        this.journalTitle = journalTitle;
        this.updater = updater;
    }

    public interface ListUpdater {
        public void updateItems(boolean toggle);
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
        private ImageView ivPopupHeart;
        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEntryBinding.bind(itemView);
            tvDateCreated = binding.tvDateCreated;
            tvPromptsAnswered = binding.tvPromptsAnswered;
            ivPopupHeart = binding.ivPopupHeart;
            rootView = binding.getRoot();
        }

        private void bind(final Entry entry) {
            tvDateCreated.setText(entry.getDateCreated().toString());
            tvPromptsAnswered.setText("Prompts Answered: " + Integer.toString(entry.getPrompts().size()));

            rootView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        Timber.d("onDoubleTap");
                        ivPopupHeart.setVisibility(View.VISIBLE);
                        if (entry.isFavorite()) {
                            Drawable fullHeart = context.getDrawable(R.drawable.ic_baseline_favorite_24);
                            DrawableCompat.setTint(fullHeart, Color.RED);
                            ivPopupHeart.setBackground(fullHeart);
                            YoYo.with(Techniques.SlideOutRight)
                                    .duration(1000)
                                    .playOn(ivPopupHeart);
                        } else {
                            Drawable fullHeart = context.getDrawable(R.drawable.ic_baseline_favorite_24);
                            DrawableCompat.setTint(fullHeart, Color.RED);
                            ivPopupHeart.setBackground(fullHeart);
                            YoYo.with(Techniques.Landing)
                                    .duration(500)
                                    .playOn(ivPopupHeart);
                            YoYo.with(Techniques.TakingOff)
                                    .duration(500)
                                    .playOn(ivPopupHeart);
                        }

                        handleLikeAction(entry);
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        Timber.i("Entry clicked at position: " + getAdapterPosition());
                        Intent i = new Intent(context, EntryDetailActivity.class);
                        i.putExtra(EntryDetailActivity.KEY_ENTRY, entry);
                        context.startActivity(i);
                        return super.onSingleTapUp(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }

        private void handleLikeAction(final Entry currentEntry) {

            FirestoreClient.getUserRef().collection("journals").document(journalTitle).update("entries", FieldValue.arrayRemove(currentEntry))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (currentEntry.isFavorite()) {
                                Timber.i("Uniking entry...");
                                currentEntry.setFavorite(false);
                            } else {
                                Timber.i("Liking post...");
                                currentEntry.setFavorite(true);
                            }
                            FirestoreClient.getUserRef().collection("journals").document(journalTitle).update("entries", FieldValue.arrayUnion(currentEntry));
                            updater.updateItems(false);
                        }
                    });


        }
    }
}
