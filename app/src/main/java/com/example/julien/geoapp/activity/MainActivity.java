package com.example.julien.geoapp.activity;
import android.database.MatrixCursor;
import android.graphics.PointF;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.api.RequestMapsApi;
import com.example.julien.geoapp.models.Point;
import com.example.julien.geoapp.services.DrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.DrawGeoJsonMapsService;
import com.example.julien.geoapp.services.DrawGeoJsonPathService;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private LatLng centerCoordinates;
    private String mapGeoJson;
    private DrawGeoJsonMapsService mapsService;
    private DrawGeoJsonDoorsService doorsService;
    private DrawGeoJsonPathService pathService;
    private SimpleCursorAdapter searchAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setMap(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchMenu);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setSuggestionsAdapter(searchAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                setTextSearch(i);
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                setTextSearch(i);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setTextSearch(int i) {
//        MatrixCursor obj= (MatrixCursor)searchAdapter.getItem(i);
//        String str=obj.toString();
//        Toast.makeText(MainActivity.this, "Item is= "+str, Toast.LENGTH_LONG).show();
    }

    private void searchQuery(String newText) {
        final MatrixCursor mc = new MatrixCursor(new String[]{ BaseColumns._ID, "localName" });
        String[] list = doorsService.getDoorsListTitle();
        for (int i=0; i<list.length; i++) {
            if (list[i].toLowerCase().startsWith(newText.toLowerCase()))
                mc.addRow(new Object[] {i, list[i]});
        }
        searchAdapter.changeCursor(mc);
    }


    private void initServices() {
        mapsService = new DrawGeoJsonMapsService(mapboxMap,mapGeoJson);
        doorsService = new DrawGeoJsonDoorsService(mapboxMap,this,mapGeoJson);
        mapsService.drawMaps();
        doorsService.drawDoors();
    }

    private void setView() {
        setContentView(R.layout.activity_main);
        final String[] from = new String[] {"localName"};
        final int[] to = new int[] {android.R.id.text1};
        searchAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.spinner_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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

