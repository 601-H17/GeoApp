package com.example.julien.geoapp.activity;

import android.database.MatrixCursor;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.api.setGeoJsonMaps;
import com.example.julien.geoapp.api.setPathGeoJson;
import com.example.julien.geoapp.services.doorsService.DrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.doorsService.IDrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.mapsService.DrawGeoJsonMapsService;
import com.example.julien.geoapp.services.mapsService.IDrawGeoJsonMapsService;
import com.example.julien.geoapp.services.pathService.DrawGeoJsonPathService;
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


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //region Private Fields (open to view)

    private MapView mapView;
    private Button bouttonEtage;
    private Button bouttonEtage2;
    private Button bouttonEtage3;
    private MapboxMap mapboxMap;

    private LatLng centerCoordinates;
    private double[] centerLatLongCegep = {46.7867176564811, -71.2869702165109};
    private double[] boundsCegep = {46.78800596023283, -71.28548741340637, 46.784788302609186, -71.28870606422424};

    private String mapGeoJson;
    private String doorsInformations;
    private String pathGeoJson;

    private IDrawGeoJsonMapsService mapsDrawService;
    private IDrawGeoJsonDoorsService doorsDrawService;
    private DrawGeoJsonPathService pathDrawService;
    private DoorsRepositoryService doorsRepositoryService;

    private SimpleCursorAdapter searchAdapter;

    private double positionZoom;
    private int positionZoomBeforePins = 18;
    private int distanceBeforeRelocation = 200;
    private String menuId = "localName";

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setButtonListener();
        setMap(savedInstanceState);
        setAdapter();
    }

    //region onCreate methods (open to view)

    private void setView() {

        setContentView(R.layout.activity_main);
        bouttonEtage = (Button) findViewById(R.id.button);
        bouttonEtage2 = (Button) findViewById(R.id.button2);
        bouttonEtage3 = (Button) findViewById(R.id.button3);
    }

    private void setButtonListener() {

        bouttonEtage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map)).execute();
                mapboxMap.clear();
            }
        });
        bouttonEtage2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map2)).execute();
                mapboxMap.clear();
            }
        });
        bouttonEtage3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map3)).execute();
                mapboxMap.clear();
            }
        });
    }

    private void setMap(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, getString(R.string.access_token));
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void setAdapter() {
        final String[] from = new String[]{menuId};
        final int[] to = new int[]{android.R.id.text1};
        searchAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.spinner_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchMenu);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSuggestionsAdapter(searchAdapter);
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    //region onCreateOptionsMenu methods (open to view)

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
        //TODO get selected doors.

    }

    private void searchQuery(String newText) {
        //TODO faire la recherche avec le doorsRepositoryService.getDoorsList, ceci ne retourne que la liste des locaux de l etage selectionnee.

        final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, menuId});
        String[] list = doorsDrawService.getDoorsListTitle();
        for (int i = 0; i < list.length; i++) {
            if (list[i].toLowerCase().startsWith(newText.toLowerCase()))
                mc.addRow(new Object[]{i, list[i]});
        }
        searchAdapter.changeCursor(mc);
    }

    //endregion

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

    //region onMapReady methods (open to view)

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

    //endregion


    //dessine la carte (locaux,porte)
    public void setMapGeoJson(String map) {
        this.mapGeoJson = map;
        initDrawableMaps();
    }

    private void initDrawableMaps() {
        mapsDrawService = new DrawGeoJsonMapsService(mapboxMap, mapGeoJson);
        doorsDrawService = new DrawGeoJsonDoorsService(mapboxMap, this, mapGeoJson);
//
        //instancier le service quand une recherc her est lanc/e (pour linbstant le plan s<affiche suelement quand on init un etage (initmaps).
        pathDrawService = new DrawGeoJsonPathService(mapboxMap);
        //quand lutilisateur entre les locaux lancer la requete api
        new setPathGeoJson(MainActivity.this, "path?localA=G-116&localB=G-160").execute();
        //dessiner le chemin:):)
        pathDrawService.drawPath(pathGeoJson);

        mapsDrawService.drawMaps();
        showDoors();
    }


    //load toutes les portes pour une recherche
    public void setDoorList(String doors) {
        this.doorsInformations = doors;
        initDoorsList();
    }

    private void initDoorsList() {
        doorsRepositoryService = new DoorsRepositoryService(mapboxMap, doorsInformations);
    }
    public void setPathGeoJson(String path){
        this.pathGeoJson=path;
    }

    //region Activity methods (open to view)

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

    //endregion
}

