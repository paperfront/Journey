package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.journey.helpers.FirestoreClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Journal implements Parcelable {

    private String title;
    private List<Entry> entries;
    private List<Prompt> prompts;
    private int colorId;

    public Journal(){}

    public Journal(String title, int colorId) {
        this.title = title;
        this.colorId = colorId;
        this.entries = new ArrayList<>();
        this.prompts = new ArrayList<>();
    }

    protected Journal(Parcel in) {
        title = in.readString();
        entries = in.readArrayList(Entry.class.getClassLoader());
        colorId = in.readInt();
        prompts = in.readArrayList(Prompt.class.getClassLoader());
    }

    public static final Creator<Journal> CREATOR = new Creator<Journal>() {
        @Override
        public Journal createFromParcel(Parcel in) {
            return new Journal(in);
        }

        @Override
        public Journal[] newArray(int size) {
            return new Journal[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public List<Entry> getEntries() {
        return entries;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeList(entries);
        parcel.writeInt(colorId);
        parcel.writeList(prompts);
    }

    public int getColorId() {
        return colorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public List<Prompt> getPrompts() {
        return prompts;
    }

    public void setPrompts(List<Prompt> prompts) {
        this.prompts = prompts;
    }
}
