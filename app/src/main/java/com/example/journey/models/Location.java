package com.example.journey.models;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.GeoPoint;

public class Location {

    private String name;
    private GeoPoint point;

    public Location() {}

    public Location(Place place) {
        this.name = place.getName();
        this.point = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    public String getName() {
        return name;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public double getLatitude() {return point.getLatitude();}

    public double getLongitude() {return point.getLongitude();}
}
