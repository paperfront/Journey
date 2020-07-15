package com.example.journey.models;

public class User {

    private String email;
    private String userId;

    public User() {

    }

    public User(String email, String userId) {
        this.email = email;
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }
}
