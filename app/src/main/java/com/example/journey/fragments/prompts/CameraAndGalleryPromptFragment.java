package com.example.journey.fragments.prompts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.journey.R;
import com.example.journey.databinding.FragmentCameraAndGalleryBinding;
import com.example.journey.helpers.BitmapScaler;
import com.example.journey.helpers.ImageUtils;
import com.example.journey.models.Prompt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraAndGalleryPromptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraAndGalleryPromptFragment extends Fragment {


    private static final String ARG_PROMPT = "prompt";

    private Prompt prompt;

    private TextView tvLaunchCamera;
    private TextView tvOpenGallery;
    private ImageView ivMedia;

    private FragmentCameraAndGalleryBinding binding;
    private File photoFile;
    private List<Bitmap> allMedia = new ArrayList<>();

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_PHOTO_CODE = 1046;
    private static final String PHOTO_FILENAME = "photo.jpg";


    public CameraAndGalleryPromptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CameraAndGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraAndGalleryPromptFragment newInstance(Prompt prompt) {
        CameraAndGalleryPromptFragment fragment = new CameraAndGalleryPromptFragment();
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
        return inflater.inflate(R.layout.fragment_camera_and_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCameraAndGalleryBinding.bind(view);
        bindElements();
        setupElements();
    }

    private void bindElements(){
        tvLaunchCamera = binding.tvLaunchCamera;
        tvOpenGallery = binding.tvOpenGallery;
        ivMedia = binding.ivMedia;
    }
    private void setupElements(){
        setupTextViews();
    }

    private void setupTextViews() {
        tvLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("Launch Camera Clicked");
                launchCamera();
            }
        });

        tvOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGallery();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                // See code above
                Uri takenPhotoUri = Uri.fromFile(photoFile);
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 50);
                Bitmap takenImage = resizedBitmap;
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivMedia.setImageBitmap(takenImage);
                allMedia.add(takenImage);
                prompt.setResponse(allMedia);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = ImageUtils.loadFromUri(photoUri, getContext());
            ivMedia.setImageBitmap(selectedImage);
            allMedia.add(selectedImage);
            prompt.setResponse(allMedia);
        }

    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = ImageUtils.getPhotoFileUri(getContext(), PHOTO_FILENAME);

        Uri fileProvider = FileProvider.getUriForFile(getContext(),
                "com.codepath.fileprovider.journey", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Trigger gallery selection for a photo
    public void launchGallery() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }
}