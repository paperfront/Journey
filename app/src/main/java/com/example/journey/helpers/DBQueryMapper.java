package com.example.journey.helpers;

import android.media.Image;

import androidx.fragment.app.Fragment;

import com.example.journey.fragments.prompts.CameraAndGalleryPromptFragment;
import com.example.journey.fragments.prompts.TravelPromptFragment;
import com.example.journey.fragments.responses.CameraAndGalleryResponseFragment;
import com.example.journey.fragments.responses.TravelResponseFragment;
import com.example.journey.models.Prompt;
import com.google.android.libraries.places.api.model.Place;

import timber.log.Timber;

public class DBQueryMapper {

    public static Fragment getFragmentForPrompt(Prompt prompt) {

       switch (prompt.getPromptId()) {
           case Prompt.CAMERA_AND_GALLERY:
               return CameraAndGalleryPromptFragment.newInstance(prompt);
           case Prompt.TRAVEL:
               return TravelPromptFragment.newInstance(prompt);
           default:
               Timber.e("No Fragment Found for that prompt ID");
               return null;
       }
    }

    public static Fragment getResponseFragmentForPrompt(Prompt prompt) {
        switch (prompt.getPromptId()) {
            case Prompt.CAMERA_AND_GALLERY:
                return CameraAndGalleryResponseFragment.newInstance(prompt);
            case Prompt.TRAVEL:
                return TravelResponseFragment.newInstance(prompt);
            default:
                Timber.e("No Fragment Found for that prompt ID");
                return null;
        }
    }

    public static Class getResponseClassForPromptId(int id) {
        switch (id) {
            case Prompt.CAMERA_AND_GALLERY:
                return Image.class;
            case Prompt.TRAVEL:
                return Place.class;
            default:
                Timber.e("No Fragment Found for that prompt ID");
                return null;
        }
    }
}
