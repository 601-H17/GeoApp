package com.example.julien.geoapp.services.repositoryServices;

import android.util.Log;

import com.example.julien.geoapp.models.DoorsInformationsForSearching;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-10.
 */

public class DoorsRepositoryService implements IDoorsRepositoryService {

    private String featuresJson[] = {"features", "geometry", "type", "Point", "coordinates", "properties", "ref", "entrance", "Disponible: "};
    private String error[] = {"TAG", "Exception Loading GeoJSON "};
    private String request;
    private ArrayList<DoorsInformationsForSearching> doorsInformationsForSearching;

    public DoorsRepositoryService( String request) {
        this.request = request;
        this.doorsInformationsForSearching = new ArrayList<DoorsInformationsForSearching>();
        initDoors();
    }

    private void initDoors() {
        try {
            JSONArray  json = new JSONArray (request);
            int i = json.length();
            for (int fn = 0; fn < i; fn++) {
                DoorsInformationsForSearching classToAdd = new DoorsInformationsForSearching(json.getJSONObject(fn).getString("name"),json.getJSONObject(fn).getString("description"),1,2,3);
                doorsInformationsForSearching.add(classToAdd);
            }
        } catch (Exception exception) {
            Log.e(error[0], exception.toString());
        }
    }


    public String[] getDoorsList() {
        String[] list = new String[doorsInformationsForSearching.size()];
        for(int i=0;i<doorsInformationsForSearching.size();i++ ){
            list[i] = doorsInformationsForSearching.get(i).getTitle();
        }
        return list;
    }

}