package com.example.journey.fragments.responses;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.journey.R;
import com.example.journey.databinding.FragmentCameraAndGalleryResponseBinding;
import com.example.journey.models.Prompt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraAndGalleryResponseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraAndGalleryResponseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROMPT = "prompt";

    // TODO: Rename and change types of parameters
    private Prompt prompt;

    private ImageView ivMedia;
    private FragmentCameraAndGalleryResponseBinding binding;

    public CameraAndGalleryResponseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CameraAndGalleryResponseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraAndGalleryResponseFragment newInstance(Prompt prompt) {
        CameraAndGalleryResponseFragment fragment = new CameraAndGalleryResponseFragment();
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
        return inflater.inflate(R.layout.fragment_camera_and_gallery_response, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCameraAndGalleryResponseBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements() {
        ivMedia = binding.ivMedia;
    }

    private void setupElements() {
        Bitmap image = (Bitmap) prompt.getParcelableResponse().get(0);
        ivMedia.setImageBitmap(image);
    }
}