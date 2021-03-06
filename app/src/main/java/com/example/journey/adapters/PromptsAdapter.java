package com.example.journey.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journey.R;
import com.example.journey.databinding.ItemCameraAndGalleryBinding;
import com.example.journey.databinding.ItemMoodBinding;
import com.example.journey.databinding.ItemPromptBinding;
import com.example.journey.databinding.ItemStringResponsesBinding;
import com.example.journey.databinding.ItemTravelBinding;
import com.example.journey.models.Location;
import com.example.journey.models.Prompt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class PromptsAdapter extends RecyclerView.Adapter<PromptsAdapter.ViewHolder> {

    private List<Prompt> prompts;
    private Context context;
    private FragmentActivity activity;
    private FragmentManager fragmentManager;
    private RecyclerView rv;
    private Fragment fragment;

    public PromptsAdapter(Context context, FragmentActivity activity, List<Prompt> prompts, RecyclerView rv) {
        this.context = context;
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
        this.rv = rv;
        getValidPrompts(prompts);
    }

    private void getValidPrompts(List<Prompt> prompts) {
        this.prompts = new ArrayList<>();
        for (Prompt prompt : prompts) {
            if (prompt.isCompleted()) {
                this.prompts.add(prompt);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return prompts.get(position).getType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prompt, parent, false);
        ;
        switch (viewType) {
            case Prompt.TYPE_MEDIA_RESPONSE:
                return new CameraAndGalleryViewHolder(view);
            case Prompt.TYPE_LOCATION_RESPONSE:
                return new TravelViewHolder(view);
            case Prompt.TYPE_STRING_RESPONSE:
                return new StringResponseViewHolder(view);
            case Prompt.TYPE_SLIDER_RESPONSE:
                return new StringResponseViewHolder(view);
            default:
                Timber.e("Invalid view type selected");
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(prompts.get(position));
    }

    @Override
    public int getItemCount() {
        return prompts.size();
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout flPromptHolder;
        private ItemPromptBinding binding;
        private Prompt prompt;
        private View rootView;


        //todo create custom view holders for each prompt response instead of using fragments
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            binding = ItemPromptBinding.bind(itemView);
            flPromptHolder = binding.flPromptHolder;
        }

        private void bind(Prompt prompt) {
            if (!prompt.isCompleted()) {
                rootView.setVisibility(View.GONE);
                return;
            }
            this.prompt = prompt;
            handleUI();
        }

        abstract void handleUI();

        protected FrameLayout getFlPromptHolder() {
            return flPromptHolder;
        }

        protected Prompt getPrompt() {
            return prompt;
        }
    }

    public class CameraAndGalleryViewHolder extends ViewHolder {

        private ImageView ivMedia;
        private TextView tvHeader;
        private ItemCameraAndGalleryBinding binding;


        public CameraAndGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_camera_and_gallery, getFlPromptHolder());
            binding = ItemCameraAndGalleryBinding.bind(view);
            ivMedia = binding.ivMedia;
            tvHeader = binding.tvHeader;
        }

        @Override
        void handleUI() {
            tvHeader.setText(getPrompt().getQuestion());
            List<String> downloadUrls = getPrompt().getStringResponse();
            String firstUrl = downloadUrls.get(0);
            Glide.with(context).load(firstUrl).into(ivMedia);
        }
    }

    public class TravelViewHolder extends ViewHolder implements OnMapReadyCallback {

        private ItemTravelBinding binding;
        private ImageView transparentImageView;
        private TextView tvHeader;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_travel, getFlPromptHolder());
            binding = ItemTravelBinding.bind(view);
            transparentImageView = binding.transparentImage;
            tvHeader = binding.tvHeader;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        void handleUI() {

            tvHeader.setText(getPrompt().getQuestion());

            transparentImageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:

                        case MotionEvent.ACTION_MOVE:
                            // Disallow RV to intercept touch events.
                            rv.requestDisallowInterceptTouchEvent(true);
                            // Disable touch on transparent view
                            return false;

                        case MotionEvent.ACTION_UP:
                            // Allow RV to intercept touch events.
                            rv.requestDisallowInterceptTouchEvent(false);
                            return true;

                        default:
                            return true;
                    }
                }
            });

            setupMap();
        }

        private void setupMap() {
            SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {


            googleMap.getUiSettings().setZoomControlsEnabled(true);
            List<Location> locations = getPrompt().getLocationResponse();
            Location firstLocation = locations.get(0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 10));
            for (Location location : locations) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(location.getName()));
            }


        }
    }

    public class StringResponseViewHolder extends ViewHolder {

        private ItemStringResponsesBinding binding;

        private TextView tvPrompt;
        private TextView tvBody;

        public StringResponseViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_string_responses, getFlPromptHolder());
            binding = ItemStringResponsesBinding.bind(view);
            tvPrompt = binding.tvPrompt;
            tvBody = binding.tvBody;
        }

        @Override
        void handleUI() {
            tvPrompt.setText(getPrompt().getQuestion());
            List<String> responses = getPrompt().getStringResponse();
            for (String response : responses) {
                tvBody.append(" - " + response + "\n");
            }
        }
    }

    public class MoodViewHolder extends ViewHolder {

        private TextView tvMood;
        private ItemMoodBinding binding;

        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_mood, getFlPromptHolder());
            binding = ItemMoodBinding.bind(view);
            tvMood = binding.tvMood;
        }


        @Override
        void handleUI() {
            tvMood.setText(getPrompt().getStringResponse().get(0));
        }
    }
}
