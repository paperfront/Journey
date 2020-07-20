package com.example.journey.models;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.journey.fragments.responses.CameraAndGalleryResponseFragment;
import com.example.journey.helpers.DBQueryMapper;
import com.google.protobuf.Any;

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
    private List<Parcelable> response = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(question);
        out.writeInt(promptId);
        out.writeBoolean(completed);
        out.writeList(response);
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
        in.readList(response, DBQueryMapper.getResponseClassForPromptId(promptId).getClassLoader());
    }


    public Prompt(){}

    public Prompt(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public Fragment getPromptFragment() {
        return DBQueryMapper.getFragmentForPrompt(this);
    }

    public Fragment getResponseFragment() {
        return DBQueryMapper.getResponseFragmentForPrompt(this);
    }

    public <T extends Parcelable> void setResponse(List<T> response) {
        completed = true;
        this.response = (List<Parcelable>) response;
    }

    public List<Parcelable> getResponse() {
        return response;
    }

    public boolean hasBeenCompleted() {
        return completed;
    }

    public int getPromptId() {
        return promptId;
    }


}
