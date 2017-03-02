package com.example.julien.geoapp.services.pathService;

import android.graphics.Color;
import android.util.Log;

import com.example.julien.geoapp.Externalization.Message;
import com.mapbox.mapboxsdk.annotations.Annotation;
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

public class DrawGeoJsonPathService implements IDrawGeoJsonPathService {


    private MapboxMap mapboxMap;

    public DrawGeoJsonPathService(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void drawPath(String pathString) {

        ArrayList<LatLng> pointDoors = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(pathString);
            JSONArray path = json.getJSONArray(Message.FEATURES_JSON[9]);
            for (int i = 0; i < path.length(); i++) {
                JSONArray coord = path.getJSONArray(i);
                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                pointDoors.add(latLng);
            }
        } catch (Exception exception) {
            Log.e(Message.ERROR[0], exception.toString());
        }
        drawLinesCorridors(pointDoors);
    }


    private void drawLinesCorridors(List<LatLng> pointDoors) {
        if (pointDoors.size() > 0) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(pointDoors)
                    .color(Color.parseColor(Message.COLOR_PATH))
                    .width(2));
        }
    }
}