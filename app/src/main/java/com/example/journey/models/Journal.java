package com.example.journey.models;

import java.util.ArrayList;
import java.util.List;

public class Journal {

    private String title;
    private List<Entry> entries;

    public Journal(){}

    public Journal(String title) {
        this.title = title;
        this.entries = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Entry> getEntries() {
        return entries;
    }


}
