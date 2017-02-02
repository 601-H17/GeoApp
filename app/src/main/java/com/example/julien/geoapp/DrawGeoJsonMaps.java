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

public class DrawGeoJsonMaps extends AsyncTask<Void, Void, List<LatLng>> {


    private MapboxMap mapboxMap;
    private Context context;
    private String routeMap = "/map";
    private String apiUrl = "https://csf-geo-app.herokuapp.com/api";

    public DrawGeoJsonMaps(MapboxMap mapboxMap, Context context) {
        this.mapboxMap = mapboxMap;
        this.context = context;
    }

    @Override
    protected List<LatLng> doInBackground(Void... voids) {

        ArrayList<LatLng> pointsLine = new ArrayList<>();
        ArrayList<LatLng> pointDoors = new ArrayList<>();
        Point descriptionTitle = new Point();

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
                        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {
                            JSONArray coords = geometry.getJSONArray("coordinates");
                            for (int lc = 0; lc < coords.length(); lc++) {
                                JSONArray coord = coords.getJSONArray(lc);
                                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                                pointsLine.add(latLng);
                            }
                        }
                        else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("Point")){
                            JSONArray coords = geometry.getJSONArray("coordinates");
                            LatLng latLng = new LatLng(coords.getDouble(1), coords.getDouble(0));
                            pointDoors.add(latLng);
                            try {
                                JSONObject description = feature.getJSONObject("properties");
                                descriptionTitle = new Point("DISPONIBLE: " + description.getString("entrance"), "LOCAL: " + description.getString("ref"));
                            }catch (Exception exception) {
                                Log.e("TAG", "Exception Loading GeoJSON");
                            }
                        }
                    }
                    numberFeatures++;
                    drawLines(pointsLine);
                    drawDoors(pointDoors,descriptionTitle);
                    pointsLine = new ArrayList<>();
                    pointDoors = new ArrayList<>();
                }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }

        return pointsLine;
    }

    private void drawLines(List<LatLng> pointsLine) {
        super.onPostExecute(pointsLine);
                if (pointsLine.size() > 0) {
                    mapboxMap.addPolyline(new PolylineOptions()
                            .addAll(pointsLine)
                            .color(Color.parseColor("#ffffff"))
                            .width(2));
        }
    }
    private void drawDoors(List<LatLng> pointDoors,Point point) {
        super.onPostExecute(pointDoors);
        IconFactory iconFactory = IconFactory.getInstance(context);
        Drawable iconDrawable = ContextCompat.getDrawable(context, R.drawable.pin);
        Icon icon = iconFactory.fromDrawable(iconDrawable);

        if(pointDoors.size() > 0) {
            mapboxMap.addMarker(new MarkerViewOptions()
                     .position(pointDoors.get(0))
                    .title(point.getTitle())
                    .snippet(point.getDescription())
                    .icon(icon)
                    );
        }
    }
}