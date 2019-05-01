package com.example.wanderdots;

public class Dot {
    private int id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private int photo;

    public Dot(int id, String title, String description, double latitude, double longitude, int photo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getPhoto() {
        return photo;
    }
}
