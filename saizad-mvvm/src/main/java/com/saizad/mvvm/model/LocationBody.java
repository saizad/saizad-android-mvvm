package com.saizad.mvvm.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationBody {

    @NonNull
    @Expose
    @SerializedName("lat")
    private final Double lat;

    @NonNull
    @Expose
    @SerializedName("lng")
    private final Double lng;

    public LocationBody(@NonNull String lat, @NonNull String lng) {
        this(Double.valueOf(lat), Double.valueOf(lng));
    }
    public LocationBody(@NonNull Double lat, @NonNull Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}