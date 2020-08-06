package com.example.journey.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import timber.log.Timber;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.ViewHolder> {

    private List<Entry> entries;
    private Context context;
    private String journalTitle;
    private int colorId;
    private ListUpdater updater;

    public EntriesAdapter(List<Entry> entries, Context context, String journalTitle, int colorId, ListUpdater updater) {
        this.entries = entries;
        this.context = context;
        this.journalTitle = journalTitle;
        this.colorId = colorId;
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
        private ImageView ivFavoriteHeart;
        private View rootView;
        private ImageButton btDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEntryBinding.bind(itemView);
            tvDateCreated = binding.tvDateCreated;
            tvPromptsAnswered = binding.tvPromptsAnswered;
            ivPopupHeart = binding.ivPopupHeart;
            ivFavoriteHeart = binding.ivFavoriteHeart;
            btDelete = binding.btDelete;
            rootView = binding.getRoot();
        }

        private void bind(final Entry entry) {
            tvDateCreated.setText(entry.getDateCreatedString());
            tvPromptsAnswered.setText("Prompts Answered: " + Integer.toString(entry.getPrompts().size()));
            if (entry.isFavorite()) {
                setToRed(ivFavoriteHeart);
            } else {
                setToGray(ivFavoriteHeart);
            }
            setBackgroundColor();
            rootView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        Timber.d("onLongPress");
                        ivPopupHeart.setVisibility(View.VISIBLE);
                        if (entry.isFavorite()) {
                            setToRed(ivPopupHeart);
                            YoYo.with(Techniques.SlideOutRight)
                                    .duration(1000)
                                    .playOn(ivPopupHeart);
                            setToGray(ivFavoriteHeart);
                        } else {
                            setToRed(ivPopupHeart);
                            setToRed(ivFavoriteHeart);
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


            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Do you really want to delete this entry?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    final Entry currentEntry = entries.get(getAdapterPosition());
                                    Toast.makeText(context, "Entry deleted!", Toast.LENGTH_SHORT).show();
                                    FirestoreClient.getUserRef().collection("journals").document(journalTitle).update("entries", FieldValue.arrayRemove(currentEntry));
                                    FirestoreClient.getAllEntriesRef().whereEqualTo("dateCreated", currentEntry.getDateCreated()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                            if (documents.size() != 1) {
                                                Timber.e("Error getting document inside allDocuments collection.");
                                                return;
                                            } else {
                                                DocumentSnapshot document = documents.get(0);
                                                FirestoreClient.getAllEntriesRef().document(document.getId()).delete();
                                            }
                                        }
                                    });
                                    entries.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
        }

        private void setBackgroundColor() {
            switch (colorId) {
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

        private void setToRed(ImageView iv) {
            Drawable fullHeart = context.getDrawable(R.drawable.ic_baseline_favorite_24);
            DrawableCompat.setTint(fullHeart, Color.RED);
            iv.setBackground(fullHeart);
        }

        private void setToGray(ImageView iv) {
            Drawable fullHeart = context.getDrawable(R.drawable.ic_baseline_favorite_24);
            DrawableCompat.setTint(fullHeart, Color.GRAY);
            iv.setBackground(fullHeart);
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
                            FirestoreClient.getAllEntriesRef().whereEqualTo("dateCreated", currentEntry.getDateCreated()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                                    if (documents.size() != 1) {
                                        Timber.e("Error getting document inside allDocuments collection.");
                                        return;
                                    } else {
                                        DocumentSnapshot document = documents.get(0);
                                        FirestoreClient.getAllEntriesRef().document(document.getId()).update("favorited", currentEntry.isFavorite());
                                    }
                                }
                            });
                            updater.updateItems(false);
                        }
                    });


        }
    }
}
