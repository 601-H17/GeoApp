package com.example.julien.geoapp.services.doorsService;

import com.example.julien.geoapp.activity.MainActivity;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by Julien on 2017-03-02.
 */
public class DrawGeoJsonDoorsServiceTest {
    @Test
    public void test1(){
        MapboxMap mMapboxMap = Mockito.mock(MapboxMap.class);
        MainActivity mActivity = Mockito.mock(MainActivity.class);
        String json = "";
        IDrawGeoJsonDoorsService service = new DrawGeoJsonDoorsService(mMapboxMap,mActivity,json);
        service.drawDoors();
    }

}