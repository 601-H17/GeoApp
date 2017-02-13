package com.example.julien.geoapp.services.MapsService;

import android.graphics.Color;
import android.text.TextUtils;
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

public class DrawGeoJsonMapsService implements IDrawGeoJsonMapsService {


    private MapboxMap mapboxMap;
    private String request;
    private String featuresJson[] = {"features", "geometry", "type", "Point", "coordinates", "properties", "ref", "entrance", "Disponible: ", "LineString"};
    private String error[] = {"TAG", "Exception Loading GeoJSON "};
    private String[] color = {"#ffffff"};

    public DrawGeoJsonMapsService(MapboxMap mapboxMap, String request) {
        this.mapboxMap = mapboxMap;
        this.request = request;
    }

    public void drawMaps() {

        ArrayList<LatLng> pointsLine = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(request);
            JSONArray features = json.getJSONArray(featuresJson[0]);
            for (int fn = 0; fn <= features.length(); fn++) {
                JSONObject feature = features.getJSONObject(fn);
                JSONObject geometry = feature.getJSONObject(featuresJson[1]);
                if (geometry != null) {
                    String type = geometry.getString(featuresJson[2]);
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(featuresJson[9])) {
                        JSONArray coords = geometry.getJSONArray(featuresJson[4]);
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            pointsLine.add(latLng);
                        }
                    }
                }
                drawLines(pointsLine);
                pointsLine = new ArrayList<>();
            }
        } catch (Exception exception) {
            Log.e(error[0], error[1] + exception.toString());
        }
    }

    private void drawLines(List<LatLng> pointsLine) {
        if (pointsLine.size() > 0) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(pointsLine)
                    .color(Color.parseColor(color[0]))
                    .width(2));
        }
    }
}