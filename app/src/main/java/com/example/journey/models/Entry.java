package com.example.journey.models;

import android.media.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;;
import java.util.Set;

public class Entry {
    private Date dateCreated;
    private List<Prompt> prompts;

    public Entry() {}

    public Entry(List<Prompt> prompts) {
        this.dateCreated = Calendar.getInstance().getTime();
        this.prompts = new ArrayList<>();
        for (Prompt prompt : prompts) {
            if (prompt.isCompleted()) {
                this.prompts.add(prompt);
            }
        }

    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public List<Prompt> getPrompts() {
        return prompts;
    }


}
