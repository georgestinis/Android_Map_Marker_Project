package com.example.app2;

public class Markers {
    private double latitude;
    private double longitude;
    private String sensor_type;
    private float sensor_value;
    private float color;
    private String description;

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

    public String getSensor_type() {
        return sensor_type;
    }

    public void setSensor_type(String sensor_type) {
        this.sensor_type = sensor_type;
    }

    public float getSensor_value() {
        return sensor_value;
    }

    public void setSensor_value(float sensor_value) {
        this.sensor_value = sensor_value;
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
