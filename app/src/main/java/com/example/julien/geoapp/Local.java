package com.example.julien.geoapp;

import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-02.
 */
public class Local {

    private String routeMap = "/corridors";
    private String apiUrl = "https://csf-geo-app.herokuapp.com/api";
    private ArrayList<LatLng> locals;

    public Local(){
        locals = new ArrayList<>();
        getAllLocals();
    }

    private void getAllLocals() {

        String request;
        try {
            URL url = new URL(apiUrl + routeMap);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                request = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
            JSONObject json = new JSONObject(request);
            JSONArray features = json.getJSONArray("features");
            int numberFeatures = 0;
            while (numberFeatures <= features.length()) {
                JSONObject feature = features.getJSONObject(numberFeatures);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");
                     if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Point")){
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        LatLng latLng = new LatLng(coords.getDouble(1), coords.getDouble(0));
                         locals.add(latLng);
                    }
                }
                numberFeatures++;
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
    }
    public LatLng getLocal(LatLng coord){
        for(int i=0;i<locals.size();i++){
            if(locals.get(i) == coord){
                return locals.get(i);
            }
        }
        return coord;
    }
    public boolean isClose (LatLng coord){
        LatLng theCloser;
        double distanceCoordOrigin;
        double x= 2;
        for(int i=0;i<locals.size();i++){
            distanceCoordOrigin = Math.sqrt((((locals.get(i).getLatitude())-(coord.getLatitude()*coord.getLatitude()))*((locals.get(i).getLatitude())-(coord.getLatitude()*coord.getLatitude())))+(((locals.get(i).getLongitude())-(coord.getLongitude()*coord.getLongitude()))*((locals.get(i).getLongitude())-(coord.getLongitude()*coord.getLongitude()))));
        }
        return true;
    }
}
