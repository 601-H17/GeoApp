package com.example.julien.geoapp.services.pathService;

/**
 * Created by Julien on 2017-02-13.
 */

public interface IDrawGeoJsonPathService {
   void drawLinesCorridorsStep();
   int getFloor();
   int getTotalStep();
   void drawPath(String requestapi);
}
