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

       switch (prompt.getType()) {
           case Prompt.TYPE_MEDIA_RESPONSE:
               return CameraAndGalleryPromptFragment.newInstance(prompt);
           case Prompt.TYPE_LOCATION_RESPONSE:
               return PlacesPromptFragment.newInstance(prompt);
           case Prompt.TYPE_STRING_RESPONSE:
               return AddStringsPromptFragment.newInstance(prompt);
           case Prompt.TYPE_SLIDER_RESPONSE:
               return SliderPromptFragment.newInstance(prompt);
           default:
               Timber.e("No Fragment Found for that prompt ID");
               return null;
       }
    }

}
