package com.example.julien.geoapp.activityTest.MainActivityTest;

import android.test.mock.MockContentProvider;

import com.example.julien.geoapp.activity.MainActivity;
import com.example.julien.geoapp.services.doorsService.IDrawGeoJsonDoorsService;
import com.example.julien.geoapp.services.repositoryServices.IDoorsRepositoryService;
import com.google.common.util.concurrent.ExecutionError;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Julien on 2017-02-08.
 */

public class MainActivityUnitTest {

    private MainActivity mainActivity;
    private MainActivity mainActivityMock;
    private String map;

    @Before
    public void setUp() throws Exception {
        //  ARRANGE
        mainActivityMock = Mockito.mock(MainActivity.class);
        mainActivity = new MainActivity();
        map = "map";
    }

    @Test
    public void mainActivityiIsInstanced() throws Exception {
        Assert.assertTrue(mainActivity != null);
    }

    @Test
    public void setGeoJsonPathIsCalled() throws Exception {

        //YM revoir les tests... ne fait rien (test du mock)... on regarde ca ensemble lundi 
        
        // ACT
        mainActivityMock.setMapGeoJson(map);
        // ASSERT
        verify(mainActivityMock, atLeastOnce()).setMapGeoJson(map);
    }

    @Test
    public void setPathGeoJsonStringIsCalled() throws Exception {
        // ACT
        mainActivityMock.setPathGeoJsonString(map);
        // ASSERT
        verify(mainActivityMock, atLeastOnce()).setPathGeoJsonString(map);
    }

    @Test
    public void setDoorListQueryIsCalled() throws Exception {
        // ACT
        mainActivityMock.setDoorListQuery(map);
        // ASSERT
        verify(mainActivityMock, atLeastOnce()).setDoorListQuery(map);
    }
}
