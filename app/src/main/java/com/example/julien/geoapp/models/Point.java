package com.example.julien.geoapp.models;

/**
 * Created by Julien on 2017-02-09.
 */

public abstract class Point {

    private String title;
    private String description;
    private double lati;
    private double longi;

    public Point(String Title, String Description,double lati,double longi) {

        this.title = Title;
        this.description = Description;
        this.longi = longi;
        this.lati = lati;
    }

    public double getLati() {
        return lati;
    }

    public double getlongi() {
        return longi;
    }


    public String getDescription() {
        return description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}