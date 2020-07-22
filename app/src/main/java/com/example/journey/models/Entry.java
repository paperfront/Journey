package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

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

    protected Entry(Parcel in) {
        dateCreated = (Date) in.readValue(Date.class.getClassLoader());
        in.readTypedList(prompts, Prompt.CREATOR);
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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(dateCreated);
        parcel.writeTypedList(prompts);
    }
}
