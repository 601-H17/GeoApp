package com.example.julien.geoapp.services.DoorsService;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by Julien on 2017-02-13.
 */

public interface IDrawGeoJsonDoorsService {
    void drawDoors();
    void hideDoors();
    String[] getDoorsListTitle();
}
