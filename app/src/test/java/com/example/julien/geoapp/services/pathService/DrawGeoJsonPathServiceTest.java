package com.example.julien.geoapp.services.pathService;

import com.example.julien.geoapp.services.mapsService.IDrawGeoJsonMapsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by Julien on 2017-02-23.
 */

@RunWith(MockitoJUnitRunner.class)
public class DrawGeoJsonPathServiceTest {
    @Test
    public void methodDrawPathShouldBeCalled() {

        //YM revoir les tests... ne fait rien (test du mock)... on regarde ca ensemble lundi 


        //  ARRANGE
        String request = "path";
        IDrawGeoJsonPathService service = Mockito.mock(IDrawGeoJsonPathService.class);

        // ACT
        service.drawPath(request);

        // ASSERT
        verify(service, atLeastOnce()).drawPath(request); 
    }

}
