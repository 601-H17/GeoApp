package com.example.julien.geoapp.services.pathService;

/**
 * Created by Julien on 2017-02-13.
 */

public interface IDrawGeoJsonPathService {
    void drawLinesCorridorsStep();

    int getFloor();

    int getFloorBefore();

    void drawPath(String requestapi);

    void drawLinesCorridorsBack();

    boolean isLastStep();

    int getStep();

    boolean isFistStep();

    void deletePath();
}