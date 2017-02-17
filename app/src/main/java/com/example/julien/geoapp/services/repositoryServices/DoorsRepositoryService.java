package com.example.julien.geoapp.services.repositoryServices;

import com.example.julien.geoapp.models.DoorsInformationsForSearching;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-10.
 */

public class DoorsRepositoryService {


    private MapboxMap mapboxMap;
    private String request;
    private ArrayList<DoorsInformationsForSearching> doorsInformationsForSearching;

    public DoorsRepositoryService(MapboxMap mapboxMap, String request) {
        this.mapboxMap = mapboxMap;
        this.request = request;
        this.doorsInformationsForSearching = new ArrayList<DoorsInformationsForSearching>();
        //initlist();
    }

    public ArrayList<DoorsInformationsForSearching> getDoorsArrayList(String s) {
        DoorsInformationsForSearching doors = new DoorsInformationsForSearching("allo", "salut", 2, 12.33312321, 23.43545340);
        DoorsInformationsForSearching doors2 = new DoorsInformationsForSearching("allo2", "salut", 2, 12.33312321, 23.43545340);
        doorsInformationsForSearching.add(doors);
        doorsInformationsForSearching.add(doors2);
        return doorsInformationsForSearching;
    }
    public DoorsInformationsForSearching getSpecificDoors(String porte) {
        //TODO BOUCLE FOR
        DoorsInformationsForSearching doors2 = new DoorsInformationsForSearching("allo2", "salut", 2, 12.33312321, 23.43545340);
        doorsInformationsForSearching.add(doors2);
        return doorsInformationsForSearching.get(0);
    }
    public String[] getDoorsList() {
       String[] list = {"Dsfdadfsafds","fsfsadsdfsfda","fsdfasdfsadfsad","sdfsdfsadf"};
        return list;
    }

}