package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Analysis implements Parcelable {

    private String title;
    private Date dateCreated;
    private List<Entry> entries;
    private HashMap<String, Boolean> data;
    private HashMap<String, Integer> wordCounts;

    public static final String SETTING_MAPS = "maps";
    public static final String SETTING_WORD_CLOUD = "word cloud";

    public Analysis(){}

    public Analysis(String title, List<Entry> entries) {
        this.title = title;
        this.dateCreated = Calendar.getInstance().getTime();
        this.entries = entries;
        this.data = new HashMap<>();
    }


    protected Analysis(Parcel in) {
        title = in.readString();
        dateCreated =  (Date) in.readValue(Date.class.getClassLoader());
        data = in.readHashMap(String.class.getClassLoader());
        wordCounts = in.readHashMap(String.class.getClassLoader());
        entries = in.readArrayList(Entry.class.getClassLoader());
    }


    public static final Creator<Analysis> CREATOR = new Creator<Analysis>() {
        @Override
        public Analysis createFromParcel(Parcel in) {
            return new Analysis(in);
        }

        @Override
        public Analysis[] newArray(int size) {
            return new Analysis[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeValue(dateCreated);
        parcel.writeMap(data);
        parcel.writeMap(wordCounts);
        parcel.writeList(entries);
    }

    public String getTitle() {
        return title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public HashMap<String, Boolean> getData() {
        return data;
    }

    public HashMap<String, Integer> getWordCounts() {
        return wordCounts;
    }

    @Exclude
    public HashMap<Location, Integer> getLocations() {
        HashMap<Location, Integer> locations = new HashMap<>();
        for (Entry entry : entries) {
            List<Prompt> prompts = entry.getPrompts();
            for (Prompt prompt : prompts) {
                if (prompt.getLocationResponse() != null) {
                    for (Location location : prompt.getLocationResponse()) {
                        if (locations.containsKey(location)) {
                            locations.put(location, locations.get(location) + 1);
                        } else {
                            locations.put(location, 1);
                        }
                    }
                }
            }
        }
        return locations;
    }

    @Exclude
    public void addData(String setting) {
        data.put(setting, true);
    }

    @Exclude
    public boolean isSettingEnabled(String setting) {
        return data.containsKey(setting);
    }


    public void setWordCounts(HashMap<String, Integer> wordCounts) {
        this.wordCounts = wordCounts;
    }
}
