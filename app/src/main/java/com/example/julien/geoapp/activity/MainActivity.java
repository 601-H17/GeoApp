package com.example.julien.geoapp.activity;

import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.julien.geoapp.Externalization.Message;
import com.example.julien.geoapp.R;
import com.example.julien.geoapp.adapter.CustomAdapterQuery;
import com.example.julien.geoapp.api.setDoorsList;
import com.example.julien.geoapp.api.setGeoJsonMaps;
import com.example.julien.geoapp.api.setPathGeoJson;
import com.example.julien.geoapp.api.setSpecificDoor;
import com.example.julien.geoapp.api.setSpecificDoorInformation;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button firstFloorButton;
    private Button secondFloorButton2;
    private Button thirdFloorButton3;
    private Button nextStepButton;
    private Button beforeButton;

    private MapboxMap mapboxMap;
    private MapView mapView;
    private AutoCompleteTextView toLocal;
    private ArrayList<String> listSearch;
    private SearchView searchView;
    private LatLng centerCoordinates;
    private SimpleCursorAdapter searchAdapter;

    private String specificDoors = "";
    private String MENU_ID = "id";
    private String MENU_REF = "ref";
    private String FROM = "localA=";
    private String TO = "&localB=";
    private String mapGeoJson;
    private String doorsInformation;
    private String pathGeoJson;
    private String searchLocal;
    private String typingQueryNavbar = "";
    private String typingQueryHelp = "";
    private boolean isQueryNavBar = false;
    private boolean isQueryHelpBar = false;
    private double[] CENTER_CEGEP = {46.7867176564811, -71.2869702165109};
    private double[] BOUNDS_CEGEP = {46.78800596023283, -71.28548741340637, 46.784788302609186, -71.28870606422424};
    private double[] CENTER_VS_USER = {0.00002295716656, 0.00000000000007};
    private double positionZoom;
    private int DISTANCE_BEFORE_RELOCATION = 300;

    private IDrawGeoJsonMapsService mapsDrawService;
    private IDrawGeoJsonDoorsService doorsDrawService;
    private IDrawGeoJsonPathService pathDrawService;
    private DoorsRepositoryService doorsRepositoryService;
    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "MainActivity";
    private boolean isLoaded = false;
    private boolean panelIsShowed = false;
    private DoorsRepositoryService specificDoorsRepositoryService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setButtonListener();
        setSlidePanelListener();
        setMap(savedInstanceState);
        setAdapter();
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
                centerUser();
            }
        });
        setMarkerListener();
        setMapClickListener();
    }

    private void setView() {
        setContentView(R.layout.activity_main);
        firstFloorButton = (Button) findViewById(R.id.button);
        secondFloorButton2 = (Button) findViewById(R.id.button2);
        thirdFloorButton3 = (Button) findViewById(R.id.button3);
        toLocal = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        nextStepButton = (Button) findViewById(R.id.next);
        beforeButton = (Button) findViewById(R.id.before);
        beforeButton.setVisibility(View.INVISIBLE);
        nextStepButton.setVisibility(View.INVISIBLE);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }


    private void setMap(Bundle savedInstanceState) {
        //Crée la carte et l'affiche. Affiche l'étage 1 par défault.
        MapboxAccountManager.start(this, getString(R.string.access_token));
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void setAdapter() {
        //Créé l'adapter qui sert à l'autocomplete view de l'action bar.
        final String[] from = new String[]{MENU_ID, MENU_REF};
        final int[] to = new int[]{R.id.textView1, R.id.textView2};
        searchAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.spinner_item_test, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Crée le menu avec l'icone de recherche dans l'action bar.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchMenu);
        searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(Message.START_MESSAGE);
        searchView.setSuggestionsAdapter(searchAdapter);
        initSearchView(searchView);
        int autoCompleteTextViewID = getResources().getIdentifier("android:id/search_src_text", null, null);
        AutoCompleteTextView searchAutoCompleteTextView = (AutoCompleteTextView) searchView.findViewById(autoCompleteTextViewID);
        searchAutoCompleteTextView.setThreshold(1);
        return super.onCreateOptionsMenu(menu);
    }

    private void setTextSearch(int i) {
        searchView.setQuery(listSearch.get(i), true);
        searchLocal = searchView.getQuery().toString().toUpperCase();
        isQueryNavBar = false;
        new setSpecificDoor(MainActivity.this, getString(R.string.getDoorsQuery) + typingQueryNavbar).execute();
    }

    private void searchQuery() {
        //S'occupe de filtrer et afficher les résultat de la recherche dans l'auto complete view de l'Action bar.
        listSearch = new ArrayList<>();
        final MatrixCursor mc = new MatrixCursor(new String[]{BaseColumns._ID, MENU_ID, MENU_REF});
        try {
            ArrayList<Doors> list = doorsRepositoryService.getAllDoors();
            for (int i = 0; i < list.size(); i++) {
                if (find(list.get(i))) {
                    mc.addRow(new Object[]{i, list.get(i).getTitle(), list.get(i).getTeacher()});
                    listSearch.add(list.get(i).getTitle());
                }
            }
            searchAdapter.changeCursor(mc);
        } catch (Exception e) {
            Log.d(Message.ERROR[0], e.toString());
        }
        if (typingQueryNavbar.length() >= 1 && typingQueryNavbar != null) {
            toLocal.setVisibility(View.VISIBLE);
        } else {
            toLocal.setVisibility(View.GONE);
        }
    }

    private boolean find(Doors door) {
        boolean isFind = false;
        String queryRegex = typingQueryNavbar.toLowerCase().replaceAll("[^A-Za-z^0-9.]+", "");
        String titleRegex = door.getTitle().toLowerCase().replaceAll("[^A-Za-z^0-9.]+", "");
        String refRegex = door.getTitle().toLowerCase().replaceAll("[^A-Za-z^0-9.]+", "");
        if (titleRegex.contains(queryRegex) || refRegex.contains(queryRegex)) {
            isFind = true;
        }
        return isFind;
    }


    private void setUserLocation() {
        //Lorsqu'un local est entré, centrer la position du local sur le point vert (l'utilisateur).
        try {
            Doors door = specificDoorsRepositoryService.getSpecificDoor(searchLocal);
            setStair(door);
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
        } catch (Exception e) {
            Log.d(Message.ERROR[0], e.toString());
        }
    }

    private void setStair(Doors door) {
        if (door.getEtage() == 1)
            firstFloorButton.performClick();
        if (door.getEtage() == 2)
            secondFloorButton2.performClick();
    }

    private void setStairNumber(int door) {
        if (door == 1)
            firstFloorButton.performClick();
        if (door == 2)
            secondFloorButton2.performClick();
    }

    private void setCenterCoordinates(int width, int height, Projection projection) {
        //Position du point vert.
        PointF centerPoint = new PointF(width / 2, height / 2);
        this.centerCoordinates = new LatLng(projection.fromScreenLocation(centerPoint));
    }

    private double calculateDistance() {
        //Calcule la distance entre le milieu du cégep et le point vert.
        float[] results = new float[1];
        Location.distanceBetween(CENTER_CEGEP[0], CENTER_CEGEP[1], centerCoordinates.getLatitude(), centerCoordinates.getLongitude(), results);
        return results[0];
    }

    private void showDoors() {
        doorsDrawService.drawDoors();
    }

    private void centerUser() {
        //Si distance entre point vert > que distance DISTANCE_BEFORE_RELOCATION, rescentrer l'utilisateur au milieu du cégep.
        if (calculateDistance() >= DISTANCE_BEFORE_RELOCATION) {
            animateCamera();
        }
    }

    private void animateCamera() {
        //4 points lat lng du cégep pour délimiter la surface de la carte sur l'application.
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(BOUNDS_CEGEP[0], BOUNDS_CEGEP[1]))
                .include(new LatLng(BOUNDS_CEGEP[2], BOUNDS_CEGEP[3]))
                .build();
        mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

    private void setAdapterString() {
        //Set la liste des locaux du champ recherche help bar.
        ArrayList<Doors> doors = doorsRepositoryService.getAllDoors();
        CustomAdapterQuery customAdapter = new CustomAdapterQuery(this, R.layout.spinner_item_test, doors);
        toLocal.setAdapter(customAdapter);
        toLocal.setHint(Message.DESTINATION_MESSAGE);
        customAdapter.notifyDataSetChanged();
    }

    private void initDoorsList() {
        //Sert à instancier le service qui servira à la recherche des services du cégep/classe/etc.Va être remplacé
        //par *things*repository étant donné qu'il n'aura pas seulement des classes.
        doorsRepositoryService = new DoorsRepositoryService(doorsInformation);
        //si l'utilisateur fait une recherche dans l'Action bar, remplir la liste autocomplete
        if (isQueryNavBar)
            searchQuery();
        //Lorsque la réponse de l'API est recue, l'adapter charge sa liste de *things*repository pour l'autocomplete.
        if (isQueryHelpBar)
            setAdapterString();
        setUserLocationNoStair();
    }

    private void initDrawableMaps() {
        //Sert à instancier tous les services lorsqu'un nouvel étage est chargé + le dessiner.
        mapsDrawService = new DrawGeoJsonMapsService(mapboxMap, mapGeoJson);
        doorsDrawService = new DrawGeoJsonDoorsService(mapboxMap, this, mapGeoJson);
        mapsDrawService.drawMaps();
        showDoors();
    }

    private void initPath() {
        //Vérifier si un trajet existe déjà, s'il existe, effacer la carte (dont le trajet fait partie) et la re-dessiner.
        if (pathDrawService != null) {
            pathDrawService.deletePath();
        }
        pathDrawService = new DrawGeoJsonPathService(mapboxMap);
        pathDrawService.drawPath(pathGeoJson);
        pathDrawService.drawLinesCorridorsStep();
        if (pathGeoJson.length() > 20) {
            beforeButton.setVisibility(View.VISIBLE);
            nextStepButton.setVisibility(View.VISIBLE);
            hideStairButton();
            if (pathDrawService.isLastStep()) {
                nextStepButton.setEnabled(true);
                nextStepButton.setText(Message.FINISH);
                beforeButton.setEnabled(false);
                hideStairButton();
            } else {
                nextStepButton.setEnabled(true);
                beforeButton.setEnabled(false);
                hideStairButton();
            }
        }
    }

    public void setPathGeoJsonStringCallback(String path) {
        //Instancier les services lorsque l'API recoit une réponse.
        pathGeoJson = path;
        initPath();
    }

    public void setMapGeoJsonCallback(String map) {
        //Instancier les services lorsque l'API recoit une réponse.
        this.mapGeoJson = map;
        initDrawableMaps();
    }

    public void setDoorsListCallback(String doors) {
        //Instancier les services lorsque l'API recoit une réponse.
        this.doorsInformation = doors;
        //Si une recherche a été lancée, localiser l'utilisateur à la réponse de l'API.
        initDoorsList();
    }

    public void setSpecificDoorInformationCallback(String request) {
        doorsRepositoryService = new DoorsRepositoryService(request);
        showSlidingUpPanel();
    }

    private void showSlidingUpPanel() {
        //Montre le sliding up panel

        TextView localNameTextView = (TextView) findViewById(R.id.local_name);
        TextView localDescriptionTextView = (TextView) findViewById(R.id.local_description);
        TextView localTagTextView = (TextView) findViewById(R.id.local_tag);
        TextView localFloorTextView = (TextView) findViewById(R.id.local_floor);
        ImageView hidePanel = (ImageView) findViewById(R.id.hidePanel);
        hideStairButton();
        hidePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });

        Doors specificDoor = doorsRepositoryService.getAllDoors().get(0);
        localNameTextView.setText(specificDoor.getTitle());
        localDescriptionTextView.setText(specificDoor.getDescription());
        localTagTextView.setText(specificDoor.getTag());
        localFloorTextView.setText(Message.FLOOR_TEXT + Integer.toString(specificDoor.getEtage()));
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        panelIsShowed = true;
    }

    private void setUserLocationNoStair() {
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
        } catch (Exception e) {
            Log.d(Message.ERROR[0], e.toString());
        }
    }

    public void setSpecificDoorsListCallback(String request) {
        specificDoors = request;
        initSpecificDoors();
    }

    private void initSpecificDoors() {
        specificDoorsRepositoryService = new DoorsRepositoryService(specificDoors);
        setUserLocation();
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

    private void setButtonListener() {
        //Une requête à l'API est lancée pour chaque touche entrée lors de la recherche dans helper bar.
        beforeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStairNumber(pathDrawService.getFloorBefore());
                pathDrawService.drawLinesCorridorsBack();
                if (nextStepButton.getText().equals(Message.FINISH)) {
                    nextStepButton.setText(Message.NEXT);
                }
                if (pathDrawService.isLastStep()) {
                    nextStepButton.setText(Message.FINISH);
                    beforeButton.setEnabled(true);
                }
                if (pathDrawService.isLastStep()) {
                    nextStepButton.setText(Message.FINISH);
                    beforeButton.setEnabled(true);
                } else if (pathDrawService.isFistStep()) {
                    beforeButton.setEnabled(false);
                    nextStepButton.setEnabled(true);
                } else {
                    nextStepButton.setEnabled(true);
                    beforeButton.setEnabled(true);
                }
            }
        });
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextStepButton.getText().equals(Message.FINISH)) {
                    nextStepButton.setVisibility(View.INVISIBLE);
                    beforeButton.setVisibility(View.INVISIBLE);
                    nextStepButton.setText(Message.NEXT);
                    pathDrawService.deletePath();
                    showStairButton();
                } else {
                    setStairNumber(pathDrawService.getFloor());
                    pathDrawService.drawLinesCorridorsStep();
                    if (pathDrawService.isLastStep()) {
                        nextStepButton.setText(Message.FINISH);
                        beforeButton.setEnabled(true);
                        hideStairButton();
                    } else if (pathDrawService.isFistStep()) {
                        beforeButton.setEnabled(false);
                        nextStepButton.setEnabled(true);
                        hideStairButton();
                    } else {
                        nextStepButton.setEnabled(true);
                        beforeButton.setEnabled(true);
                        hideStairButton();
                    }
                }
            }
        });

        toLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isQueryHelpBar = true;
                typingQueryHelp = s.toString();
                searchApiQueryHelper();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isQueryHelpBar = true;
                typingQueryHelp = s.toString();
                searchApiQueryHelper();
            }

            @Override
            public void afterTextChanged(Editable s) {
                isQueryHelpBar = true;
                typingQueryHelp = s.toString();
                searchApiQueryHelper();
            }
        });

        toLocal.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isQueryHelpBar = false;
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(adapterView.getApplicationWindowToken(), 0);
                setUserLocation();
                researchPath();
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
        thirdFloorButton3.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {
                new setGeoJsonMaps(MainActivity.this, getString(R.string.map3)).execute();
                mapboxMap.clear();
            }
        });
        toLocal.setVisibility(View.GONE);
    }

    private void researchPath() {
        nextStepButton.setText(Message.NEXT);
        new setPathGeoJson(MainActivity.this, FROM + searchView.getQuery().toString().toUpperCase() + TO + toLocal.getText().toString().toUpperCase()).execute();
        new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + searchView.getQuery().toString().toUpperCase()).execute();
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
                researchPath();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                isQueryNavBar = true;
                typingQueryNavbar = query;
                //Lance la requête.
                searchApiQueryNavbar();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isQueryNavBar = true;
                typingQueryNavbar = newText;
                searchApiQueryNavbar();
                return true;
            }
        });
    }

    private void searchApiQueryNavbar() {
        new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + typingQueryNavbar).execute();
    }

    private void searchApiQueryHelper() {
        new setDoorsList(MainActivity.this, getString(R.string.getDoorsQuery) + typingQueryHelp).execute();
    }

    private void setSearchDoorInformationQuery(String localName) {
        if (localName.charAt(1) != 'E') {
            new setSpecificDoorInformation(MainActivity.this, getString(R.string.getDoorsQuery) + localName).execute();
        }
    }

    private void setSlidePanelListener() {
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, Message.ERROR[1]);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, Message.ERROR[1]);
            }
        });
    }

    private void setMarkerListener() {
        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String localName = doorsDrawService.getLocalName(marker);
                setSearchDoorInformationQuery(localName);
                return false;
            }
        });
    }

    private void setMapClickListener() {
        this.mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                hidePanel();
            }
        });
    }

    private void hidePanel() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        panelIsShowed = false;
        if (beforeButton.getVisibility() == View.VISIBLE) {
            hideStairButton();
        } else {
            showStairButton();
        }
    }

    public void hideStairButton() {
        firstFloorButton.setVisibility(View.INVISIBLE);
        secondFloorButton2.setVisibility(View.INVISIBLE);
        thirdFloorButton3.setVisibility(View.INVISIBLE);
    }

    private void showStairButton() {
        firstFloorButton.setVisibility(View.VISIBLE);
        secondFloorButton2.setVisibility(View.VISIBLE);
        thirdFloorButton3.setVisibility(View.VISIBLE);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean slidingPanelShowed() {
        return panelIsShowed;
    }

}