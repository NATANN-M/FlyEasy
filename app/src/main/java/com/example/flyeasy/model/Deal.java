package com.example.flyeasy.model;

public class Deal {
    private String title;
    private int imageResId;

    public Deal(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}

