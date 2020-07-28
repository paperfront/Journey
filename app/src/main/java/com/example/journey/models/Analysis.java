package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.journey.helpers.DateCreatedFormatter;
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
    public static final String SETTING_IMPORTANT_ENTRIES = "important entries";
    public static final String SETTING_MOOD_GRAPH = "mood graph";

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


    /**
     * Algorithm to select 1 entry as the most important. First considers the number
     * of prompts answered in each entry, and chooses from those that are tied for the most
     * prompts answered. If no tie exists, the entry with the most prompts answered is returned.
     * If a tie exists, the tie is broken by first considering the amount of items added
     * to each prompt. If a tie still exists, an entry will be chosen at random.
     *
     * Currently, any ties in the longest entry are resolved randomly. Expanded functionality
     * as described above is a todo feature.
     */
    @Exclude
    public Entry getImportantEntry() {
        Entry longestEntry = entries.get(0);
        for (int i = 1; i < entries.size(); i++) {
            if (entries.get(i).getPrompts().size() > longestEntry.getPrompts().size()) {
                longestEntry = entries.get(i);
            }
        }
        return longestEntry;
    }

    @Exclude
    public String getDateCreatedString() {
        return DateCreatedFormatter.formatDate(getDateCreated());
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
