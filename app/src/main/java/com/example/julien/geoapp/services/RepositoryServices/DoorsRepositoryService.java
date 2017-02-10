package com.example.julien.geoapp.services.RepositoryServices;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.example.julien.geoapp.models.DoorsInformationsForSearching;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien on 2017-02-10.
 */

public class DoorsRepositoryService {


    private MapboxMap mapboxMap;
    private String request;
    private ArrayList<DoorsInformationsForSearching> doorsInformationsForSearching;

    public DoorsRepositoryService(MapboxMap mapboxMap, String request) {
        this.mapboxMap = mapboxMap;
        this.request = request;
        this.doorsInformationsForSearching = new ArrayList<DoorsInformationsForSearching>();
    }

    private void getDoorsList() {
        //return list with entered research
    }

}