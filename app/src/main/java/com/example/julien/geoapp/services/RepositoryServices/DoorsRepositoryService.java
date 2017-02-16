package com.example.julien.geoapp.services.repositoryServices;

import com.example.julien.geoapp.models.DoorsInformationsForSearching;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

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