package com.example.journey.fragments.responses;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.journey.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraAndGalleryResponseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraAndGalleryResponseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Bitmap mParam1;

    public CameraAndGalleryResponseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CameraAndGalleryResponseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraAndGalleryResponseFragment newInstance(Bitmap image) {
        CameraAndGalleryResponseFragment fragment = new CameraAndGalleryResponseFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_and_gallery_response, container, false);
    }
}