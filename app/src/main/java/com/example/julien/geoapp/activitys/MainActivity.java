package com.example.julien.geoapp.activitys;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.api.RequestMapsApi;
import com.example.julien.geoapp.services.DrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.DrawGeoJsonMapsService;
import com.example.julien.geoapp.services.DrawGeoJsonPath;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private LatLng centerCoordinates;
    private String mapGeoJson;
    private DrawGeoJsonMapsService mapsService;
    private DrawGeoJsonDoorsService doorsService;
    private DrawGeoJsonPath pathService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setMap(savedInstanceState);
    }

    private void initServices() {
        mapsService = new DrawGeoJsonMapsService(mapboxMap,mapGeoJson);
        doorsService = new DrawGeoJsonDoorsService(mapboxMap,this,mapGeoJson);
        mapsService.drawMaps();
        doorsService.drawDoors();
    }

    private void setView() {
        setContentView(R.layout.activity_main);
    }

    private void setMap(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, getString(R.string.access_token));
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        new RequestMapsApi(this,getString(R.string.map)).execute();
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        final Projection projection = mapboxMap.getProjection();
        final int width = mapView.getMeasuredWidth();
        final int height = mapView.getMeasuredHeight();

        this.setCenterCoordinates(width, height, projection);
        this.mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                setCenterCoordinates(width, height, projection);
            }
        });

    }

    private void setCenterCoordinates(int width, int height, Projection projection) {
        PointF centerPoint = new PointF(width / 2, height / 2);
        this.centerCoordinates = new LatLng(projection.fromScreenLocation(centerPoint));
    }

    public void setMapGeoJson(String map){
        this.mapGeoJson = map;
        initServices();
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

