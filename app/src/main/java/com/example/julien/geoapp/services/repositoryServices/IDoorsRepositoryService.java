package com.example.julien.geoapp.services.repositoryServices;

import com.example.julien.geoapp.models.DoorsInformationsForSearching;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-13.
 */

public interface IDoorsRepositoryService {
    ArrayList<DoorsInformationsForSearching> getSpecificDoors(String porte);
}
