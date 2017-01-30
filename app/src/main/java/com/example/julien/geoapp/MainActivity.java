package com.example.julien.geoapp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.VisibleRegion;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DrawGeojsonLineActivity";
    private static final LatLng SW_LIMIT = new LatLng(46.78468364611, -71.28986268010);
    private static final LatLng NE_LIMIT = new LatLng(46.78844237314, -71.28435965687);

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
        this.setListeners();
        new DrawGeoJson(mapboxMap).execute();
        MarkerViewOptions markerViewOptions = new MarkerViewOptions().position(new LatLng(46.78868330000, -71.29073110000));
        mapboxMap.addMarker(markerViewOptions);
    }

    //region boundaries-branch functions

    private void setListeners(){
        this.mapboxMap.setOnScrollListener(new MapboxMap.OnScrollListener() {
            @Override
            public void onScroll() {
                restrictMapToBoundingBox();
            }
        });
        this.mapboxMap.setOnFlingListener(new MapboxMap.OnFlingListener() {
            @Override
            public void onFling() {
                restrictMapToBoundingBox();
            }
        });
    }

    private void easeCameraBackToBoundingBox() {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(NE_LIMIT) // Northeast
                .include(SW_LIMIT) // Southwest
                .build();

        this.mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10), 100);
    }

    public void restrictMapToBoundingBox() {
        VisibleRegion visibleRegion = this.mapboxMap.getProjection().getVisibleRegion();

        Double maxLat = NE_LIMIT.getLatitude();
        Double maxLng = NE_LIMIT.getLongitude();
        Double minLat = SW_LIMIT.getLatitude();
        Double minLng = SW_LIMIT.getLongitude();

        if( !(visibleRegion.farLeft.getLatitude() >= minLat && visibleRegion.farLeft.getLatitude() <= maxLat
                && visibleRegion.farLeft.getLongitude() >= minLng && visibleRegion.farLeft.getLongitude() <= maxLng) ) {
            easeCameraBackToBoundingBox();
        }
        if( !(visibleRegion.farRight.getLatitude() >= minLat && visibleRegion.farRight.getLatitude() <= maxLat
                && visibleRegion.farRight.getLongitude() >= minLng && visibleRegion.farRight.getLongitude() <= maxLng) ) {
            easeCameraBackToBoundingBox();
        }
        if( !(visibleRegion.nearLeft.getLatitude() >= minLat && visibleRegion.nearLeft.getLatitude() <= maxLat
                && visibleRegion.nearLeft.getLongitude() >= minLng && visibleRegion.nearLeft.getLongitude() <= maxLng) ) {
            easeCameraBackToBoundingBox();
        }
        if( !(visibleRegion.nearRight.getLatitude() >= minLat && visibleRegion.nearRight.getLatitude() <= maxLat
                && visibleRegion.nearRight.getLongitude() >= minLng && visibleRegion.nearRight.getLongitude() <= maxLng) ) {
            easeCameraBackToBoundingBox();
        }
    }


    //endregion

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

