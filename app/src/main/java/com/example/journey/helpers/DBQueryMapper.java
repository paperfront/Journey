package com.example.journey.helpers;

import androidx.fragment.app.Fragment;

import com.example.journey.fragments.prompts.CameraAndGalleryFragment;
import com.example.journey.models.Prompt;
import com.example.journey.models.Track;

import timber.log.Timber;

public class DBQueryMapper {

    public static Fragment getFragmentForPrompt(Prompt prompt) {

       switch (prompt.getPromptId()) {
           case Prompt.CAMERA_AND_GALLERY:
               return CameraAndGalleryFragment.newInstance();
           default:
               Timber.e("No Fragment Found for that prompt ID");
               return null;
       }
    }
}
