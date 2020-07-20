package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.journey.fragments.responses.CameraAndGalleryResponseFragment;
import com.example.journey.helpers.DBQueryMapper;

public class Prompt {
    //TRAVEL("Where did you travel today?", CameraAndGalleryFragment.newInstance(), Track.GENERAL),

    public static final int CAMERA_AND_GALLERY = 0;
    public static final int TRAVEL = 1;
    public static final int PROUD = 2;





    private String question;
    private Fragment promptFragment;
    private Fragment responseFragment;
    private Object response = null;
    private int promptId;
    private boolean completed = false;


    public Prompt(){}

    public Prompt(String question, Fragment fragment) {
        this.question = question;
        this.promptFragment = fragment;
    }

    public String getQuestion() {
        return question;
    }

    public Fragment getPromptFragment() {
        return DBQueryMapper.getFragmentForPrompt(this);
    }


    public Fragment getResponseFragment() {
        return responseFragment;
    }

    public void setResponseFragment(Fragment responseFragment) {
        this.responseFragment = responseFragment;
    }

    public void setResponse(Object response) {
        completed = true;
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public boolean hasBeenCompleted() {
        return completed;
    }

    public int getPromptId() {
        return promptId;
    }

    public void createResponseFragment() {
        if (!hasBeenCompleted()) {
            return;
        }

        Bitmap image = ((BitmapDrawable) ((ImageView) getResponse()).getDrawable()).getBitmap();
        setResponseFragment(CameraAndGalleryResponseFragment.newInstance(image));
    }


}
