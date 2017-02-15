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

import java.lang.reflect.Array;
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
        //initlist();
    }

    public ArrayList<DoorsInformationsForSearching> getDoorsArrayList() {
        DoorsInformationsForSearching doors = new DoorsInformationsForSearching("allo", "salut", 2, 12.33312321, 23.43545340);
        DoorsInformationsForSearching doors2 = new DoorsInformationsForSearching("allo2", "salut", 2, 12.33312321, 23.43545340);
        doorsInformationsForSearching.add(doors);
        doorsInformationsForSearching.add(doors2);
        return doorsInformationsForSearching;
    }
    public String[] getDoorsList() {
       String[] list = {"Dsfdadfsafds","fsfsadsdfsfda","fsdfasdfsadfsad","sdfsdfsadf"};
        return list;
    }

}