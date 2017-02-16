package com.example.julien.geoapp.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.api.setGeoJsonMaps;
import com.example.julien.geoapp.api.setPathGeoJson;
import com.example.julien.geoapp.services.DoorsService.DrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.DoorsService.IDrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.MapsService.DrawGeoJsonMapsService;
import com.example.julien.geoapp.services.MapsService.IDrawGeoJsonMapsService;
import com.example.julien.geoapp.services.PathService.DrawGeoJsonPathService;
import com.example.julien.geoapp.services.RepositoryServices.DoorsRepositoryService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Projection;

;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //region Private Fields (open to view)

    private MapView mapView;
    private Button bouttonEtage;
    private Button bouttonEtage2;
    private Button bouttonEtage3;
    private MapboxMap mapboxMap;
    private AutoCompleteTextView autoCompleteTextView;

    private LatLng centerCoordinates;
    private double[] centerLatLongCegep = {46.7867176564811, -71.2869702165109};
    private double[] boundsCegep = {46.78800596023283, -71.28548741340637, 46.784788302609186, -71.28870606422424};

    private String mapGeoJson;
    private String doorsInformations;
    private String pathGeoJson;
    private String selectedDoor;

    private IDrawGeoJsonMapsService mapsDrawService;
    private IDrawGeoJsonDoorsService doorsDrawService;
    private DrawGeoJsonPathService pathDrawService;
    private DoorsRepositoryService doorsRepositoryService;

    private ArrayAdapter searchAdapter;

    private double positionZoom;
    private int positionZoomBeforePins = 18;
    private int distanceBeforeRelocation = 200;
    private String menuId = "localName";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setButtonListener();
        setMap(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setSearchBar() {
        setAdapter();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(searchAdapter);
        initAutoCompleteTextView();
    }

    //region onCreate methods (open to view)

    private void setView() {

        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.search, null);
        actionBar.setCustomView(v);

        setSearchBar();

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

        //Il faut obtenir la vrai liste avec le service DoorsRepositoryService

        String[] listDoor = {"G-165", "G-164", "G-154", "G-163", "G-162"};
        searchAdapter = new ArrayAdapter(this, R.layout.spinner_item, listDoor);
    }

    private void initAutoCompleteTextView() {
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedtextView = (CheckedTextView) view;
                String text = checkedtextView.getText().toString();
                changePosition(text);
            }
        });

    }

    private void changePosition(String text) {
        selectedDoor = text;
        //mapView.;
    }

    //region onCreateOptionsMenu methods (open to view)

    private void searchQuery(String newText) {
        //TODO faire la recherche avec le doorsRepositoryService.getDoorsList, ceci ne retourne que la liste des locaux de l etage selectionnee.

        final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, menuId});
        String[] list = doorsDrawService.getDoorsListTitle();
        for (int i = 0; i < list.length; i++) {
            if (list[i].toLowerCase().startsWith(newText.toLowerCase()))
                mc.addRow(new Object[]{i, list[i]});
        }
        //searchAdapter.changeCursor(mc);

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

    public void setPathGeoJson(String path) {
        this.pathGeoJson = path;
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //endregion
}

