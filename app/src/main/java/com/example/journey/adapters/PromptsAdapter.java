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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journey.R;
import com.example.journey.databinding.ItemCameraAndGalleryBinding;
import com.example.journey.databinding.ItemPromptBinding;
import com.example.journey.databinding.ItemProudBinding;
import com.example.journey.databinding.ItemTravelBinding;
import com.example.journey.models.Location;
import com.example.journey.models.Prompt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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
        return prompts.get(position).getPromptId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_prompt, parent, false);;
        switch (viewType) {
            case Prompt.CAMERA_AND_GALLERY:
                return new CameraAndGalleryViewHolder(view);
            case Prompt.TRAVEL:
                return new TravelViewHolder(view);
            case Prompt.PROUD:
                return new ProudViewHolder(view);
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
        return  prompts.size();
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
        private ItemCameraAndGalleryBinding binding;


        public CameraAndGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_camera_and_gallery, getFlPromptHolder());
            binding = ItemCameraAndGalleryBinding.bind(view);
            ivMedia = binding.ivMedia;
        }

        @Override
        void handleUI() {
            List<String> downloadUrls = getPrompt().getStringResponse();
            String firstUrl = downloadUrls.get(0);
            Glide.with(context).load(firstUrl).into(ivMedia);
        }
    }

    public class TravelViewHolder extends ViewHolder implements OnMapReadyCallback {

        private ItemTravelBinding binding;
        private ImageView transparentImageView;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_travel, getFlPromptHolder());
            binding = ItemTravelBinding.bind(view);
            transparentImageView = binding.transparentImage;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        void handleUI() {

            transparentImageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:

                        case MotionEvent.ACTION_MOVE:
                            // Disallow ScrollView to intercept touch events.
                            rv.requestDisallowInterceptTouchEvent(true);
                            // Disable touch on transparent view
                            return false;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
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
            GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            List<Location> locations = getPrompt().getLocationResponse();
            Location firstLocation = locations.get(0);
            for (Location location : locations) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(location.getName()));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 10));

        }
    }

    public class ProudViewHolder extends ViewHolder {

        private ItemProudBinding binding;

        private TextView tvPrompt;
        private TextView tvProud;

        public ProudViewHolder(@NonNull View itemView) {
            super(itemView);
            View view = View.inflate(context, R.layout.item_proud, getFlPromptHolder());
            binding = ItemProudBinding.bind(view);
            tvPrompt = binding.tvPrompt;
            tvProud = binding.tvProud;
        }

        @Override
        void handleUI() {
            tvPrompt.setText(getPrompt().getQuestion());
            List<String> responses = getPrompt().getStringResponse();
            for (String response : responses) {
                tvProud.append(" - " + response + "\n");
            }
        }
    }
}
