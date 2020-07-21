package com.example.journey.fragments.responses;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;
import com.example.journey.databinding.FragmentTravelResponseBinding;
import com.example.journey.models.Location;
import com.example.journey.models.Prompt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TravelResponseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelResponseFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PROMPT = "prompt";

    private Prompt prompt;

    private FragmentTravelResponseBinding binding;

    public TravelResponseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TravelResponseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TravelResponseFragment newInstance(Prompt prompt) {
        TravelResponseFragment fragment = new TravelResponseFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROMPT, prompt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prompt = getArguments().getParcelable(ARG_PROMPT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel_response, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentTravelResponseBinding.bind(view);
        bindElements();
    }

    private void bindElements() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(binding.flContainer.getId(), mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        List<Location> locations = prompt.getLocationResponse();
        Location firstLocation = locations.get(0);
        for (Location location : locations) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getName()));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(firstLocation.getLatitude(), firstLocation.getLongitude()), 10));
    }
}