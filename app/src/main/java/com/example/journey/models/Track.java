package com.example.journey.models;

import java.util.ArrayList;
import java.util.List;

public enum Track {
    SELF_HELP("Self Help"),
    ;

    private String title;

    Track(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public static List<String> getAllTracks() {
        List<String> titles = new ArrayList<>();
        for (Track track : values()) {
            titles.add(track.toString());
        }
        return titles;
    }

}
