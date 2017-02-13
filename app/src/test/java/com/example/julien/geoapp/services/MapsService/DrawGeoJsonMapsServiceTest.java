package com.example.julien.geoapp.services.MapsService;

import com.example.julien.geoapp.services.DoorsService.IDrawGeoJsonDoorsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Julien on 2017-02-13.
 */

@RunWith(MockitoJUnitRunner.class)
public class DrawGeoJsonMapsServiceTest {

    @Test
    public void methodDrawMapsShouldBeCalled() {

        //  ARRANGE
        IDrawGeoJsonMapsService service = Mockito.mock(IDrawGeoJsonMapsService.class);

        // ACT
        service.drawMaps();

        // ASSERT
        verify(service, atLeastOnce()).drawMaps();
    }
}