package com.example.julien.geoapp.services.pathfinderService;

import android.graphics.Color;
import android.util.Log;

import com.example.julien.geoapp.R;
import com.example.julien.geoapp.activity.MainActivity;
import com.example.julien.geoapp.api.setPathGeoJson;
import com.example.julien.geoapp.services.doorsService.IDrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.pathService.DrawGeoJsonPathService;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic on 16/02/2017.
 */

public class PathfinderService implements IPathfinderService {

    private MainActivity mainActivity;
    private MapboxMap mapboxMap;
    private String startingPoint;
    private String endingPoint;

    private String finalEndingPoint;


    private DrawGeoJsonPathService pathDrawService;
    private IDrawGeoJsonDoorsService doorsDrawService;

    public PathfinderService(MainActivity mainActivity, MapboxMap mapboxmap, String startingPoint, String endingPoint) {
        this.mainActivity = mainActivity;
        this.mapboxMap = mapboxmap;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.finalEndingPoint = endingPoint;
    }

    //Déroulement: 1. Vers la bonne aile     2. Vers le bon étage     3. Vers le local

    //IMP: Indiquer le chemin par "Steps"

    //TODO: Faire entrées pour les ailes (geojson)
    //TODO: Faire une requête à l'API "Pathfinder" pour chaque escalier dans la liste. (avoir la distance de chacun)
    //TODO: Vérifier le format de tous les string utilisant "substring"
    //TODO: Faire afficher le chemin à partir de "MainActivity"

    public void pathfind(){
        //Instancier le service quand une recherche est lancé
       // this.pathDrawService = new DrawGeoJsonPathService(this.mapboxMap);

        //Variable temporaire pour tester
        this.startingPoint = "G-116";
        this.endingPoint = "G-160";

        String startingWing = this.startingPoint.substring(0,1);
        String endingWing = this.endingPoint.substring(0,1);


        //1. LOCAL -> SORTIE AILE
        //TODO: 1. Trouver toutes les sorties de l'aile -> s'il n'y a pas de sortie sur l'étage, il faut regarder sur les autres étages
        if(startingWing.equals(endingWing)){
            String startingFloor = this.startingPoint.substring(2,3);
            String endingFloor = this.endingPoint.substring(2,3);
            if(startingFloor.equals(endingFloor)){
                drawPath(this.find("path?localA="+this.startingPoint+"&localB="+this.endingPoint));
                //new setPathGeoJson(this.mainActivity, "path?localA="+this.startingPoint+"&localB="+this.endingPoint).execute();
            }
            else {
               /* String[] sameFloorStaircases = this.findingSameFloorStaircases(startingWing, startingFloor);
                for (String staircase: sameFloorStaircases) {
                    new setPathGeoJson(this.mainActivity, "path?localA="+this.startingPoint+"&localB="+staircase).execute();
                    StringBuilder newPoint = new StringBuilder(staircase);
                    newPoint.setCharAt(3, endingFloor.charAt(0));
                    new setPathGeoJson(this.mainActivity, "path?localA="+newPoint+"&localB="+this.endingPoint).execute();
                */
            }
        }
        /*
        else{
            if(findingSameFloorWingExits(startingWing).length == 0){
                String[] staircases = findingSameFloorStaircases();
                for (String staircase: staircases) {

                }

            }

            String wingEntry;

            //2. AILE -> AILE
            pathfindWingToWing(startingPoint, wingEntry);
        }


        //3. ESCALIER

        int startingFloor = Integer.parseInt(wingEntry.substring(2,3));
        int endingFloor = Integer.parseInt(endingPoint.substring(2,3));

        if(startingFloor != endingFloor){

            String[] sameFloorStaircases = findingSameFloorStaircases();
        }
        */


    }

    private void pathfindStaircases(String startingPoint, String endingPoint){

        new setPathGeoJson(this.mainActivity, "path?localA="+ startingPoint + "&localB=" + endingPoint).execute();
    }

    public void pathfindWingToWing(String startingPoint, String endingPoint){
        //TODO: 1. Trouver la meilleure entrée de l'aile d'arrivée.

        //TODO: 2. Trouver le meilleur chemin de la sortie vers l'entrée de l'autre aile
    }

    private String[] findingSameFloorWingExits(String wing) {
        return null;
    }

    //TODO: Vérifier que les escaliers se rendent bien à l'étage désiré
    public String[] findingSameFloorStaircases(String currentFloor, String currentWing){
        ArrayList<String> staircaseOnSameFloor = new ArrayList<String>();
        String[] markerList = doorsDrawService.getDoorsListTitle();
        for(int i = 0; i < markerList.length; i++){
            String staircase = markerList[i].substring(1,2);
            String staircaseFloor = markerList[i].substring(2,3);
            String staircaseWing = markerList[i].substring(0,1);
            if(staircase.equals("E") && staircaseWing.equals(currentWing) && staircaseFloor.equals(currentFloor)){
                staircaseOnSameFloor.add(markerList[i]);
            }
        }
        return staircaseOnSameFloor.toArray(new String[staircaseOnSameFloor.size()]);
    }

    public String find(String routeMap) {

        String request = "";
        try {
            URL url = new URL(this.mainActivity.getBaseContext().getString(R.string.apiUrl) + routeMap);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(this.mainActivity.getBaseContext().getString(R.string.headerAuthorization), this.mainActivity.getBaseContext().getString(R.string.headerToken));
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                request = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
        return request;
    }


    public void drawPath(String pathString) {

        ArrayList<LatLng> pointDoors = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(pathString);
            JSONArray path = json.getJSONArray("path");
            for (int i = 0; i < path.length(); i++) {
                JSONArray coord = path.getJSONArray(i);
                LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                pointDoors.add(latLng);
            }
        } catch (Exception exception) {
            Log.e("TAG", "Exception Loading GeoJSON: " + exception.toString());
        }
        drawLinesCorridors(pointDoors);
    }
    private void drawLinesCorridors(List<LatLng> pointDoors) {
        if (pointDoors.size() > 0) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(pointDoors)
                    .color(Color.parseColor("#cb2c39"))
                    .width(2));
        }
    }

}
