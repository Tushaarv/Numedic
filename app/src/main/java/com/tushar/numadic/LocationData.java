package com.tushar.numadic;

/**
 * Created by Tushar Vengurlekar
 * Created on 01/07/17.
 */

public class LocationData {

    private double latitude;
    private double longitude;

    public LocationData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationData getLocation() {

        return new LocationData(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
