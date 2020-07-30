package com.example.journey.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.example.journey.helpers.DateCreatedFormatter;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

;import timber.log.Timber;

public class Entry implements Parcelable, Comparable<Entry> {
    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    private Date dateCreated;
    private List<Prompt> prompts;
    private boolean favorite;

    public Entry() {
    }

    public Entry(List<Prompt> prompts) {
        this.favorite = false;
        this.dateCreated = Calendar.getInstance().getTime();
        this.prompts = new ArrayList<>();
        for (Prompt prompt : prompts) {
            if (prompt.isCompleted()) {
                this.prompts.add(prompt);
            }
        }

    }

    protected Entry(Parcel in) {
        favorite = in.readBoolean();
        dateCreated = (Date) in.readValue(Date.class.getClassLoader());
        prompts = in.readArrayList(Prompt.class.getClassLoader());
        if (prompts == null) {
            prompts = new ArrayList<>();
        }
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    @Exclude
    public String getDateCreatedString() {
        return DateCreatedFormatter.formatDate(getDateCreated());
    }

    public List<Prompt> getPrompts() {
        return prompts;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBoolean(favorite);
        parcel.writeValue(dateCreated);
        parcel.writeList(prompts);
    }

    @Exclude
    public List<String> getAllStringResponses() {
        List<String> responses = new ArrayList<>();
        for (Prompt prompt : prompts) {
            if (!prompt.getStringResponse().isEmpty() && prompt.isTextPrompt()) {
                responses.addAll(prompt.getStringResponse());
            }
        }
        return responses;
    }

    @Exclude
    public int getMood() {
        for (Prompt prompt : prompts) {
            if (prompt.getPromptId() == Prompt.MOOD) {
                return (int) Float.parseFloat(prompt.getStringResponse().get(0));
            }
        }
        Timber.e("Failed to find mood prompt");
        return 0;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public int compareTo(Entry o) {
        return getDateCreated().compareTo(o.getDateCreated());
    }

}
