package com.example.journey;

import android.app.Application;

import com.google.android.libraries.places.api.Places;

import java.util.Locale;

import timber.log.Timber;

public class ApplicationController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_api_key), Locale.US);
        }
    }
}

