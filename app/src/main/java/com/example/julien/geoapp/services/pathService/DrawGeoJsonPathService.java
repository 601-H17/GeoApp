package com.example.julien.geoapp.services.pathService;

import android.graphics.Color;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien on 2017-01-30.
 */

public class DrawGeoJsonPathService implements  IDrawGeoJsonPathService {


    private MapboxMap mapboxMap;

    public DrawGeoJsonPathService(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void drawPath(String pathString) {

        ArrayList<LatLng> pointDoors = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(pathString);
            JSONArray path = json.getJSONArray("path");
            for (int i = 0; i < path.length(); i++) {
                JSONArray coord = path.getJSONArray(i);
                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                pointDoors.add(latLng);
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
        drawLinesCorridors(pointDoors);
    }
    private void drawLinesCorridors(List<LatLng> pointDoors) {
        if (pointDoors.size() > 0) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(pointDoors)
                    .color(Color.parseColor("#cb2c39"))
                    .width(2));
        }
    }
}