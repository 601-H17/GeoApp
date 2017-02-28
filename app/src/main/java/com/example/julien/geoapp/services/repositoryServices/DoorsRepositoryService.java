package com.example.julien.geoapp.services.repositoryServices;

import android.util.Log;

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.models.Doors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-10.
 */

public class DoorsRepositoryService implements IDoorsRepositoryService {

    private String request;
    private ArrayList<Doors> doors;

    public DoorsRepositoryService(String request) {
        this.request = request;
        this.doors = new ArrayList<>();
        initDoors();
    }

    private void initDoors() {
        try {
            JSONArray json = new JSONArray(request);
            int i = json.length();
            for (int fn = 0; fn < i; fn++) {
                String ref = json.getJSONObject(fn).getString(Message.FEATURES_JSON_PATH[0]);
                String description = json.getJSONObject(fn).getString(Message.FEATURES_JSON_PATH[1]);
                int floor = json.getJSONObject(fn).getInt(Message.FEATURES_JSON_PATH[2]);
                double lat = json.getJSONObject(fn).getJSONObject(Message.FEATURES_JSON_PATH[3]).getDouble(Message.FEATURES_JSON_PATH[4]);
                double longi = json.getJSONObject(fn).getJSONObject(Message.FEATURES_JSON_PATH[3]).getDouble(Message.FEATURES_JSON_PATH[5]);
                String teacher = "Paul Moisant";
                Doors classToAdd = new Doors(ref, description, teacher, floor, lat, longi);
                doors.add(classToAdd);
            }
        } catch (Exception exception) {
            Log.e(Message.ERROR[0], exception.toString());
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

    public ArrayList<Doors> allDoors() {
        return doors;
    }

}