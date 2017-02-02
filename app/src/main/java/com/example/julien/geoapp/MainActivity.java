package com.example.julien.geoapp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.julien.geoapp.SubPos.SubPos;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DrawGeojsonLineActivity";

    private static final String LAT_TAG = "POSITION'S LATITUDE";
    private static final String LONG_TAG = "POSITION'S LONGITUDE";
    private static final String SUBPOS_TAG = "POSITION'S CREATED";

    private MapView mapView;
    private MapboxMap mapboxMap;

    //region SubPos Variables
    private SubPos subpos;
    private double posLongitude;
    private double posLatitude;
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         MapboxAccountManager.start(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        this.subpos = new SubPos(this);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        this.getMyPosition();
    }

    //region SUBPOS Functions
    public void getMyPosition(){
        Log.d(SUBPOS_TAG, String.valueOf(this.subpos.getNodes()));
        this.posLatitude = this.subpos.getPosition().getLat();
        this.posLongitude = this.subpos.getPosition().getLong();

        Log.d(LAT_TAG, String.valueOf(this.posLatitude));
        Log.d(LONG_TAG, String.valueOf(this.posLongitude));
    }
    //endregion

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        new DrawGeoJson(mapboxMap).execute();
        MarkerViewOptions markerViewOptions = new MarkerViewOptions().position(new LatLng(46.78868330000, -71.29073110000));
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
}

