package com.example.julien.geoapp.services.mapsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by Julien on 2017-02-13.
 */

@RunWith(MockitoJUnitRunner.class)
public class DrawGeoJsonMapsServiceTest {

    @Test
    public void methodDrawMapsShouldBeCalled() {

        //YM revoir les tests... ne fait rien (test du mock)... on regarde ca ensemble lundi 

        //  ARRANGE
        IDrawGeoJsonMapsService service = Mockito.mock(IDrawGeoJsonMapsService.class);

        // ACT
        service.drawMaps();

        // ASSERT
        verify(service, atLeastOnce()).drawMaps();
    }
}