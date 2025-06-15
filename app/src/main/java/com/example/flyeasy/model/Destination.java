package com.example.flyeasy.model;

public class Destination {
    private String name;
    private int imageResId;

    public Destination(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
