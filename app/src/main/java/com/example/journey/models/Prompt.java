package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.journey.fragments.prompts.CameraAndGalleryFragment;
import com.example.journey.fragments.responses.CameraAndGalleryResponseFragment;

public enum Prompt {
    PICTURE("Do you have any pictures to share from today?", CameraAndGalleryFragment.newInstance(), Track.GENERAL) {

        @Override
        public void createResponseFragment() {
            Bitmap image = ((BitmapDrawable) ((ImageView) getResponse()).getDrawable()).getBitmap();
            setResponseFragment(CameraAndGalleryResponseFragment.newInstance(image));
        }
    },
    //TRAVEL("Where did you travel today?", CameraAndGalleryFragment.newInstance(), Track.GENERAL),
    ;

    private String question;
    private Fragment promptFragment;
    private Fragment responseFragment;
    private Object response = null;
    private Track track;
    private boolean completed = false;


    Prompt(String question, Fragment fragment, Track track) {
        this.question = question;
        this.promptFragment = fragment;
        this.track = track;
    }

    public String getQuestion() {
        return question;
    }

    public Fragment getPromptFragment() {
        return promptFragment;
    }

    public Track getTrack() {
        return track;
    }

    public void setResponseFragment(Fragment responseFragment) {
        this.responseFragment = responseFragment;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public abstract void createResponseFragment();


}
