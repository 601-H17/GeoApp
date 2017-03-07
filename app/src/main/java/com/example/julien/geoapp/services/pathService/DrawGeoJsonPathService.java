package com.example.julien.geoapp.services.pathService;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.models.Path;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by Julien on 2017-01-30.
 */

public class DrawGeoJsonPathService implements IDrawGeoJsonPathService {


    private MapboxMap mapboxMap;
    private ArrayList<Path> stepsPath;
    private Context context;
    private int steps = 0;

    public DrawGeoJsonPathService(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        this.context = context;
    }

    public void drawPath(String drawPath) {
        this.stepsPath = new ArrayList<>();
        steps = 0;
        try {
            JSONObject fullPath = new JSONObject(drawPath);
            JSONArray json = fullPath.getJSONArray("fullPath");
            for (int b = 0; b < json.length(); b++) {
                JSONObject path = json.getJSONObject(b);
                if (path.getJSONArray(Message.FEATURES_JSON[9]) != null) {
                    ArrayList<LatLng> tempPath = new ArrayList<>();
                    int floor = path.getInt(Message.FEATURES_JSON[11]);
                    JSONArray pathFound = path.getJSONArray(Message.FEATURES_JSON[9]);
                    for (int fn = 0; fn < pathFound.length(); fn++) {
                        JSONArray coord = pathFound.getJSONArray(fn);
                        LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                        tempPath.add(latLng);
                    }
                    stepsPath.add(new Path(tempPath, floor, b));
                }
            }
        } catch (Exception exception) {
            Log.e(Message.ERROR[0], exception.toString());
        }
    }

    public void drawLinesCorridorsStep() {
        try {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(stepsPath.get(steps).getPath())
                    .color(Color.parseColor(Message.COLOR_PATH))
                    .width(2));
            steps++;
        } catch (Exception e) {
            Log.d(Message.ERROR[0], e.toString());
        }
    }

    public int getFloor() {
        return stepsPath.get(steps).getFloor();
    }

    public boolean isLastStep() {
        boolean last = false;
        if (stepsPath.size() == steps)
            last = true;
        return last;
    }

    public int getTotalStep() {
        return stepsPath.size();
    }
}