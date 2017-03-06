package com.example.julien.geoapp.services.doorsService;

import com.mapbox.mapboxsdk.annotations.Marker;

/**
 * Created by Julien on 2017-02-13.
 */

public interface IDrawGeoJsonDoorsService {
    void drawDoors();
    void hideDoors();
    void addFromToMarkers(String pathGeoJson);
    String getLocalName(Marker localToFind);
}