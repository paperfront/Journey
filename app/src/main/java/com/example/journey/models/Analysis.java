package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Analysis implements Parcelable {

    private String title;
    private Date dateCreated;
    private List<Entry> entries;
    private HashMap<String, String> data;
    private HashMap<String, Integer> wordCounts;

    public Analysis(){}

    public Analysis(String title, List<Entry> entries) {
        this.title = title;
        this.dateCreated = Calendar.getInstance().getTime();
        this.entries = entries;
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

    public HashMap<String, String> getData() {
        return data;
    }

    public HashMap<String, Integer> getWordCounts() {
        return wordCounts;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public void setWordCounts(HashMap<String, Integer> wordCounts) {
        this.wordCounts = wordCounts;
    }
}
