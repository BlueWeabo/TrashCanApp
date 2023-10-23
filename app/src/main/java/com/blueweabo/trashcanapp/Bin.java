package com.blueweabo.trashcanapp;

import org.jetbrains.annotations.NotNull;

public class Bin {

    private float percentage = 0;
    @NotNull
    private GPSLocation location = new GPSLocation();

    public Bin() {}

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }
    @NotNull
    public GPSLocation getLocation() {
        return location;
    }

    public static class GPSLocation {
        private double latitude = 0;
        private double longitude = 0;

        public GPSLocation() {}
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
