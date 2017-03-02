package com.example.julien.geoapp.services.repositoryServices;

import com.example.julien.geoapp.models.Doors;

import java.util.ArrayList;

/**
 * Created by Julien on 2017-02-13.
 */

public interface IDoorsRepositoryService {
    String[] getDoorsList();

    ArrayList<Doors> getAllDoors();
}
