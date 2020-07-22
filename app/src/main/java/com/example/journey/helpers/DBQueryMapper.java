package com.example.journey.helpers;

import androidx.fragment.app.Fragment;

import com.example.journey.fragments.prompts.CameraAndGalleryPromptFragment;
import com.example.journey.fragments.prompts.MoodPromptFragment;
import com.example.journey.fragments.prompts.AddStringsPromptFragment;
import com.example.journey.fragments.prompts.TravelPromptFragment;
import com.example.journey.models.Prompt;

import timber.log.Timber;

public class DBQueryMapper {

    public static Fragment getFragmentForPrompt(Prompt prompt) {

       switch (prompt.getPromptId()) {
           case Prompt.CAMERA_AND_GALLERY:
               return CameraAndGalleryPromptFragment.newInstance(prompt);
           case Prompt.TRAVEL:
               return TravelPromptFragment.newInstance(prompt);
           case Prompt.PROUD:
               return AddStringsPromptFragment.newInstance(prompt);
           case Prompt.MOOD:
               return MoodPromptFragment.newInstance(prompt);
           default:
               Timber.e("No Fragment Found for that prompt ID");
               return null;
       }
    }

}
