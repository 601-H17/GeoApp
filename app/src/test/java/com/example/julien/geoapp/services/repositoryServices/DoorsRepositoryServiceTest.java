package com.example.julien.geoapp.services.repositoryServices;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Created by eric3 on 2017-02-17.
 */


@RunWith(MockitoJUnitRunner.class)
public class DoorsRepositoryServiceTest {


    @Test
    public void methodDrawgetSpecificDoorsShouldBeCalled() {

        //  ARRANGE
        IDoorsRepositoryService service = Mockito.mock(IDoorsRepositoryService.class);

        // ACT
        service.getSpecificDoors("G-165");

        // ASSERT
        verify(service, atLeastOnce()).getSpecificDoors("G-165");
    }

}
