package com.example.yuen.model;

import java.util.ArrayList;

/**
 * Created by Yuen on 9/1/2016.
 */
public class Case_history {
    private String title, thumbnailUrl;
    private int year;
    private double rating;
    private ArrayList<String> genre;

    public Case_history() {
    }

    public Case_history(String name, String thumbnailUrl, int year, double rating,
                 ArrayList<String> genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
