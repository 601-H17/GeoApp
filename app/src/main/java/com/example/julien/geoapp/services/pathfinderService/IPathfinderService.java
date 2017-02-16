package com.example.julien.geoapp.services.pathfinderService;

/**
 * Created by frederic on 16/02/2017.
 */

public interface IPathfinderService {
    void pathfind();
    void pathfindWingToWing(String startingPoint, String endingPoint);
    String[] findingSameFloorStaircases(String currentFloor, String currentWing);
}
