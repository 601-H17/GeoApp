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
    public void getDoorsListShouldReturbList() {
        String[] doorsList = {"G-120", "G=122"};

        //  ARRANGE
//        IDrawGeoJsonDoorsService service = Mockito.mock(IDrawGeoJsonDoorsService.class);
//
//        // ACT
//        when(service.getDoorsListTitle().thenReturn(doorsList);
//
//        // ASSERT
//        assertEquals(service.getDoorsListTitle(), doorsList);
    }

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
    public void methodDrawDoorsShouldBeCalled() {

        //  ARRANGE
        IDrawGeoJsonDoorsService service = Mockito.mock(IDrawGeoJsonDoorsService.class);

        // ACT
        service.drawDoors();

        // ASSERT
        verify(service, atLeastOnce()).drawDoors();
    }

}