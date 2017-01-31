package com.example.julien.geoapp;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Julien on 2017-01-30.
 */

public class Point {

    private String title;
    private String description;

    public Point(String Title, String Description){

        this.title = Title;
        this.description = Description;
    }
    public Point(){
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
