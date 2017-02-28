package com.example.julien.geoapp.services.repositoryServices;

import android.util.Log;

import com.example.julien.geoapp.models.Doors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-10.
 */

public class DoorsRepositoryService implements IDoorsRepositoryService {

    private String featuresJson[] = {"features", "geometry", "type", "Point", "coordinates", "properties", "ref", "entrance", "Disponible: "};
    private String error[] = {"TAG", "Exception Loading GeoJSON "};
    private String request;
    private ArrayList<Doors> doors;

    public DoorsRepositoryService(String request) {
        this.request = request;
        this.doors = new ArrayList<Doors>();
        initDoors();
    }

    private void initDoors() {
        try {
            JSONArray json = new JSONArray(request);
            int i = json.length();
            for (int fn = 0; fn < i; fn++) {
                Doors classToAdd = new Doors(json.getJSONObject(fn).getString("name"), json.getJSONObject(fn).getString("description"), json.getJSONObject(fn).getInt("floor"), json.getJSONObject(fn).getJSONObject("point").getDouble("lat"), json.getJSONObject(fn).getJSONObject("point").getDouble("lng"));
                doors.add(classToAdd);
            }
        } catch (Exception exception) {
            Log.e(error[0], exception.toString());
        }
    }


    public String[] getDoorsList() {
        String[] list = new String[doors.size()];
        for (int i = 0; i < doors.size(); i++) {
            list[i] = doors.get(i).getTitle();
        }
        return list;
    }

    public Doors getSpecificDoor(String name) {
        Doors toReturn = null;
        for (int i = 0; i < doors.size(); i++) {
            if (doors.get(i).getTitle().equals(name)) {
                toReturn = doors.get(i);
            }
        }
        return toReturn;
    }

}