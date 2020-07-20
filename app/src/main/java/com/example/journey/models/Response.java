package com.example.journey.models;

import android.os.Parcelable;

public class Response<T extends Parcelable> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
