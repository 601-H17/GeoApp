package com.example.julien.geoapp.services.repositoryServices;

import com.example.julien.geoapp.services.pathService.IDrawGeoJsonPathService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Julien on 2017-02-23.
 */

@RunWith(MockitoJUnitRunner.class)
public class DoorsRepositoryServiceTest {
    @Test
    public void methodGetListShouldReturnDoorsList() {
        //YM revoir les tests... ne fait rien (test du mock)... on regarde ca ensemble lundi 

        //  ARRANGE
        String[] doorsList = {"G-120", "G=122"};
        IDoorsRepositoryService service = Mockito.mock(IDoorsRepositoryService.class);

        // ACT
        when(service.getDoorsList()).thenReturn(doorsList);

        // ASSERT
        Assert.assertSame(service.getDoorsList(),doorsList);
    }
}
