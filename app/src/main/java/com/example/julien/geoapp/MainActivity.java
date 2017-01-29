package com.example.julien.geoapp;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DrawGeojsonLineActivity";

    private MapView mapView;
    private MapboxMap mapboxMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        new DrawGeoJson().execute();
        MarkerViewOptions markerViewOptions = new MarkerViewOptions()
                .position(new LatLng(46.78868330000, -71.29073110000));

        mapboxMap.addMarker(markerViewOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private class DrawGeoJson extends AsyncTask<Void, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            ArrayList<LatLng> points = new ArrayList<>();

            try {
                InputStream inputStream = getAssets().open("cegep-locaux.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                inputStream.close();
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                int numberFeatures = 0;
                while(numberFeatures <= features.length() ){
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
                Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
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
}

