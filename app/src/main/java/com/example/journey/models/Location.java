package com.example.journey.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.GeoPoint;

public class Location implements Parcelable {

    private String name;
    private GeoPoint point;

    public Location() {}

    public Location(Place place) {
        this.name = place.getName();
        this.point = new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    protected Location(Parcel in) {
        name = in.readString();
        point = new GeoPoint(in.readDouble(), in.readDouble());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getName() {
        return name;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public double getLatitude() {return point.getLatitude();}

    public double getLongitude() {return point.getLongitude();}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(point.getLatitude());
        parcel.writeDouble(point.getLongitude());
    }
}
