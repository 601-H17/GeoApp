package com.example.julien.geoapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

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

public class DrawGeoJson  extends AsyncTask<Void, Void, List<LatLng>> {


    private MapboxMap mapboxMap;

    public DrawGeoJson(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    @Override
    protected List<LatLng> doInBackground(Void... voids) {

        ArrayList<LatLng> points = new ArrayList<>();
        String resquestRespond;
        try {
            URL url = new URL("https://csf-geo-app.herokuapp.com/api/map");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                resquestRespond = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
            JSONObject json = new JSONObject(resquestRespond.toString());
            JSONArray features = json.getJSONArray("features");
            int numberFeatures = 0;
            while (numberFeatures <= features.length()) {
                JSONObject feature = features.getJSONObject(numberFeatures);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
                numberFeatures++;
                draw(points);
                points = new ArrayList<>();
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }

        return points;
    }

    protected void draw(List<LatLng> points) {
        super.onPostExecute(points);
        if (points.size() > 0) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .color(Color.parseColor("#3bb2d0"))
                    .width(2));
        }
    }
}