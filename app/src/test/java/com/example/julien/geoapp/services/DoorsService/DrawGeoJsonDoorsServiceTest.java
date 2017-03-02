package com.example.julien.geoapp.services.doorsService;

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
public class DrawGeoJsonDoorsServiceTest {


    @Test
    public void methodHideDoorsShouldBeCalled() {

        //  ARRANGE
        IDrawGeoJsonDoorsService service = Mockito.mock(IDrawGeoJsonDoorsService.class);

        // ACT
        service.hideDoors();

        // ASSERT
        verify(service, atLeastOnce()).hideDoors();
    }
    @Test
    public void methodShowDoorsShouldBeCalled() {

        //  ARRANGE
        IDrawGeoJsonDoorsService service = Mockito.mock(IDrawGeoJsonDoorsService.class);

        // ACT
        service.drawDoors();

        // ASSERT
        verify(service, atLeastOnce()).drawDoors();
    }
    @Test
    public void methodDrawDoorsShouldBeCalled() {

        //  ARRANGE
        IDrawGeoJsonDoorsService service = Mockito.mock(IDrawGeoJsonDoorsService.class);

        // ACT
        service.drawDoors();

        // ASSERT
        verify(service, atLeastOnce()).drawDoors();
    }

}