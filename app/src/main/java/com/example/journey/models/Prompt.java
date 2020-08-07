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
    public static final int MOOD = 3;
    public static final int ANYTHING = 4;


    public static final int TYPE_STRING_RESPONSE = 100;
    public static final int TYPE_LOCATION_RESPONSE = 101;
    public static final int TYPE_SLIDER_RESPONSE = 102;
    public static final int TYPE_MEDIA_RESPONSE = 103;


    private String title;
    private String question;
    private String promptHeader;
    private int promptId;
    private int type;
    private boolean completed = false;
    private boolean enabled = true;
    private List<String> stringResponse = new ArrayList<>();
    private List<Location> locationResponse = new ArrayList<>();

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(question);
        out.writeString(promptHeader);
        out.writeInt(promptId);
        out.writeInt(type);
        out.writeBoolean(completed);
        out.writeBoolean(enabled);
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

    private Prompt(Parcel in) {
        title = in.readString();
        question = in.readString();
        promptHeader = in.readString();
        promptId = in.readInt();
        type = in.readInt();
        completed = in.readBoolean();
        enabled = in.readBoolean();
        stringResponse = in.readArrayList(String.class.getClassLoader());
        locationResponse = in.readArrayList(Location.class.getClassLoader());
    }


    public Prompt(){}

    public Prompt(String question) {
        this.question = question;
    }

    public Prompt(String title, String question, int type) {
        this.title = title;
        this.question = question;
        this.type = type;
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

    public String getPromptHeader() {
        return promptHeader;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Exclude
    public boolean isTextPrompt() {
        return promptId == 2 || promptId == 4;
    }
}
