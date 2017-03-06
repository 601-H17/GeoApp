package com.example.julien.geoapp.models;

import android.graphics.Paint;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-03-06.
 */

public class Path {
    private ArrayList<LatLng> path;
    private int floor;
    private int indexStep;

    public Path(ArrayList<LatLng> path, int floor, int indexStep){

        this.path = path;
        this.floor = floor;
        this.indexStep = indexStep;
    }

    public ArrayList<LatLng> getPath() {
        return path;
    }

    public int getFloor() {
        return floor;
    }

    public int getIndexStep() {
        return indexStep;
    }
}
