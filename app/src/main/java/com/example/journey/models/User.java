package com.example.journey.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String userId;
    private List<String> journalNames;

    public User() {

    }

    public User(String email, String userId) {
        this.email = email;
        this.userId = userId;
        this.journalNames = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getJournalNames() {
        return journalNames;
    }
}
