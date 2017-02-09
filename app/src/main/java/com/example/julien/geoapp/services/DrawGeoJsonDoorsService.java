package com.example.julien.geoapp.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.models.DoorsInformation;
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
    private ArrayList<DoorsInformation> doorsInformation;
    private ArrayList<MarkerViewOptions> markers;
    private String featuresJson[] = {"features", "geometry", "type", "Point", "coordinates", "properties", "ref", "entrance", "Disponible: "};
    private String error[] = {"TAG", "Exception Loading GeoJSON "};

    public DrawGeoJsonDoorsService(MapboxMap mapboxMap, Context context, String geojson) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        this.request = geojson;
        doorsInformation = new ArrayList<>();
        markers = new ArrayList<>();
        saveDoors();
    }

    private void saveDoors() {
        try {
            JSONObject json = new JSONObject(request);
            JSONArray features = json.getJSONArray(featuresJson[0]);
            for (int fn = 0; fn <= features.length(); fn++) {
                JSONObject feature = features.getJSONObject(fn);
                JSONObject geometry = feature.getJSONObject(featuresJson[1]);
                if (geometry != null) {
                    String type = geometry.getString(featuresJson[2]);
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(featuresJson[3])) {
                        JSONArray coords = geometry.getJSONArray(featuresJson[4]);
                        try {
                            JSONObject description = feature.getJSONObject(featuresJson[5]);
                            DoorsInformation door = new DoorsInformation(description.getString(featuresJson[6]),  featuresJson[8]+description.getString(featuresJson[7]),coords.getDouble(1), coords.getDouble(0));
                            doorsInformation.add(door);
                        } catch (Exception exception) {
                            Log.e(error[0], error[1]);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            Log.e(error[0], exception.toString());
        }
        createMarkers();
    }

    private void createMarkers() {
        IconFactory iconFactory = IconFactory.getInstance(context);
        Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.pin);
        Icon icon = iconFactory.fromDrawable(iconDrawable);

        if (doorsInformation.size() > 0) {
            for (int i = 0; i < doorsInformation.size(); i++) {
                MarkerViewOptions mark = new MarkerViewOptions()
                        .position(new LatLng(doorsInformation.get(i).getLati(), doorsInformation.get(i).getlongi()))
                        .title(doorsInformation.get(i).getTitle())
                        .snippet(doorsInformation.get(i).getDescription())
                        .icon(icon);
                markers.add(mark);
            }
        }
    }

    public void drawDoors() {
        if (mapboxMap.getMarkers().isEmpty()) {
            for (int i = 0; i < markers.size(); i++) {
                mapboxMap.addMarker(markers.get(i));
            }
        }
    }

    public void hideDoors() {
        if (!mapboxMap.getMarkers().isEmpty()) {
            List<Marker> listDoors = mapboxMap.getMarkers();
            for (int i = 0; i < listDoors.size(); i++)
                mapboxMap.removeMarker(listDoors.get(i));
        }
    }

    public String[] getDoorsListTitle() {
        String[] list = {error[1]};
        try {
            list = new String[doorsInformation.size()];
            for (int i = 0; i < doorsInformation.size(); i++) {
                list[i] = doorsInformation.get(i).getTitle();
            }
            return list;
        } catch (Exception exception) {
            Log.e(error[0], error[1] + exception.toString());
        }
        return list;
    }
}