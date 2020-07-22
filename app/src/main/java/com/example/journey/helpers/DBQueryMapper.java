package com.example.journey.helpers;

import androidx.fragment.app.Fragment;

import com.example.journey.fragments.prompts.CameraAndGalleryPromptFragment;
import com.example.journey.fragments.prompts.SliderPromptFragment;
import com.example.journey.fragments.prompts.AddStringsPromptFragment;
import com.example.journey.fragments.prompts.PlacesPromptFragment;
import com.example.journey.models.Prompt;

import timber.log.Timber;

public class DBQueryMapper {

    public static Fragment getFragmentForPrompt(Prompt prompt) {

       switch (prompt.getPromptId()) {
           case Prompt.CAMERA_AND_GALLERY:
               return CameraAndGalleryPromptFragment.newInstance(prompt);
           case Prompt.TRAVEL:
               return PlacesPromptFragment.newInstance(prompt);
           case Prompt.PROUD:
               return AddStringsPromptFragment.newInstance(prompt);
           case Prompt.MOOD:
               return SliderPromptFragment.newInstance(prompt);
           case Prompt.ANYTHING:
               return AddStringsPromptFragment.newInstance(prompt);
           default:
               Timber.e("No Fragment Found for that prompt ID");
               return null;
       }
    }

}
