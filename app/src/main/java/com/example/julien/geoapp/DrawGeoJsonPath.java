package com.example.julien.geoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien on 2017-01-30.
 */

public class DrawGeoJsonPath  extends AsyncTask<Void, Void, List<LatLng>>{


    private MapboxMap mapboxMap;
    private Context context;
    private String routeMap;
    private String apiUrl = " http://csf-geo-app.herokuapp.com/api/path?";

    public DrawGeoJsonPath(MapboxMap mapboxMap, Context context,String localA,String localB) {
        this.mapboxMap = mapboxMap;
        this.context = context;
        this.routeMap = "localA="+localA+"&localB="+localB;
    }
    @Override
    protected List<LatLng> doInBackground(Void... voids) {

        ArrayList<LatLng> pointDoors = new ArrayList<>();
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
            JSONArray path = json.getJSONArray("path");
            for(int i=0;i<path.length();i++){
                JSONArray coord = path.getJSONArray(i);
                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                pointDoors.add(latLng);
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
        drawLinesCorridors(pointDoors);
        return pointDoors;
    }

    private void drawLinesCorridors(List<LatLng> pointDoors) {
        super.onPostExecute(pointDoors);
        if (pointDoors.size() > 0) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(pointDoors)
                    .color(Color.parseColor("#cb2c39"))
                    .width(2));
        }
    }
}