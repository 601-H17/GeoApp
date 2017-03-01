package com.example.julien.geoapp.activity;

import android.database.MatrixCursor;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.api.setDoorsList;
import com.example.julien.geoapp.api.setGeoJsonMaps;
import com.example.julien.geoapp.api.setPathGeoJson;
import com.example.julien.geoapp.models.Doors;
import com.example.julien.geoapp.services.doorsService.DrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.doorsService.IDrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.mapsService.DrawGeoJsonMapsService;
import com.example.julien.geoapp.services.mapsService.IDrawGeoJsonMapsService;
import com.example.julien.geoapp.services.pathService.DrawGeoJsonPathService;
import com.example.julien.geoapp.services.pathService.IDrawGeoJsonPathService;
import com.example.julien.geoapp.services.repositoryServices.DoorsRepositoryService;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
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
    private Button goButton;

    private MapboxMap mapboxMap;
    private AutoCompleteTextView toLocal;
    private ArrayList<String> listSearch;
    private SearchView searchView;
    private LatLng centerCoordinates;

    private String mapGeoJson;
    private String doorsInformation;
    private String pathGeoJson;
    private String searchLocal;
    private double[] CENTER_CEGEP = {46.7867176564811, -71.2869702165109};
    private double[] BOUNDS_CEGEP = {46.78800596023283, -71.28548741340637, 46.784788302609186, -71.28870606422424};
    private double[] CENTER_VS_USER = {0.00002295716656, 0.00000000000007};
    private String MENU_ID = "id";
    private String MENU_REF = "ref";
    private String FROM = "path?localA=";
    private String TO = "&localB=";

    private int DISTANCE_BEFORE_RELOCATION = 200;

    private IDrawGeoJsonMapsService mapsDrawService;
    private IDrawGeoJsonDoorsService doorsDrawService;
    private IDrawGeoJsonPathService pathDrawService;
    private DoorsRepositoryService doorsRepositoryService;

    private SimpleCursorAdapter searchAdapter;

    private double positionZoom;

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
        goButton = (Button) findViewById(R.id.button4);
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

        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setPathGeoJson(MainActivity.this, FROM + searchView.getQuery().toString().toUpperCase() + TO + toLocal.getText().toString().toUpperCase()).execute();
                new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + searchView.getQuery().toString().toUpperCase()).execute();
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
        goButton.setVisibility(View.GONE);
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
        final String[] from = new String[]{MENU_ID, MENU_REF};
        final int[] to = new int[]{R.id.textView1, R.id.textView2};
        searchAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.spinner_item_test, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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

        searchView.setQuery(listSearch.get(i), true);
        searchLocal = searchView.getQuery().toString().toUpperCase();
        setUserLocation();
    }

    private void searchQuery(String newText) {
        listSearch = new ArrayList<>();
        new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + newText).execute();
        final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, MENU_ID, MENU_REF});
        if (doorsRepositoryService != null) {
            ArrayList<Doors> list = doorsRepositoryService.allDoors();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().toLowerCase().startsWith(newText.toLowerCase()) || list.get(i).getTeacher().toLowerCase().startsWith(newText.toLowerCase())) {
                    mc.addRow(new Object[]{i, list.get(i).getTitle(), list.get(i).getTeacher()});
                    listSearch.add(list.get(i).getTitle());
                }
            }
            searchAdapter.changeCursor(mc);
        }
        if (newText.length() >= 4) {
            toLocal.setVisibility(View.VISIBLE);
            goButton.setVisibility(View.VISIBLE);
        } else {
            toLocal.setVisibility(View.GONE);
            goButton.setVisibility(View.GONE);
        }
    }

    private void setUserLocation() {
        try {
            Doors door = doorsRepositoryService.getSpecificDoor(searchLocal);
            LatLng user = new LatLng(door.getlongi() - CENTER_VS_USER[0], door.getLati() - CENTER_VS_USER[1]);
            int positionZoomCameraMove = 19;
            CameraPosition position = new CameraPosition.Builder()
                    .target(user)
                    .zoom(positionZoomCameraMove)
                    .tilt(0)
                    .bearing(0)
                    .build();
            mapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position));
            positionZoom = positionZoomCameraMove;
            showDoors();
        } catch (Exception e) {
            Log.d(Message.ERROR[0], Message.ERROR[1]);
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
                if (calculateDistance() >= DISTANCE_BEFORE_RELOCATION) {
                    animateCamera();
                }
            }
        });
        this.mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                return false;
            }
        });
    }

    private void setCenterCoordinates(int width, int height, Projection projection) {
        PointF centerPoint = new PointF(width / 2, height / 2);
        this.centerCoordinates = new LatLng(projection.fromScreenLocation(centerPoint));
    }

    private double calculateDistance() {
        float[] results = new float[1];
        Location.distanceBetween(CENTER_CEGEP[0], CENTER_CEGEP[1], centerCoordinates.getLatitude(), centerCoordinates.getLongitude(), results);
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
                .include(new LatLng(BOUNDS_CEGEP[0], BOUNDS_CEGEP[1]))
                .include(new LatLng(BOUNDS_CEGEP[2], BOUNDS_CEGEP[3]))
                .build();
        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
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
        if (searchLocal != null) {
            setUserLocation();
        }
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
        pathDrawService.drawPath(pathGeoJson);
        doorsDrawService.addFromToMarkers(pathGeoJson);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
