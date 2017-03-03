package com.example.julien.geoapp.services.pathService;

import android.graphics.Color;
import android.text.TextUtils;
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
    private ArrayList<ArrayList<LatLng>> stepsPath;

    public DrawGeoJsonPathService(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void drawPath(String pathString) {

        ArrayList<LatLng> pointDoors = new ArrayList<>();
        try {
//            JSONObject json = new JSONObject(pathString);
//            JSONArray features = json.getJSONArray(Message.FEATURES_JSON[9]);
//
//            for (int fn = 0; fn <= features.length(); fn++) {
//                JSONObject feature = features.getJSONObject(fn);
//                JSONObject geometry = feature.getJSONObject(Message.FEATURES_JSON[1]);
//            }

            JSONObject json = new JSONObject(pathString);
            for (int b = 0; b < json.length(); b++) {
                Object pathStepObj = json.get(Message.FEATURES_JSON[9]);
                JSONArray path = (JSONArray) pathStepObj;
                for (int fn = 0; fn <= path.length(); fn++) {
                    JSONArray coord = path.getJSONArray(fn);
                    LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                    pointDoors.add(latLng);
                }
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