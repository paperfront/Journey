package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Journal implements Parcelable {

    private String title;
    private List<Entry> entries;
    private int colorId;

    public Journal(){}

    public Journal(String title, int colorId) {
        this.title = title;
        this.colorId = colorId;
        this.entries = new ArrayList<>();
    }

    protected Journal(Parcel in) {
        title = in.readString();
        entries = in.readArrayList(Entry.class.getClassLoader());
        colorId = in.readInt();
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
    }

    public int getColorId() {
        return colorId;
    }
}
