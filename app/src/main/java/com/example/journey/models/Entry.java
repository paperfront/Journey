package com.example.journey.models;

import android.media.Image;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Entry {
    private Date dateCreated;
    private String title;
    private HashMap<String, String> promptsAndAnswers;
    private File image;

    // todo update with google maps class
    // private List<Geotags> locationsVisited;

    public Set<String> getPrompts() {
        return promptsAndAnswers.keySet();
    }

    public Set<String> getResponses() {
        return ((Set<String>) promptsAndAnswers.values());
    }

}
