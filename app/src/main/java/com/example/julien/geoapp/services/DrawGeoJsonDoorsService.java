package com.example.julien.geoapp.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.julien.geoapp.models.Point;
import com.example.julien.geoapp.R;
import com.google.common.collect.MapMaker;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien on 2017-01-30.
 */

public class DrawGeoJsonDoorsService {


    private MapboxMap mapboxMap;
    private Context context;
    private String request;
    private ArrayList<LatLng> pointDoors;
    private ArrayList<Point> doorsInformation;
    private ArrayList<MarkerViewOptions> markers;

    public DrawGeoJsonDoorsService(MapboxMap mapboxMap, Context context, String geojson) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        this.request = geojson;
        pointDoors = new ArrayList<>();
        doorsInformation = new ArrayList<>();
        markers = new ArrayList<>();
        saveDoors();
    }

    private void saveDoors() {
        Point descriptionTitle = new Point();
        try {
            JSONObject json = new JSONObject(request);
            JSONArray features = json.getJSONArray("features");
            for (int fn = 0; fn <= features.length(); fn++) {
                JSONObject feature = features.getJSONObject(fn);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Point")) {
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        LatLng latLng = new LatLng(coords.getDouble(1), coords.getDouble(0));
                        pointDoors.add(latLng);
                        try {
                            JSONObject description = feature.getJSONObject("properties");
                            descriptionTitle = new Point(description.getString("ref"), "DISPONIBLE: " + description.getString("entrance"));
                            doorsInformation.add(descriptionTitle);
                        } catch (Exception exception) {
                            Log.e("TAG", "Exception Loading GeoJSON");
                        }
                    }
                }
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
        createMarkers();
    }

    private void createMarkers() {
        IconFactory iconFactory = IconFactory.getInstance(context);
        Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.pin);
        Icon icon = iconFactory.fromDrawable(iconDrawable);

        if (pointDoors.size() > 0) {
            for (int i = 0; i < pointDoors.size(); i++) {
                MarkerViewOptions mark = (new MarkerViewOptions()
                        .position(pointDoors.get(i))
                        .title(doorsInformation.get(i).getTitle())
                        .snippet(doorsInformation.get(i).getDescription())
                        .icon(icon)
                        .visible(false)
                );
                markers.add(mark);
            }
        }
    }

    public void drawDoors() {
        for (int i = 0; i < markers.size(); i++) {
            mapboxMap.addMarker(markers.get(i).visible(true));
        }
    }

    public LatLng getDoorsLocation(String name) {
        LatLng error = new LatLng(0, 0);
        for (int i = 0; i < doorsInformation.size(); i++) {
            if (doorsInformation.get(i).getTitle().equals(name)) {
                return pointDoors.get(i);
            }
        }
        return error;
    }

    public String[] getDoorsListTitle() {
        String[] list = {"error API"};
        try {
            list = new String[doorsInformation.size()];
            for (int i = 0; i < doorsInformation.size(); i++) {
                list[i] = doorsInformation.get(i).getTitle();
            }
            return list;
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
        return list;
    }

}