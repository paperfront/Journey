package com.example.journey.models;

import java.util.ArrayList;
import java.util.List;

public enum Track {
    SELF_HELP("Self Help", "self help", 0),
    GENERAL("General", "general", 1),
    ;

    private String title;
    private String key;
    private int trackId;

    Track(String title, String key, int trackId) {
        this.title = title;
        this.trackId = trackId;
        this.key = key;
    }


    public int getTrackId() {
        return trackId;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public String getKey() {
        return key;
    }

    public static List<String> getAllTracks() {
        List<String> titles = new ArrayList<>();
        for (Track track : values()) {
            titles.add(track.toString());
        }
        return titles;
    }

}
