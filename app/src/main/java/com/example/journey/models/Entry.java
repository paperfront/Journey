package com.example.journey.models;

import android.media.Image;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;;
import java.util.Set;

public class Entry {
    private Date dateCreated;
    private String title;
    private List<Prompt> prompts;
    private File image;

    // todo update with google maps class
    // private List<Geotags> locationsVisited;

    public List<Prompt> getPrompts() {
        return prompts;
    }


}
