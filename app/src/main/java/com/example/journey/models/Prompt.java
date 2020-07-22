package com.example.journey.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.journey.helpers.DBQueryMapper;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Prompt implements Parcelable {
    //TRAVEL("Where did you travel today?", CameraAndGalleryFragment.newInstance(), Track.GENERAL),

    public static final int CAMERA_AND_GALLERY = 0;
    public static final int TRAVEL = 1;
    public static final int PROUD = 2;


    private String question;
    private int promptId;
    private boolean completed = false;
    private List<String> stringResponse = new ArrayList<>();
    private List<Location> locationResponse = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(question);
        out.writeInt(promptId);
        out.writeBoolean(completed);
        out.writeList(stringResponse);
        out.writeList(locationResponse);
    }

    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    // But this is out of scope of this tutorial
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    @Exclude
    public static final Parcelable.Creator<Prompt> CREATOR
            = new Parcelable.Creator<Prompt>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Prompt createFromParcel(Parcel in) {
            return new Prompt(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Prompt[] newArray(int size) {
            return new Prompt[size];
        }
    };

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Prompt(Parcel in) {
        question = in.readString();
        promptId = in.readInt();
        completed = in.readBoolean();
        stringResponse = in.readArrayList(String.class.getClassLoader());
        locationResponse = in.readArrayList(Location.class.getClassLoader());
    }


    public Prompt(){}

    public Prompt(String question) {
        this.question = question;
    }

    @Exclude
    public Fragment getPromptFragment() {
        return DBQueryMapper.getFragmentForPrompt(this);
    }


    public void setStringResponse(List<String> response) {
        completed = true;
        this.stringResponse = response;
    }

    public void setLocationResponse(List<Location> response) {
        completed = true;
        this.locationResponse = response;
    }

    public String getQuestion() {
        return question;
    }

    public int getPromptId() {
        return promptId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<String> getStringResponse() {
        return stringResponse;
    }

    public List<Location> getLocationResponse() {
        return locationResponse;
    }
}
