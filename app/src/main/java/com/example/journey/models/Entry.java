package com.example.journey.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

;

public class Entry implements Parcelable {
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

    public Entry() {
    }

    public Entry(List<Prompt> prompts) {
        this.dateCreated = Calendar.getInstance().getTime();
        this.prompts = new ArrayList<>();
        for (Prompt prompt : prompts) {
            if (prompt.isCompleted()) {
                this.prompts.add(prompt);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Entry(Parcel in) {
        dateCreated = (Date) in.readValue(Date.class.getClassLoader());
        prompts = in.readArrayList(Prompt.class.getClassLoader());
        if (prompts == null) {
            prompts = new ArrayList<>();
        }
    }

    public Date getDateCreated() {
        return dateCreated;
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
        parcel.writeValue(dateCreated);
        parcel.writeList(prompts);
    }
}
