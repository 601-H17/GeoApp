package com.example.julien.geoapp.services;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien on 2017-01-30.
 */

public class DrawGeoJsonMapsService {


    private MapboxMap mapboxMap;
    private String request;

    public DrawGeoJsonMapsService(MapboxMap mapboxMap, String request) {
        this.mapboxMap = mapboxMap;
        this.request = request;
    }

    public void drawMaps() {

        ArrayList<LatLng> pointsLine = new ArrayList<>();

        try {
                JSONObject json = new JSONObject(request);
                JSONArray features = json.getJSONArray("features");
            int numberFeatures = 0;
                while (numberFeatures <= features.length()) {
                    JSONObject feature = features.getJSONObject(numberFeatures);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    if (geometry != null) {
                        String type = geometry.getString("type");
                        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {
                            JSONArray coords = geometry.getJSONArray("coordinates");
                            for (int lc = 0; lc < coords.length(); lc++) {
                                JSONArray coord = coords.getJSONArray(lc);
                                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                                pointsLine.add(latLng);
                            }
                        }
                    }
                    numberFeatures++;
                    drawLines(pointsLine);
                    pointsLine = new ArrayList<>();
                }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
    }

    private void drawLines(List<LatLng> pointsLine) {
                if (pointsLine.size() > 0) {
                    mapboxMap.addPolyline(new PolylineOptions()
                            .addAll(pointsLine)
                            .color(Color.parseColor("#ffffff"))
                            .width(2));
        }
    }
}