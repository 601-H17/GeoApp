package com.example.julien.geoapp.activity;

import android.database.MatrixCursor;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.api.setDoorsList;
import com.example.julien.geoapp.api.setGeoJsonMaps;
import com.example.julien.geoapp.api.setPathGeoJson;
import com.example.julien.geoapp.services.doorsService.DrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.doorsService.IDrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.mapsService.DrawGeoJsonMapsService;
import com.example.julien.geoapp.services.mapsService.IDrawGeoJsonMapsService;
import com.example.julien.geoapp.services.pathService.DrawGeoJsonPathService;
import com.example.julien.geoapp.services.pathService.IDrawGeoJsonPathService;
import com.example.julien.geoapp.services.repositoryServices.DoorsRepositoryService;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private Button firstFloorButton;
    private Button secondFloorButton2;
    private Button thirdFloorButton3;
    private Button go;

    private MapboxMap mapboxMap;
    private AutoCompleteTextView toLocal;
    private ArrayList<String> listSearch;
    private SearchView searchView;
    private LatLng centerCoordinates;
    private double[] centerLatLongCegep = {46.7867176564811, -71.2869702165109};
    private double[] boundsCegep = {46.78800596023283, -71.28548741340637, 46.784788302609186, -71.28870606422424};

    private String mapGeoJson;
    private String doorsInformation;
    private String pathGeoJson;

    private IDrawGeoJsonMapsService mapsDrawService;
    private IDrawGeoJsonDoorsService doorsDrawService;
    private IDrawGeoJsonPathService pathDrawService;
    private DoorsRepositoryService doorsRepositoryService;

    private SimpleCursorAdapter searchAdapter;

    private double positionZoom;
    private int distanceBeforeRelocation = 200;
    private String menuId = "localName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setButtonListener();
        setMap(savedInstanceState);
        setAdapter();
    }

    private void setView() {

        setContentView(R.layout.activity_main);
        firstFloorButton = (Button) findViewById(R.id.button);
        secondFloorButton2 = (Button) findViewById(R.id.button2);
        thirdFloorButton3 = (Button) findViewById(R.id.button3);
        go = (Button) findViewById(R.id.button4);
        toLocal = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

    }

    private void setButtonListener() {
        toLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + s).execute();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + s).execute();

            }

            @Override
            public void afterTextChanged(Editable s) {
                new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + s).execute();

            }
        });

        go.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setPathGeoJson(MainActivity.this, "path?localA=" + searchView.getQuery().toString().toUpperCase() + "&localB=" + toLocal.getText().toString().toUpperCase()).execute();
            }
        });
        firstFloorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map)).execute();
                mapboxMap.clear();
            }
        });
        secondFloorButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map2)).execute();
                mapboxMap.clear();
            }
        });
        thirdFloorButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map3)).execute();
                mapboxMap.clear();
            }
        });
        go.setVisibility(View.GONE);
        toLocal.setVisibility(View.GONE);
    }

    private void setMap(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, getString(R.string.access_token));
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        new setGeoJsonMaps(MainActivity.this, getString(R.string.map)).execute();
    }

    private void setAdapter() {
        final String[] from = new String[]{menuId};
        final int[] to = new int[]{android.R.id.text1};
        searchAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.spinner_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchMenu);
        searchView = (SearchView) item.getActionView();
        searchView.setSuggestionsAdapter(searchAdapter);
        toLocal = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void initSearchView(SearchView searchView) {
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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
    }

    private void setTextSearch(int i) {
        searchView.setQuery(listSearch.get(i), false);
    }

    private void searchQuery(String newText) {
        listSearch = new ArrayList<>();
        new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + newText).execute();
        final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, menuId});
        if (doorsRepositoryService != null) {
            String[] list = doorsRepositoryService.getDoorsList();
            for (int i = 0; i < list.length; i++) {
                if (list[i].toLowerCase().startsWith(newText.toLowerCase())) {
                    mc.addRow(new Object[]{i, list[i]});
                    listSearch.add(list[i]);
                }
            }
            searchAdapter.changeCursor(mc);
        }
        if (newText.length() >= 3) {
            toLocal.setVisibility(View.VISIBLE);
            go.setVisibility(View.VISIBLE);
        } else {
            toLocal.setVisibility(View.GONE);
            go.setVisibility(View.GONE);
        }
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
                positionZoom = position.zoom;
                calculateDistance();
                showDoors();
                if (calculateDistance() >= distanceBeforeRelocation) {
                    animateCamera();
                }
            }
        });
    }

    private void setCenterCoordinates(int width, int height, Projection projection) {
        PointF centerPoint = new PointF(width / 2, height / 2);
        this.centerCoordinates = new LatLng(projection.fromScreenLocation(centerPoint));
    }

    private double calculateDistance() {
        float[] results = new float[1];
        Location.distanceBetween(centerLatLongCegep[0], centerLatLongCegep[1], centerCoordinates.getLatitude(), centerCoordinates.getLongitude(), results);
        return results[0];
    }

    private void showDoors() {
        if (doorsDrawService != null) {
            int positionZoomBeforePins = 18;
            if (positionZoom >= positionZoomBeforePins) {
                doorsDrawService.drawDoors();
            } else {
                doorsDrawService.hideDoors();
            }
        }
    }

    private void animateCamera() {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(boundsCegep[0], boundsCegep[1]))
                .include(new LatLng(boundsCegep[2], boundsCegep[3]))
                .build();
        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10), 200);
    }

    public void setMapGeoJson(String map) {
        this.mapGeoJson = map;
        initDrawableMaps();
    }

    private void initDrawableMaps() {
        mapsDrawService = new DrawGeoJsonMapsService(mapboxMap, mapGeoJson);
        doorsDrawService = new DrawGeoJsonDoorsService(mapboxMap, this, mapGeoJson);
        pathDrawService = new DrawGeoJsonPathService(mapboxMap);
        mapsDrawService.drawMaps();
        showDoors();
    }

    public void setDoorListQuery(String doors) {
        this.doorsInformation = doors;
        initDoorsList();
    }

    private void initDoorsList() {
        doorsRepositoryService = new DoorsRepositoryService(doorsInformation);
        setAdapterString();
    }

    private void setAdapterString() {
        ArrayAdapter toAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doorsRepositoryService.getDoorsList());
        toLocal.setAdapter(toAdapter);
    }

    public void setPathGeoJsonString(String path) {
        pathGeoJson = path;
        initPath();
    }

    private void initPath() {
        mapboxMap.clear();
        mapsDrawService.drawMaps();
        showDoors();
        pathDrawService.drawPath(pathGeoJson);
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
